package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.service.DuplicateEntryException;
import com.ractoc.mytasksbackend.common.service.NoSuchEntryException;
import com.ractoc.mytasksbackend.common.service.ServiceException;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.Task;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.TaskManager;
import com.speedment.runtime.core.exception.SpeedmentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class TaskService {

    private final TaskManager taskManager;

    @Autowired
    public TaskService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    Stream<Task> getAllTasks() {
        return taskManager.stream();
    }

    Task getTask(String uuid) {
        return taskManager.stream().filter(Task.ID.equal(uuid)).findAny().orElseThrow(() -> new NoSuchEntryException("Task", uuid));
    }

    Task saveTask(Task task) {
        try {
            createIdForTask(task);
            Optional<Task> existingTask = taskManager.stream()
                    .filter(Task.NAME.equalIgnoreCase(task.getName())).findAny();
            if (existingTask.isPresent()) {
                throw new DuplicateEntryException("Task with title " + task.getName() + " already exists.");
            }
            return taskManager.persist(task);
        } catch (SpeedmentException e) {
            throw new ServiceException("Unable to save task " + task.getName(), e);
        }
    }

    Task updateTask(Task task) {
        try {
            // test of this is a valid task to update by retrieving it
            getTask(task.getId());
            Optional<Task> existingTask = taskManager.stream()
                    .filter(Task.NAME.equalIgnoreCase(task.getName()).and(Task.ID.equal(task.getId()).negate())).findAny();
            if (existingTask.isPresent()) {
                throw new DuplicateEntryException("Task with title " + task.getName() + " already exists.");
            }
            return taskManager.update(task);
        } catch (SpeedmentException e) {
            throw new ServiceException("Unable to save task " + task.getName(), e);
        }
    }

    void removeTask(String uuid) {
        taskManager.remove(getTask(uuid));
    }

    private void createIdForTask(Task task) {
        boolean idExists;
        String id;
        do {
            id = UUID.randomUUID().toString();
            Optional<Task> existingTask = taskManager.stream().filter(Task.ID.equal(id)).findAny();
            idExists = existingTask.isPresent();
        } while (idExists);
        task.setId(id);
    }
}
