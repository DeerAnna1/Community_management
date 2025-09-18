package com.example.common.enums;

//结果枚举
public enum ResultCodeEnum {
    SUCCESS("200", "成功"),

    PARAM_ERROR("400", "参数异常"),
    TOKEN_INVALID_ERROR("401", "无效的token"),
    TOKEN_CHECK_ERROR("401", "token验证失败，请重新登录"),
    PARAM_LOST_ERROR("4001", "请选择身份"),

    SYSTEM_ERROR("500", "系统异常"),
    USER_EXIST_ERROR("5001", "账号已存在"),
    USER_NOT_LOGIN("5002", "用户未登录"),
    USER_ACCOUNT_ERROR("5003", "账号或密码错误"),
    USER_NOT_EXIST_ERROR("5004", "用户不存在"),
    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),
    LEADER_ALREADY_ERROR("5006", "该用户已经是社长了"),
    APPLY_ALREADY_ERROR("5007", "您已经申请过该社团了，请勿重复申请！"),
    APPLY_NOT_ALREADY_ERROR("5008", "您还未加入该社团，请先提交入团申请！"),
    PARTICIPATION_ALREADY_ERROR("5009", "您已经参与该社团活动了，请勿重复参与！"),
    PARTICIPATION_TIME_ERROR("5010", "您未在活动时间内，请在报名截止前参与！"),
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
