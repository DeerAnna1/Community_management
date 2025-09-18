package com.example.controller;

import com.example.common.Result;
import com.example.entity.Participation;
import com.example.service.ActivityService;
import com.example.service.ParticipationService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/*参与的社团活动表前端操作接口*/
@RestController  //用于标识一个类是一个控制器（Controller），并且该控制器中的方法返回的数据不是视图页面，
// 而是直接作为响应内容发送到客户端，通常是以 JSON 格式返回数据。
@RequestMapping("/participation")
public class ParticipationController {

    @Resource
    private ParticipationService participationService;

    /*新增*/
    @PostMapping("/add")  //@RequestBody适用于处理json格式的数据
    public Result add(@RequestBody Participation participation) {
        participationService.add(participation);
        return Result.success();
    }

//     删除
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        participationService.deleteById(id);
        return Result.success();
    }


     /* 批量删除*/
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        participationService.deleteBatch(ids);
        return Result.success();
    }

    /* 查询所有*/
    @GetMapping("/selectAll")
    public Result selectAll(Participation participation ) {
        List<Participation> list = participationService.selectAll(participation);
        return Result.success(list);
    }

    //查询我加入的社团
    @GetMapping("/selectMyParticipation")
    public Result selectMyParticipation(Participation participation) {
        List<Participation> list = participationService.selectMyParticipation(participation);
        return Result.success(list);
    }

     /* 分页查询*/
    @GetMapping("/selectPage")  //@RequestParam将这些参数映射到方法的参数中。
    public Result selectPage(Participation participation,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Participation> page = participationService.selectPage(participation, pageNum, pageSize);
        return Result.success(page);
    }
}