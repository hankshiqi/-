package com.example.mapper;

import com.example.entity.Blog;
import org.apache.ibatis.annotations.Select;

import java.util.Arrays;
import java.util.List;

/**
 * 操作blog相关数据接口
*/
public interface BlogMapper {

    /**
      * 新增
    */
    int insert(Blog blog);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Blog blog);

    /**
      * 根据ID查询
    */
    Blog selectById(Integer id);

    /**
      * 查询所有
    */
    List<Blog> selectAll(Blog blog);

    @Select("select count(*) from blog where user_id = #{userId}")
    Integer countOfOneAuthorBlogs(Integer userId);

    @Select("select * from blog where user_id = #{userId}")
    List<Blog> selectAllofOneAuthor(Integer userId);

    List<Blog> selectPageCollect(Blog blog);
}