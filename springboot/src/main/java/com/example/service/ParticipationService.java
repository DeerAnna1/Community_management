package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.*;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/*参与活动信息表业务处理*/
@Service
public class ParticipationService {

    @Resource
    private ParticipationMapper participationMapper;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private ApplyMapper applyMapper;

    /* 新增*/
    public void add(Participation participation) {
        List<Apply> list1 = applyMapper.selectForStatus(participation.getUserId(), participation.getDepartmentId());
        if (CollectionUtil.isEmpty(list1)) {//该学生还未申请该社团
            throw new CustomException(ResultCodeEnum.APPLY_NOT_ALREADY_ERROR);
        }
        List<Participation> list = participationMapper.selectByData(
                participation.getUserId(), participation.getDepartmentId(),participation.getActivityId());
        if (CollectionUtil.isNotEmpty(list)) {//该学生已参与该活动
            throw new CustomException(ResultCodeEnum.PARTICIPATION_ALREADY_ERROR);
        }
        //获取当前时间字符串格式
        participation.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        //比较时间
        Activity activity = activityMapper.selectById(participation.getActivityId());
        Integer compare = participation.getTime().compareTo(activity.getTimeEnd());
        if (compare > 0) {
            throw new CustomException(ResultCodeEnum.PARTICIPATION_TIME_ERROR);
        }
        participationMapper.insert(participation);
    }

 /* 删除*/
    public void deleteById(Integer id) {
        participationMapper.deleteById(id);
    }

    /*批量删除*/
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            participationMapper.deleteById(id);
        }
    }

    /*查询所有*/
    public List<Participation> selectAll(Participation participation) {
        return participationMapper.selectAll(participation);
    }

    /*分页查询*/
    public PageInfo<Participation> selectPage(Participation participation, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        //是否为学生身份
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            Department department = departmentMapper.selectByUserId(currentUser.getId());
            if (ObjectUtil.isNotEmpty(department)) {//是社长
                participation.setDepartmentId(department.getId());//把当前的社团内容给他
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Participation> list = participationMapper.selectAll(participation);
        return PageInfo.of(list);
    }

    public List<Participation> selectMyParticipation(Participation participation) {
        Account currentUser = TokenUtils.getCurrentUser();
        participation.setUserId(currentUser.getId());
        return participationMapper.selectAll(participation);
    }
}