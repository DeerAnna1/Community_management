package com.example.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/* 社团活动信息表*/
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    /*社团活动ID */
    private Integer id;
    /*社团活动图标*/
    private String img;
    /*社团活动名*/
    private String name;
    /*社团活动信息*/
    private String description;
    /*社团ID*/
    private Integer departmentId;
    /*开始时间*/
    private String time;
    /*结束时间*/
    private String timeEnd;
    /*社团名称*/
    private String departmentName;

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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

}