package com.example.controller;

import com.example.common.Result;
import com.example.entity.Blog;
import com.example.service.BlogService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Blog blog) {
        blogService.add(blog);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        blogService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        blogService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Blog blog) {
        blogService.updateById(blog);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Blog blog = blogService.selectById(id);
        return Result.success(blog);
    }


    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Blog blog ) {
        List<Blog> list = blogService.selectAll(blog);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Blog> page = blogService.selectPage(blog, pageNum, pageSize);
        return Result.success(page);
    }
    @GetMapping("/selectPageByUser")

    public Result selectPageByUser(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Blog> page = blogService.selectPageByUser(blog, pageNum, pageSize);
        return Result.success(page);
    }
    @GetMapping("/selectPageCollect")
    public Result selectPageCollect(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Blog> page = blogService.selectPageCollect(blog, pageNum, pageSize);
        return Result.success(page);
    }


    @GetMapping("/selectTop")
    public Result selectTop(){
        List<Blog> list=blogService.selectTop();
        return Result.success(list);
    }
    @PostMapping("/selectRecommendBlog")
    public Result selectRecommendBlog(@RequestBody Blog blog){
        Set<Blog> blogSet=blogService.selectRecommendBlog(blog);
        return Result.success(blogSet);
    }
    @GetMapping("/countOfOneAuthorBlogs/{userId}")
    public Result countOfOneAuthorBlogs(@PathVariable Integer userId){
        Integer count=blogService.countOfOneAuthorBlogs(userId);
        return Result.success(count);
    }
    @GetMapping("/countOfOneAuthorLikes/{userId}")
    public Result countOfOneAuthorLikes(@PathVariable Integer userId) {
        Integer count = blogService.countOfOneAuthorLikes(userId);
        return Result.success(count);
    }
    @GetMapping("/countOfOneAuthorCollects/{userId}")
    public Result countOfOneAuthorCollects(@PathVariable Integer userId) {
        Integer count = blogService.countOfOneAuthorCollects(userId);
        return Result.success(count);
    }

    //更新阅读量
    @PutMapping("/updateReadCount/{id}")
    public Result updateReadCount(@PathVariable Integer id){
        blogService.updateReadCount(id);
        return Result.success();
    }
}