package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/* 用户业务处理*/
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private DepartmentMapper departmentMapper;

    /*新增*/
    public void add(User user) {
        User User = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(User)) {//用户名已存在
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);//抛出异常
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {//管理员在新增学生时，给一个默认密码
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(user.getName())) {//存入用户名
            user.setName(user.getUsername());
        }
        user.setRole(RoleEnum.USER.name());//存入身份
        userMapper.insert(user);//将信息插入数据库
    }

    /*删除*/
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    /* 批量删除*/
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.deleteById(id);
        }
    }

    /*修改*/
    public void updateById(User user) {
        userMapper.updateById(user);
    }

    /*根据ID查询*/
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    /* 查询所有*/
    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    /* 分页查询 */
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        for (User User : list){
            if (ObjectUtil.isNotEmpty(User.getLevel()) &&
                    LevelEnum.LEADER.level.equals(User.getLevel())){
                Department department = departmentMapper.selectByUserId(User.getId());
                if (ObjectUtil.isNotEmpty(department)){
                    User.setDepartmentName(department.getName());
                }
            }
        }
        return PageInfo.of(list);
    }

    /*登录*/
    public Account login(Account account) {
        Account User = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(User)) {//该用户信息不存在数据库中
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);//抛出异常
        }
        if (!account.getPassword().equals(User.getPassword())) {//用户输入的密码与数据库中的密码不相等
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);//抛出异常
        }
        // 将用户输入的信息生成token
        String tokenData = User.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, User.getPassword());
        User.setToken(token);
        return User;
    }

    /*注册*/
    public void register(Account account) {
        User user = new User();
        BeanUtils.copyProperties(account, user);
        add(user);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }

    public List<User> getAllLeaders(User user) {
        user.setLevel(LevelEnum.LEADER.level);
        return userMapper.selectAll(user);
    }
}