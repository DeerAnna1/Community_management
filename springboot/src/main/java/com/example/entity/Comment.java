package com.example.entity;

import java.io.Serializable;

/* 社团活动评论表*/
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    /** ID */
    private Integer id;
    private Integer userId;
    private Integer ActivityId;
    private String time;
    private String content;

    private String userName;
    private String userAvatar;
    private String ActivityName;

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

    public Integer getActivityId() {
        return ActivityId;
    }

    public void setActivityId(Integer activityId) {
        ActivityId = activityId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }
}
