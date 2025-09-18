package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.LevelEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Department;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/* 社团信息表业务处理*/
@Service
public class DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private UserMapper userMapper;

    /*新增*/
    public void add(Department department) {
        if (ObjectUtil.isNotEmpty(department.getUserId())){//社团信息表中是否有该社长
            //判断该社长是否已经承担别的社团的社长了
            Department dbDepartment = departmentMapper.selectByUserId(department.getUserId());
            if (ObjectUtil.isNotEmpty(dbDepartment)){
                throw new CustomException(ResultCodeEnum.LEADER_ALREADY_ERROR);
            }
        }
        department.setTime(DateUtil.format(new Date(),"yyy-MM-dd"));
        departmentMapper.insert(department);//更新
    }

    /* 删除*/
    public void deleteById(Integer id) {
        departmentMapper.deleteById(id);
    }

    /*批量删除*/
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            departmentMapper.deleteById(id);
        }
    }

    /* 修改*/
    public void updateById(Department department) {
        if (ObjectUtil.isNotEmpty(department.getUserId())){//社团信息表中是否有该社长
            //判断该社长是否已经承担别的社团的社长了
            Department dbDepartment = departmentMapper.selectByUserId(department.getUserId());
            if (ObjectUtil.isNotEmpty(dbDepartment)){
                if (!dbDepartment.getName().equals(department.getName())) {
                    throw new CustomException(ResultCodeEnum.LEADER_ALREADY_ERROR);
                }
            }
        }
        departmentMapper.updateById(department);//更新
        Department info = departmentMapper.selectByUserId(department.getUserId());
    }

    /* 根据ID查询*/
    public Department selectById(Integer id) {
        return departmentMapper.selectById(id);
    }

    /* 查询所有*/
    public List<Department> selectAll(Department department) {
        return departmentMapper.selectAll(department);
    }

    /*分页展示*/
    public PageInfo<Department> selectPage(Department department, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())){//角色是学生
            User user = userMapper.selectById(currentUser.getId());
            if (LevelEnum.LEADER.level.equals(user.getLevel())){//是社长身份
                department.setUserId(user.getId());
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Department> list = departmentMapper.selectAll(department);
        return PageInfo.of(list);
    }

}