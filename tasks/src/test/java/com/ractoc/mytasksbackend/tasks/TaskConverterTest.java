package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.model.ListModel;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.Task;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.TaskImpl;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

@DisplayName("Test the TaskHandler")
@ExtendWith(MockitoExtension.class)
class TaskConverterTest implements WithAssertions {

    @Test
    void convertTaskToListModel() {
        // Given

        // When
        ListModel result = TaskConverter.convertTaskToListModel(createDbTask("taskId", "taskName", null));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("taskId");
        assertThat(result.getName()).isEqualTo("taskName");
    }

    @Test
    void convertToTaskModel() {
        // Given

        // When
        TaskModel result = TaskConverter.convertToTaskModel(createDbTask("taskId", "taskName", "task description"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("taskId");
        assertThat(result.getTitle()).isEqualTo("taskName");
        assertThat(result.getDescription()).isEqualTo("task description");
        assertThat(result.getStatus()).isEqualTo("TEST");
    }

    @Test
    void convertToTaskModelNoDescription() {
        // Given

        // When
        TaskModel result = TaskConverter.convertToTaskModel(createDbTask("taskId", "taskName", null));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("taskId");
        assertThat(result.getTitle()).isEqualTo("taskName");
        assertThat(result.getDescription()).isNull();
        assertThat(result.getStatus()).isEqualTo("TEST");
    }

    @Test
    void convertToTask() {
        // Given

        // When
        Task result = TaskConverter.convertToTask(new TaskModel("taskId", "taskName", "task description", "TEST"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("taskId");
        assertThat(result.getName()).isEqualTo("taskName");
        assertThat(result.getDescription()).isPresent();
        assertThat(result.getDescription().get()).isEqualTo("task description");
        assertThat(result.getStatus()).isEqualTo("TEST");
    }

    @Test
    void convertToTaskNoDescription() {
        // Given

        // When
        Task result = TaskConverter.convertToTask(new TaskModel("taskId", "taskName", null, "TEST"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("taskId");
        assertThat(result.getName()).isEqualTo("taskName");
        assertThat(result.getDescription()).isNotPresent();
        assertThat(result.getStatus()).isEqualTo("TEST");
    }

    private Task createDbTask(String id, String name, String description) {
        Task task = new TaskImpl();
        task.setId(id);
        task.setName(name);
        task.setDescription(description);
        task.setCreatedBy("TEST_USER");
        task.setCreationDate(new Timestamp(System.currentTimeMillis()));
        task.setStatus("TEST");
        return task;
    }

}
