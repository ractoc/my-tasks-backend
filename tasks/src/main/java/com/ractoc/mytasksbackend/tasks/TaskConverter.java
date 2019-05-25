package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.model.ListModel;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.Task;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.TaskImpl;

import java.sql.Timestamp;

class TaskConverter {

    private TaskConverter(){}

    static ListModel taskToListModel(Task task) {
        return new ListModel(task.getId(), task.getName());
    }

    static TaskModel convertToTaskModel(Task task) {
        return new TaskModel(task.getId(), task.getName(), task.getDescription().orElse(null), task.getStatus());
    }

    static Task convertToTask(TaskModel taskModel) {
        Task task = new TaskImpl();
        task.setId(taskModel.getId());
        task.setName(taskModel.getTitle());
        task.setDescription(taskModel.getDescription());
        task.setStatus(taskModel.getStatus());
        task.setCreatedBy("mytasks");
        task.setCreationDate(new Timestamp(System.currentTimeMillis()));
        return task;
    }
}
