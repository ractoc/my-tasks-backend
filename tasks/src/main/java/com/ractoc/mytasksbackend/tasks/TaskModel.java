package com.ractoc.mytasksbackend.tasks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ractoc.mytasksbackend.common.BaseConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="Task Model", description="Contains all task fields.")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskModel {
    @ApiModelProperty(value = "The id of the task. This is a UUID.")
    @Pattern(regexp = BaseConstants.PATTERN_UUID)
    private String id;
    @ApiModelProperty(value = "The title of the task.")
    @NotNull
    private String title;
    @ApiModelProperty(value = "The description of the task.")
    private String description;
    @ApiModelProperty(value = "containst the status of the task.")
    private String status;
}
