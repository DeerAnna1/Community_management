package com.example.mapper;

import com.example.entity.Apply;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/* 操作apply相关数据接口*/
public interface ApplyMapper {

    /* 新增*/
    int insert(Apply apply);

    /* 删除*/
    int deleteById(Integer id);

    /* 修改*/
    int updateById(Apply apply);

    /* 根据ID查询*/
    Apply selectById(Integer id);

    /* 查询所有*/
    List<Apply> selectAll(Apply apply);

    @Select("select * from apply where user_id = #{userId} and department_id = #{departmentId} and (status = '待审核' or status = '审核通过')")
    List<Apply> selectByStatus(@Param("userId") Integer userId, @Param("departmentId") Integer departmentId);

    @Select("select * from apply where user_id = #{userId} and department_id = #{departmentId} and status = '审核通过'")
    List<Apply> selectForStatus(@Param("userId") Integer userId, @Param("departmentId") Integer departmentId);

}