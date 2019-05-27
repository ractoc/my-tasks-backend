package com.ractoc.mytasksbackend.common.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ListModel {

    private final String id;
    @EqualsAndHashCode.Exclude
    private final String name;

}
