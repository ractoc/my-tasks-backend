package com.ractoc.mytasksbackend.common.response;

import com.ractoc.mytasksbackend.common.model.ListModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "ListResponse Model", description = "Contains the response containing a list of items")
public class ListResponse extends BaseResponse {

    @ApiModelProperty(value = "The list of list items.")
    private List<ListModel> items = new ArrayList<>();

    public ListResponse(HttpStatus httpStatus, List<ListModel> listItems) {
        setResponseCode(httpStatus.value());
        this.items = listItems;
    }
}
