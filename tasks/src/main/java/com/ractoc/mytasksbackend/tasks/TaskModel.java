package com.ractoc.mytasksbackend.tasks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ractoc.mytasksbackend.common.BaseConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
@ApiModel(value="Task Model", description="Contains all task fields.")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskModel {
    @ApiModelProperty(value = "The id of the task. This is a UUID.")
    @Pattern(regexp = BaseConstants.PATTERN_UUID)
    private final String id;
    @ApiModelProperty(value = "The name of the task.")
    @NotNull
    private final String name;
    @ApiModelProperty(value = "The description of the task.")
    private final String description;
    @ApiModelProperty(value = "containst the status of the task.")
    private final String status;
}
