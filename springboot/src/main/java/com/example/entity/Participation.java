package com.example.entity;

import java.io.Serializable;

/* 参与活动信息表*/
public class Participation implements Serializable {
    private static final long serialVersionUID = 1L;
    /** ID */
    private Integer id;
    /*学生ID*/
    private Integer userId;
    /*社团ID*/
    private Integer departmentId;
    /*活动ID*/
    private Integer activityId;
    //报名时间
    private String time;
    /*学生姓名*/
    private String userName;
    /*社团名称*/
    private String departmentName;
    /*活动名称*/
    private String activityName;

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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}