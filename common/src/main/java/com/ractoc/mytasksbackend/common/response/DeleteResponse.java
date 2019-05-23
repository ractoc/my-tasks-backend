package com.ractoc.mytasksbackend.common.response;

import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@ApiModel(value="DeleteResponse Model", description="Contains the delete response. Used by all types when a delete has been performed.")
public class DeleteResponse extends BaseResponse {
    public DeleteResponse(HttpStatus httpStatus) {
        setResponseCode(httpStatus.value());
    }

    public DeleteResponse(HttpStatus httpStatus, String message) {
        setResponseCode(httpStatus.value());
        setMessage(message);
    }
}
