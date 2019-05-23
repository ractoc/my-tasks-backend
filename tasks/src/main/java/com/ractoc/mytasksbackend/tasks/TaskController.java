package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.response.ListResponse;
import com.ractoc.mytasksbackend.common.service.DuplicateEntryException;
import com.ractoc.mytasksbackend.common.service.NoSuchEntryException;
import com.ractoc.mytasksbackend.common.service.ServiceException;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.ractoc.mytasksbackend.common.BaseConstants.PATTERN_UUID;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Api(tags = {"Task Resource"}, value = "/task", produces = "application/json")
@SwaggerDefinition(tags = {
        @Tag(name = "Task Resource", description = "Main entry point for the Task micro-service. " +
                "Handles all related actions on the tasks. Aside from the HTTP return codes on the requests, " +
                "the response body also contains a HTTP status code which gives additional information.")
})
@RequestMapping("/task")
@Validated
public class TaskController {

    private final TaskHandler taskHandler;

    @Autowired
    public TaskController(TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    @ApiOperation(value = "Get list of tasks for use in drop-down menus", response = ListResponse.class, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieval successfully processed. This does not always mean tasks were found.", response = ListResponse.class)
    })
    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<ListResponse> getTaskList() {
        return new ResponseEntity<>(
                new ListResponse(OK, taskHandler.getRaceList()), OK);
    }

    @ApiOperation(value = "Get a specific task based on the supplied uuid", response = TaskResponse.class, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieval successfully processed.", response = TaskResponse.class),
            @ApiResponse(code = 400, message = "Requested task was not found.", response = TaskResponse.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = "{uuid}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<TaskResponse> getTaskById(@Pattern(regexp = PATTERN_UUID) @PathVariable String uuid) {
        try {
            return new ResponseEntity<>(new TaskResponse(OK, taskHandler.getTaskById(uuid)), OK);
        } catch (NoSuchEntryException e) {
            return new ResponseEntity<>(new TaskResponse(NOT_FOUND, e.getMessage()), BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Create a new task", response = TaskResponse.class, consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The task was successfully created", response = TaskResponse.class),
            @ApiResponse(code = 400, message = "A task with this title already exists", response = TaskResponse.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<TaskResponse> createTask(@Valid @NotNull @RequestBody TaskModel task) {
        try {
            return new ResponseEntity<>(new TaskResponse(CREATED, taskHandler.saveTask(task)), OK);
        } catch (DuplicateEntryException e) {
            return new ResponseEntity<>(new TaskResponse(CONFLICT, e.getMessage()), BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

}
