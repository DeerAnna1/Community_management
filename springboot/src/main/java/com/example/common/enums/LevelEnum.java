package com.example.common.enums;

//学生身份枚举
public enum LevelEnum {
    LEADER("社长"),
    STUDENT("学生"),
    ;
    public String level;

    LevelEnum(String level) {
        this.level = level;
    }
}
