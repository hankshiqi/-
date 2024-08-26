package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.ModuleCategoryEnum;
import com.example.entity.*;
import com.example.mapper.ActivityMapper;
import com.example.mapper.CategoryMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 公告信息表业务处理
 **/
@Service
public class ActivityService {

    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ActivitySignService activitySignService;
    @Resource
    private LikesService likesService;
    @Resource
    private CollectsService collectsService;
    /**
     * 新增
     */
    public void add(Activity activity) {

        activityMapper.insert(activity);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        activityMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activityMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Activity activity) {
        activityMapper.updateById(activity);
    }

    /**
     * 根据ID查询
     */
    public Activity selectById(Integer actId) {
        Activity activity = activityMapper.selectById(actId);
        Account currentUser = TokenUtils.getCurrentUser();

        if(activity!=null) {
            this.checkActivity(activity, currentUser);
        }
        //获取当前用户是否点赞
        Boolean isLiked = likesService.checkLiked(actId, ModuleCategoryEnum.ACTIVITY.name, currentUser.getId()) != null;
        activity.setIsLiked(isLiked);
        //获取当前用户是否收藏
        Boolean isCollected = collectsService.checkCollected(actId, ModuleCategoryEnum.ACTIVITY.name, currentUser.getId()) != null;
        activity.setIsCollected(isCollected);
        //获取当前活动的点赞数和收藏数
        activity.setLikeCount(likesService.getLikesNum(actId, ModuleCategoryEnum.ACTIVITY.name));
        activity.setCollectCount(collectsService.getCollectsNum(actId, ModuleCategoryEnum.ACTIVITY.name));
        return activity;
    }

    /**
     * 查询所有
     */
    public List<Activity> selectAll(Activity activity) {
        return activityMapper.selectAll(activity);
    }

    /**
     * 分页查询
     */
    public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectAll(activity);
        Account currentUser = TokenUtils.getCurrentUser();
        for (Activity activity1 : list) {
            this.checkActivity(activity1, currentUser);
        }
            return PageInfo.of(list);
    }

    public List<Activity> selectTopActivity() {
        List<Activity> activities = activityMapper.selectAll(null);
        activities=activities.stream().sorted((a,b)->b.getReadCount().compareTo(a.getReadCount()))
                .limit(2)
                .collect(Collectors.toList());
        return activities;
    }

    //判断到期和是否已经报名
    public void checkActivity(Activity activity,Account account){
        if(DateUtil.parse(activity.getEnd()).before(DateUtil.date())){
            activity.setDue(true);
        }
        boolean signed = activitySignService.checkSign(activity.getId(), account.getId());
        activity.setSign(signed);
    }

    public void updateReadCount(Integer id) {
        Activity activity = activityMapper.selectById(id);
        activity.setReadCount(activity.getReadCount() + 1);
        activityMapper.updateById(activity);
    }

    public PageInfo<Activity> selectPageByUser(Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectPageByUser(currentUser.getId());
        for (Activity activity : list) {
            this.checkActivity(activity, currentUser);
        }
        return PageInfo.of(list);
    }

    public PageInfo<Activity> selectPageCollect(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Account currentUser = TokenUtils.getCurrentUser();
        List<Activity> list = activityMapper.selectPageCollect(currentUser.getId());
        return PageInfo.of(list);
    }
}