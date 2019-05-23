package com.ractoc.mytasksbackend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ApiModel(value="BaseResponse Model", description="Contains the basic response. All other response inherit this response.")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse {

    @ApiModelProperty(value = "The internal response code.")
    private int responseCode;
    @ApiModelProperty(value = "The message linked to the response code. This is to be used when something goes wrong.")
    private String message;
    @ApiModelProperty(value = "The detailed list of errors.")
    private List<Map<String, Object>> errors;

    public void addError(Map<String, Object> error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}
