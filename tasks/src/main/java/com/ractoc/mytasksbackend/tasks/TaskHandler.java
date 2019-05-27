package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.model.ListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

import static com.ractoc.mytasksbackend.tasks.TaskConverter.convertToTask;
import static com.ractoc.mytasksbackend.tasks.TaskConverter.convertToTaskModel;

@Service
@Validated
public class TaskHandler {

    private final TaskService taskService;

    @Autowired
    public TaskHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    List<ListModel> getTaskList() {
        return taskService.getAllTasks().map(TaskConverter::taskToListModel).collect(Collectors.toList());
    }

    TaskModel getTaskById(String uuid) {
        return convertToTaskModel(taskService.getTask(uuid));
    }

    TaskModel saveTask(TaskModel task) {
        return convertToTaskModel(taskService.saveTask(convertToTask(task)));
    }

    TaskModel updateTask(TaskModel task) {
        return convertToTaskModel((taskService.updateTask(convertToTask(task))));
    }

    void removeTask(String uuid) {
        taskService.removeTask(uuid);
    }
}
