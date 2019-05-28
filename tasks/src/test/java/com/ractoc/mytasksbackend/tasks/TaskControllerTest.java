package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.model.ListModel;
import com.ractoc.mytasksbackend.common.response.DeleteResponse;
import com.ractoc.mytasksbackend.common.response.ListResponse;
import com.ractoc.mytasksbackend.common.service.DuplicateEntryException;
import com.ractoc.mytasksbackend.common.service.NoSuchEntryException;
import com.ractoc.mytasksbackend.common.service.ServiceException;
import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;

@DisplayName("Test the TaskHandler")
@ExtendWith(MockitoExtension.class)
class TaskControllerTest implements WithAssertions, WithBDDMockito {

    @Mock
    private TaskHandler mockedTaskHandler;

    @InjectMocks
    private TaskController controller;

    @Test
    void getTaskList() {
        // Given
        when(mockedTaskHandler.getTaskList()).thenReturn(createTaskList());

        // When
        ResponseEntity<ListResponse> result = controller.getTaskList();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(OK.value());
        assertThat(result.getBody().getItems()).contains(createListItem("task-1"), createListItem("task-2"));
    }

    @Test
    void getTaskListNoItems() {
        // Given
        when(mockedTaskHandler.getTaskList()).thenReturn(new ArrayList<>());

        // When
        ResponseEntity<ListResponse> result = controller.getTaskList();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(OK.value());
        assertThat(result.getBody().getItems()).isEmpty();
    }

    @Test
    void getTaskById() {
        // Given
        when(mockedTaskHandler.getTaskById("taskId")).thenReturn(createTask("taskId"));

        // When
        ResponseEntity<TaskResponse> result = controller.getTaskById("taskId");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(OK.value());
        assertThat(result.getBody().getTask()).isEqualTo(createTask("taskId"));
    }

    @Test
    void getTaskByIdNoSuchEntity() {
        // Given
        when(mockedTaskHandler.getTaskById("taskId")).thenThrow(new NoSuchEntryException("Task", "test"));

        // When
        ResponseEntity<TaskResponse> result = controller.getTaskById("taskId");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(NOT_FOUND.value());
        assertThat(result.getBody().getTask()).isNull();
        assertThat(result.getBody().getMessage()).isNotNull();
    }

    @Test
    void getTaskByIdServiceException() {
        // Given
        when(mockedTaskHandler.getTaskById("taskId")).thenThrow(new ServiceException("test exception"));

        // When
        ResponseEntity<TaskResponse> result = controller.getTaskById("taskId");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void createTask() {
        // Given
        when(mockedTaskHandler.saveTask(any(TaskModel.class))).thenReturn(createTask("taskId"));

        // When
        ResponseEntity<TaskResponse> result = controller.createTask(new TaskModel(null, "task name", "creator", "NEW"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(CREATED.value());
        assertThat(result.getBody().getTask()).isEqualTo(createTask("taskId"));
    }

    @Test
    void createTaskDuplicateEntryException() {
        // Given
        when(mockedTaskHandler.saveTask(any(TaskModel.class))).thenThrow(new DuplicateEntryException("test exception"));

        // When
        ResponseEntity<TaskResponse> result = controller.createTask(new TaskModel(null, "task name", "creator", "NEW"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(CONFLICT.value());
        assertThat(result.getBody().getTask()).isNull();
        assertThat(result.getBody().getMessage()).isNotNull();
    }

    @Test
    void createTaskServiceException() {
        // Given
        when(mockedTaskHandler.saveTask(any(TaskModel.class))).thenThrow(new ServiceException("test exception"));

        // When
        ResponseEntity<TaskResponse> result = controller.createTask(new TaskModel(null, "task name", "creator", "NEW"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void updateTask() {
        // Given
        when(mockedTaskHandler.updateTask(any(TaskModel.class))).thenReturn(createTask("taskName"));

        // When
        ResponseEntity<TaskResponse> result = controller.updateTask(createTask("taskId"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(MOVED_PERMANENTLY.value());
        assertThat(result.getBody().getTask()).isEqualTo(createTask("taskName"));
    }

    @Test
    void updateTaskNoSuchEntryException() {
        // Given
        when(mockedTaskHandler.updateTask(any(TaskModel.class))).thenThrow(new NoSuchEntryException("test", "exception"));

        // When
        ResponseEntity<TaskResponse> result = controller.updateTask(createTask("taskId"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(NOT_FOUND.value());
        assertThat(result.getBody().getTask()).isNull();
        assertThat(result.getBody().getMessage()).isNotNull();
    }

    @Test
    void updateTaskDuplicateEntryException() {
        // Given
        when(mockedTaskHandler.updateTask(any(TaskModel.class))).thenThrow(new DuplicateEntryException("test exception"));

        // When
        ResponseEntity<TaskResponse> result = controller.updateTask(createTask("taskId"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(CONFLICT.value());
        assertThat(result.getBody().getTask()).isNull();
        assertThat(result.getBody().getMessage()).isNotNull();
    }

    @Test
    void updateTaskServiceException() {
        // Given
        when(mockedTaskHandler.updateTask(any(TaskModel.class))).thenThrow(new ServiceException("test exception"));

        // When
        ResponseEntity<TaskResponse> result = controller.updateTask(createTask("taskId"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void removeTask() {
        // Given

        // When
        ResponseEntity<DeleteResponse> result = controller.removeTask("taskId");

        // Then
        verify(mockedTaskHandler).removeTask("taskId");
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(GONE.value());
    }

    @Test
    void removeTaskNoSuchEntryException() {
        // Given
        doThrow(new NoSuchEntryException("test", "exception")).when(mockedTaskHandler).removeTask("taskId");

        // When
        ResponseEntity<DeleteResponse> result = controller.removeTask("taskId");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponseCode()).isEqualTo(NOT_FOUND.value());
        assertThat(result.getBody().getMessage()).isNotNull();
    }

    @Test
    void removeTaskServiceException() {
        // Given
        doThrow(new ServiceException("test exception")).when(mockedTaskHandler).removeTask("taskId");

        // When
        ResponseEntity<DeleteResponse> result = controller.removeTask("taskId");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    private List<ListModel> createTaskList() {
        return Stream.of(createListItem("task-1"), createListItem("task-2")).collect(Collectors.toList());
    }

    private ListModel createListItem(String name) {
        return new ListModel(name, name);
    }

    private TaskModel createTask(String taskId) {
        return new TaskModel(taskId, taskId, "creator", "NEW");
    }

}
