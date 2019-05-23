package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.model.ListModel;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.Task;

public class TaskConverter {

    private TaskConverter(){}

    public static ListModel taskToListModel(Task task) {
        return new ListModel(task.getId(), task.getName());
    }

    public static TaskModel convertToTaskModel(Task task) {
        return new TaskModel(task.getId(), task.getName(), task.getDescription().orElse(null), task.getStatus());
    }
}
