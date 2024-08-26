package com.example.controller;

import com.example.common.Result;
import com.example.entity.Activity;
import com.example.entity.Blog;
import com.example.service.ActivityService;
import com.example.service.ActivitySignService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private ActivityService activityService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Activity activity) {
        activityService.add(activity);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        activityService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        activityService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Activity activity) {
        activityService.updateById(activity);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Activity activity = activityService.selectById(id);
        return Result.success(activity);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Activity activity ) {
        List<Activity> list = activityService.selectAll(activity);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Activity activity,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Activity> page = activityService.selectPage(activity, pageNum, pageSize);
        return Result.success(page);
    }

    //用户查询我的活动
    @GetMapping("/selectPageByUser")
    public Result selectPageByUser(
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Activity> page = activityService.selectPageByUser(pageNum, pageSize);
        return Result.success(page);
    }
    @GetMapping("/selectPageCollect")

    public Result selectPageCollect(
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize
                             ) {
        PageInfo<Activity> page = activityService.selectPageCollect(pageNum, pageSize);
        return Result.success(page);
    }
    @GetMapping("/selectTopActivity")
    public Result selectTopActivity() {
        List<Activity> list=activityService.selectTopActivity();
        return Result.success(list);
    }

    //更新阅读量
    @PutMapping("/updateReadCount/{id}")
    public Result updateReadCount(@PathVariable Integer id){
        activityService.updateReadCount(id);
        return Result.success();
    }

}