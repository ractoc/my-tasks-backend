package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.model.ListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskHandler {

    private final TaskService taskService;

    @Autowired
    public TaskHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    List<ListModel> getRaceList() {
        return taskService.getAllTasks().map(TaskConverter::taskToListModel).collect(Collectors.toList());
    }

    TaskModel getTaskById(String uuid) {
        return TaskConverter.convertToTaskModel(taskService.getTask(uuid));
    }

    TaskModel saveTask(TaskModel task) {
        return TaskConverter.convertToTaskModel(taskService.saveTask(TaskConverter.convertToTask(task)));
    }
}
