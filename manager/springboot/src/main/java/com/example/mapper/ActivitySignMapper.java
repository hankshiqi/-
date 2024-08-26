package com.example.mapper;

import com.example.entity.Activity;
import com.example.entity.ActivitySign;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ActivitySignMapper {
    @Insert("insert into activity_sign(user_id, act_id, time) values(#{userId}, #{actId}, #{time})")
    void add(ActivitySign activitySign);
    @Select("select * from activity_sign where act_id = #{actId} and user_id = #{userId}")
    ActivitySign getByActIdAndUserId(Integer actId, Integer userId);
    @Delete("delete from activity_sign where id = #{id}")
    void deleteById(Integer id);

    List<ActivitySign> selectAll(ActivitySign activitySign);
    @Delete("delete from activity_sign where act_id = #{actId} and user_id = #{userId}")
    void cancel(ActivitySign activitySign);
}
