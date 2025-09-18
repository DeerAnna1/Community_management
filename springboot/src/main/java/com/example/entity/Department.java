package com.example.entity;

import java.io.Serializable;

/*社团信息表*/
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;
    /*社团ID */
    private Integer id;
    /*社团logo*/
    private String img;
    /*社团名*/
    private String name;
    /*社团信息*/
    private String description;
    /*社长ID*/
    private Integer userId;
    /*社长姓名*/
    private String userName;
    /*发布时间*/
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}