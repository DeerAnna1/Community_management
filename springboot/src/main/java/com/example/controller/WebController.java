package com.example.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Activity;
import com.example.entity.Comment;
import com.example.entity.Department;
import com.example.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*基础前端接口*/
@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    private CommentService commentService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private ActivityService activityService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }

    /*登录*/
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            account = adminService.login(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            account = userService.login(account);
        }
        return Result.success(account);
    }

    /* 注册*/
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.register(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.register(account);
        }
        return Result.success();
    }

    /* 修改密码*/
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

//    统计总数
    @GetMapping("/count")
    public Result getCount() {
        List<Comment> comments = commentService.selectAll(null);
        List<Department> departments = departmentService.selectAll(null);
        List<Activity> activities = activityService.selectAll(null);
        Dict dict = Dict.create().set("commentCount", comments.size())
                .set("departmentCount",departments.size())
                .set("activityCount",activities.size());
        return Result.success(dict);
    }

    // 饼图数据
    @GetMapping("/pieData")
    public Result pieData() {
        List<Dict> list = new ArrayList<>();
        List<Activity> activityList = activityService.selectAll(null);
        List<String> typeList = activityList.stream()  //按照社团名称分组，获取社团名称列表
                .map(Activity::getDepartmentName).distinct().collect(Collectors.toList());
        for (String type : typeList) {  //遍历社团名称列表，获取每个社团的数量
            long count = activityList.stream()
                    .filter(activity -> activity.getDepartmentName().equals(type)).count();
            Dict dict = Dict.create().set("name", type).set("value", count);
            list.add(dict);
        }
        return Result.success(list);
    }
}
