package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.ActivityMapper;
import com.example.mapper.DepartmentMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/* 社团活动表业务处理*/
@Service
public class ActivityService {

    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private DepartmentMapper departmentMapper;

    /* 新增*/
    public void add(Activity activity) {
        //获取当前时间
        activity.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        // 获取截止时间
        activity.setTimeEnd(activity.getTimeEnd());

        // 获取社团ID
        Account currentUser = TokenUtils.getCurrentUser();
        Department department = departmentMapper.selectByUserId(currentUser.getId());
        activity.setDepartmentId(department.getId());

        activityMapper.insert(activity);
    }

    /* 删除*/
    public void deleteById(Integer id) {
        activityMapper.deleteById(id);
    }

    /* 批量删除*/
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activityMapper.deleteById(id);
        }
    }

    /*修改*/
    public void updateById(Activity activity) {
        activityMapper.updateById(activity);
    }

    /* 根据ID查询*/
    public Activity selectById(Integer id) {
        Activity activity = activityMapper.selectById(id);
        Department department = departmentMapper.selectById(activity.getDepartmentId());
        if (ObjectUtil.isNotEmpty(department)) {
            activity.setDepartmentName(department.getName());
        }
        return activity;
    }

    /* 查询所有*/
    public List<Activity> selectAll(Activity activity) {
        List<Activity> activities = activityMapper.selectAll(activity);
        for (Activity dbActivity : activities) {
            dbActivity.setDescription(dbActivity.getDescription());
        }
        return activities;
    }

    public List<Activity> selectAll2(Activity activity) {
        Account currentUser = TokenUtils.getCurrentUser();
        //是否为学生身份
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            Department department = departmentMapper.selectByUserId(currentUser.getId());
            if (ObjectUtil.isNotEmpty(department)) {//是社长
                activity.setDepartmentId(department.getId());//把当前的社团内容给他
            }
        }
        List<Activity> activities = activityMapper.selectAll(activity);
        for (Activity dbActivity : activities) {
            dbActivity.setDescription(dbActivity.getDescription());
        }
        return activities;
    }

    public PageInfo<Activity> selectPage2(Activity activity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = selectAll(activity);
        return PageInfo.of(list);
    }

    /* 分页查询*/
    public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        //是否为学生
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            Department department = departmentMapper.selectByUserId(currentUser.getId());
            if (ObjectUtil.isNotEmpty(department)) {//是社长
                activity.setDepartmentId(department.getId());//把当前的社团内容给他
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectAll(activity);
        return PageInfo.of(list);
    }

}