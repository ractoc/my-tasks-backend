package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.common.response.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "TaskResponse Model", description = "Contains the response containing a single task")
class TaskResponse extends BaseResponse {

    @ApiModelProperty(value = "The task.")
    private TaskModel task;

    TaskResponse(HttpStatus httpStatus, TaskModel task) {
        setResponseCode(httpStatus.value());
        this.task = task;
    }

    TaskResponse(HttpStatus httpStatus, String message) {
        setResponseCode(httpStatus.value());
        setMessage(message);
    }
}
