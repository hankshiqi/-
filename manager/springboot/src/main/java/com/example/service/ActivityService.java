package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Blog;
import com.example.entity.Category;
import com.example.entity.User;
import com.example.mapper.BlogMapper;
import com.example.mapper.CategoryMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class BlogService {

    @Resource
    private BlogMapper blogMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private UserMapper userMapper;
    /**
     * 新增
     */
    public void add(Blog blog) {
        Account currentUser = TokenUtils.getCurrentUser();
        blog.setDate(DateUtil.now());
        String categoryName = blog.getCategoryName();
        String userName = blog.getUserName();
        //new User().se
        User user = userMapper.selectByUsername(userName);
        Integer userId = user.getId();
        blog.setUserId(userId);
        Category category = categoryMapper.selectByCategoryname(categoryName);
        Integer categoryId = category.getId();
        blog.setCategoryId(categoryId);
        blogMapper.insert(blog);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        blogMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            blogMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Blog blog) {
        blogMapper.updateById(blog);
    }

    /**
     * 根据ID查询
     */
    public Blog selectById(Integer id) {
        return blogMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Blog> selectAll(Blog blog) {
        return blogMapper.selectAll(blog);
    }

    /**
     * 分页查询
     */
    public PageInfo<Blog> selectPage(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list = blogMapper.selectAll(blog);
        return PageInfo.of(list);
    }

}