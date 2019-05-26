package com.ractoc.mytasksbackend.tasks;

import com.ractoc.junit5.extensions.SpeedmentDBTestCase;
import com.ractoc.mytasksbackend.common.service.DuplicateEntryException;
import com.ractoc.mytasksbackend.common.service.NoSuchEntryException;
import com.ractoc.mytasksbackend.tasks.db.TasksApplicationBuilder;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.Task;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.TaskImpl;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.TaskManager;
import org.assertj.core.api.WithAssertions;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Test the TaskService")
class TaskServiceTest extends SpeedmentDBTestCase implements WithAssertions {

    private TaskService service;

    @BeforeAll
    static void setupDatabase() throws Exception {
        createDatabase("my_tasks", 1234, "sql/create_test_schema.sql", "datasets/task/initial.xml");
    }

    @AfterAll
    static void tearDownAll() throws Exception {
        destroyDatabase();
    }

    @BeforeEach
    void setUp() {
        TaskManager categoryManager;
        categoryManager = new TasksApplicationBuilder()
                .withPassword("MyTasks")
                .withConnectionUrl(dbUrl)
                .withLogging(TasksApplicationBuilder.LogType.STREAM)
                .withLogging(TasksApplicationBuilder.LogType.REMOVE)
                .withLogging(TasksApplicationBuilder.LogType.PERSIST)
                .withLogging(TasksApplicationBuilder.LogType.UPDATE)
                .build().getOrThrow(TaskManager.class);
        service = new TaskService(categoryManager);
    }

    @Test
    void getAllTasks() {
        // Given

        // When
        Stream<Task> results = service.getAllTasks();

        // Then
        assertThat(results).isNotNull();
        List<Task> resultList = results.collect(Collectors.toList());
        assertThat(resultList.size()).isEqualTo(2);
        Task result = resultList.get(0);
        assertThat(result.getId()).isEqualTo("e00f7211-1982-40c1-ac96-b04c3a44adde");
        assertThat(result.getName()).isEqualTo("create test");
        assertThat(result.getDescription().isPresent()).isFalse();
        result = resultList.get(1);
        assertThat(result.getId()).isEqualTo("f5d58333-0e06-4824-94f6-eb99995ea5dd");
        assertThat(result.getName()).isEqualTo("more tests");
        assertThat(result.getDescription().isPresent()).isTrue();
        assertThat(result.getDescription().get()).isEqualTo("some nice text");
    }

    @Test
    void getTask() {
        // Given

        // When
        Task result = service.getTask("e00f7211-1982-40c1-ac96-b04c3a44adde");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("e00f7211-1982-40c1-ac96-b04c3a44adde");
        assertThat(result.getName()).isEqualTo("create test");
        assertThat(result.getDescription().isPresent()).isFalse();
    }

    @Test
    void getTaskNotFound() {
        // Given

        // When
        assertThrows(NoSuchEntryException.class, () -> service.getTask("Not-Found"));

        // Then
    }

    @Test
    void saveTask() throws Exception {
        // Given
        Task task = createTask("save new task");

        // When
        Task result = service.saveTask(task);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isNotIn("e00f7211-1982-40c1-ac96-b04c3a44adde", "f5d58333-0e06-4824-94f6-eb99995ea5dd");

        compareTaskTable("datasets/task/save.xml");
    }

    @Test
    void saveTaskDuplicateEntry() {
        // Given
        Task task = createTask("create test");

        // When
        assertThrows(DuplicateEntryException.class, ()->service.saveTask(task));

        // Then
    }

    @Test
    void updateTask() throws Exception {
        // Given
        Task task = service.getTask("e00f7211-1982-40c1-ac96-b04c3a44adde");
        task.setDescription("updated description");

        // When
        Task result = service.updateTask(task);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isEqualTo("e00f7211-1982-40c1-ac96-b04c3a44adde");
        assertThat(result.getDescription().isPresent()).isTrue();
        assertThat(result.getDescription().get()).isEqualTo("updated description");

        compareTaskTable("datasets/task/update.xml");
    }

    @Test
    void updateTaskDuplicateEntry() {
        // Given
        Task task = service.getTask("e00f7211-1982-40c1-ac96-b04c3a44adde");
        task.setName("more tests");
        task.setDescription("updated description");

        // When
        assertThrows(DuplicateEntryException.class, ()->service.updateTask(task));

        // Then
    }

    @Test
    void updateTaskNoSuchEntry() {
        // Given
        Task task = service.getTask("e00f7211-1982-40c1-ac96-b04c3a44adde");
        task.setId("Not-Found");
        task.setDescription("updated description");

        // When
        assertThrows(NoSuchEntryException.class, ()->service.updateTask(task));

        // Then
    }

    @Test
    void removeTask() throws Exception {
        // Given

        // When
        service.removeTask("e00f7211-1982-40c1-ac96-b04c3a44adde");

        // Then
        compareTaskTable("datasets/task/remove.xml");
    }

    @Test
    void removeTaskNoSuchEntry() {
        // Given

        // When
        assertThrows(NoSuchEntryException.class, ()->service.removeTask("Not-Found"));

        // Then
    }

    private Task createTask(String taskName) {
        Task task = new TaskImpl();
        task.setName(taskName);
        task.setStatus("NEW");
        task.setCreationDate(new Timestamp(System.currentTimeMillis()));
        task.setCreatedBy("test case");
        return task;
    }

    private void compareTaskTable(String dataset) throws Exception {
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("task");

        IDataSet expectedDataSet = getDataSet(dataset);
        ITable expectedTable = expectedDataSet.getTable("task");

        ITable filteredTable = DefaultColumnFilter.includedColumnsTable(actualTable,
                expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(new SortedTable(expectedTable),
                new SortedTable(filteredTable, expectedTable.getTableMetaData()));
    }
}
