package com.example.common.enums;

public enum ApplyEnum {
    PROCESS_LEADER_APPLYING("审核中"),
    PROCESS_LEADER_FINISH("审核完成"),

    STATUS_APPLY("待审核"),
    STATUS_APPLY_OK("审核通过"),
    STATUS_APPLY_NOT("审核不通过"),
    ;
    public String status;

    ApplyEnum(String status) {
        this.status = status;
    }
}
