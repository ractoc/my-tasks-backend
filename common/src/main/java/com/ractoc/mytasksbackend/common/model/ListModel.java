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

    @EqualsAndHashCode.Include
    private final String id;
    private final String name;

}
