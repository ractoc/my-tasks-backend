package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.service.NoSuchEntryException;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.Task;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class TaskService {

    private final TaskManager taskManager;

    @Autowired
    public TaskService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public Stream<Task> getAllTasks() {
        return taskManager.stream();
    }

    public Task getTask(String uuid) {
        return taskManager.stream().filter(Task.ID.equal(uuid)).findAny().orElseThrow(() -> new NoSuchEntryException("Task", uuid));
    }
}
