package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Activity;
import com.example.entity.ActivitySign;
import com.example.entity.Category;
import com.example.exception.CustomException;
import com.example.mapper.ActivitySignMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivitySignService {
    @Resource
    private ActivitySignMapper activitySignMapper;



    public void add(ActivitySign activitySign) {
        if(this.checkSign(activitySign.getActId(), activitySign.getUserId())){
            throw new CustomException(ResultCodeEnum.SIGN_AGAIN_ERROR);
        }
        activitySign.setTime(DateUtil.now());
        activitySignMapper.add(activitySign);
    }
    //检查是否已经进行了报名
    public boolean checkSign(Integer actId, Integer userId) {
        ActivitySign activitySign = activitySignMapper.getByActIdAndUserId(actId, userId);
        return activitySign != null;
    }

    public void deleteById(Integer id) {
        activitySignMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activitySignMapper.deleteById(id);
        }
    }
    public List<ActivitySign> selectAll(ActivitySign activitySign) {
        return activitySignMapper.selectAll(activitySign);
    }

    /**
     * 分页查询
     */
    public PageInfo<ActivitySign> selectPage(ActivitySign activitySign, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ActivitySign> list = activitySignMapper.selectAll(activitySign);
        return PageInfo.of(list);
    }

    public void cancel(ActivitySign activitySign) {
        activitySignMapper.cancel(activitySign);
    }
}
