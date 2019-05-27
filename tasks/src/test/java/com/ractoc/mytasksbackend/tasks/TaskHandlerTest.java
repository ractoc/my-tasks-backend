package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.model.ListModel;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.Task;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.TaskImpl;
import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

@DisplayName("Test the TaskHandler")
@ExtendWith(MockitoExtension.class)
class TaskHandlerTest implements WithAssertions, WithBDDMockito {

    @Mock
    private TaskService mockedTaskService;

    @InjectMocks
    private TaskHandler handler;

    @Test
    void getTaskList() {
        // Given
        when(mockedTaskService.getAllTasks()).thenReturn(createDbTaskList());

        // When
        List<ListModel> result = handler.getTaskList();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).containsExactlyInAnyOrder(new ListModel("task-1", "task-1"), new ListModel("task-2", "task-2"));
    }

    @Test
    void getTaskListNoEntries() {
        // Given
        when(mockedTaskService.getAllTasks()).thenReturn(Stream.empty());

        // When
        List<ListModel> result = handler.getTaskList();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void getTaskById() {
        // Given
        Task dbTask = new TaskImpl();
        dbTask.setId("taskId");
        dbTask.setName("taskName");
        dbTask.setCreatedBy("creator");
        dbTask.setCreationDate(new Timestamp(System.currentTimeMillis()));
        when(mockedTaskService.getTask("taskId")).thenReturn(dbTask);

        // When
        TaskModel result = handler.getTaskById("taskId");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("taskId");
        assertThat(result.getTitle()).isEqualTo("taskName");
    }

    @Test
    void saveTask() {
        // Given
        TaskModel taskModel = new TaskModel(null, "testTask", "testDesc", "NEW");
        Task dbTask = new TaskImpl();
        dbTask.setId("taskId");
        dbTask.setName("taskName");
        dbTask.setCreatedBy("creator");
        dbTask.setCreationDate(new Timestamp(System.currentTimeMillis()));
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        when(mockedTaskService.saveTask(taskCaptor.capture())).thenReturn(dbTask);

        // When
        TaskModel result = handler.saveTask(taskModel);

        // Then
        Task task = taskCaptor.getValue();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("taskId");
        assertThat(result.getTitle()).isEqualTo("taskName");
        assertThat(task).isNotNull();
        assertThat(task.getId()).isNull();
        assertThat(task.getName()).isEqualTo("testTask");
        assertThat(task.getStatus()).isEqualTo("NEW");
    }

    @Test
    void updateTask() {
        // Given
        TaskModel taskModel = new TaskModel("testId", "testTask", "testDesc", "NEW");
        Task dbTask = new TaskImpl();
        dbTask.setId("taskId");
        dbTask.setName("taskName");
        dbTask.setCreatedBy("creator");
        dbTask.setCreationDate(new Timestamp(System.currentTimeMillis()));
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        when(mockedTaskService.updateTask(taskCaptor.capture())).thenReturn(dbTask);

        // When
        TaskModel result = handler.updateTask(taskModel);

        // Then
        Task task = taskCaptor.getValue();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("taskId");
        assertThat(result.getTitle()).isEqualTo("taskName");
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo("testId");
        assertThat(task.getName()).isEqualTo("testTask");
        assertThat(task.getStatus()).isEqualTo("NEW");
    }

    @Test
    void removeTask() {
        // Given

        // When
        handler.removeTask("taskId");

        // Then
        verify(mockedTaskService).removeTask("taskId");
    }

    private Stream<Task> createDbTaskList() {
        return Stream.of(createDbTask("task-1"), createDbTask("task-2"));
    }

    private Task createDbTask(String name) {
        Task task = new TaskImpl();
        task.setId(name);
        task.setName(name);
        return task;
    }

}
