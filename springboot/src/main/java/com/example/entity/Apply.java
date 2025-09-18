package com.example.entity;

import java.io.Serializable;

/*申请审批信息表*/
public class Apply implements Serializable {
    private static final long serialVersionUID = 1L;
    /** ID */
    private Integer id;
    /*学生ID*/
    private Integer userId;
    /*社团ID*/
    private Integer departmentId;
    /*申请说明*/
    private String description;
    /*审核进度*/
    private String process;
    /*审核状态*/
    private String status;
    /*审核说明*/
    private String note;
    //以下两个变量是用来关联字段的
    /*学生姓名*/
    private String userName;
    /*社团名称*/
    private String departmentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}