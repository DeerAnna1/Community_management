package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ApplyEnum;
import com.example.common.enums.LevelEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.ApplyMapper;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/* 申请审核信息表业务处理*/
@Service
public class ApplyService {

    @Resource
    private ApplyMapper applyMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private DepartmentMapper departmentMapper;

    /* 新增*/
    public void add(Apply apply) {
        // 先去查询一下该学生有没有申请过该社团（避免重复申请）
        List<Apply> list = applyMapper.selectByStatus(apply.getUserId(), apply.getDepartmentId());
        if (CollectionUtil.isNotEmpty(list)) {//该学生已申请过该社团
            throw new CustomException(ResultCodeEnum.APPLY_ALREADY_ERROR);
        }
        //申请的默认状态
        apply.setProcess(ApplyEnum.PROCESS_LEADER_APPLYING.status);
        apply.setStatus(ApplyEnum.STATUS_APPLY.status);
        applyMapper.insert(apply);
    }

    /* 删除*/
    public void deleteById(Integer id) {
        applyMapper.deleteById(id);
    }

    /* 批量删除*/
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            applyMapper.deleteById(id);
        }
    }

    /* 修改*/
    public void updateById(Apply apply) {
        applyMapper.updateById(apply);
    }

    /* 根据ID查询*/
    public Apply selectById(Integer id) {
        return applyMapper.selectById(id);
    }

    /* 查询所有*/
    public List<Apply> selectAll(Apply apply) {
        return applyMapper.selectAll(apply);
    }

    /* 分页查询*/
    public PageInfo<Apply> selectPage(Apply apply, Integer pageNum, Integer pageSize) {
        //判断是哪个社团的审批
        extracted(apply);
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyMapper.selectAll(apply);
        return PageInfo.of(list);
    }

    /* 分页查询成员申请信息*/
    public PageInfo<Apply> selectMember(Apply apply, Integer pageNum, Integer pageSize) {
        extracted(apply);
        apply.setStatus(ApplyEnum.STATUS_APPLY_OK.status);
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyMapper.selectAll(apply);
        return PageInfo.of(list);
    }

    private void extracted(Apply apply) {
        //判断是哪个社团的审批
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            User user = userMapper.selectById(currentUser.getId());
            if (LevelEnum.LEADER.level.equals(user.getLevel())) {//只查询当前社长社团的审批学生ID
                Department department = departmentMapper.selectByUserId(user.getId());
                if (ObjectUtil.isNotEmpty(department)) {//社团不为空
                    apply.setDepartmentId(department.getId());
                }
            }
        }
    }

    public List<Apply> selectMyApply(Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        apply.setUserId(currentUser.getId());
        return applyMapper.selectAll(apply);
    }
}