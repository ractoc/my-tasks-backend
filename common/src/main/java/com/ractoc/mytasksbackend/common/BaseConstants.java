package com.ractoc.mytasksbackend.common;

public class BaseConstants {
    public static final String PATTERN_UUID = "[a-fA-F0-9]{8}(-[a-fA-F0-9]{4}){3}-[a-fA-F0-9]{12}";

    private BaseConstants() {
        // since this class only contains constants it is never meant to be instantiated.
    }
}
