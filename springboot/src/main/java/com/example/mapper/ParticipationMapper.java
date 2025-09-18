package com.example.mapper;

import com.example.entity.Participation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/* 操作participation相关数据接口*/
public interface ParticipationMapper {

    /* 新增*/
    int insert(Participation participation);

    /* 删除*/
    int deleteById(Integer id);

    /* 根据ID查询*/
    Participation selectById(Integer id);

    /*查询所有*/
    List<Participation> selectAll(Participation participation);

    @Select("select * from participation where userr_id = #{userId} and departmentt_id = #{departmentId} and activityy_id = #{activityId}")
    List<Participation> selectByData(@Param("userId") Integer userId, @Param("departmentId") Integer departmentId, @Param("activityId") Integer activityId);
}