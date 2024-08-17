package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.ModuleCategoryEnum;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Resource
    private UserService userService;
    @Resource
    private LikesService likesService;
    @Resource
    private CollectsService collectsService;
    /**
     * 新增
     */
    public void add(Blog blog) {
        Account currentUser = TokenUtils.getCurrentUser();
        blog.setDate(DateUtil.now());
        String categoryName = blog.getCategoryName();
        String userName = blog.getUserName();
        //new User().se
        //设置blog作者ID
        User user = userMapper.selectByUsername(userName);
        Integer userId = user.getId();
        blog.setUserId(userId);
        //设置blog分类Id
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
        Blog blog = blogMapper.selectById(id);
        blog.setUser(userService.selectById(blog.getUserId()));
        //获取blog的点赞数量
        Integer blogLikeNums = likesService.getLikesNum(id, ModuleCategoryEnum.BLOG.name);
        blog.setLikesNum(blogLikeNums);
        //获取当前用户是否点赞
        Account currentUser = TokenUtils.getCurrentUser();
        Boolean isLiked = likesService.checkLiked(id, ModuleCategoryEnum.BLOG.name, currentUser.getId()) != null;
        blog.setIsLiked(isLiked);
        //获取blog的收藏数量
        Integer collectsNums = collectsService.getCollectsNum(id, ModuleCategoryEnum.BLOG.name);
        blog.setCollectsNum(collectsNums);
        //获取当前用户是否收藏
        Boolean isCollected= collectsService.checkCollected(id, ModuleCategoryEnum.BLOG.name, currentUser.getId()) != null;
        blog.setIsCollected(isCollected);
        return blog;
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

    public List<Blog> selectTop() {
        List<Blog> blogs = blogMapper.selectAll(null);
        blogs=blogs.stream().sorted((a,b)->b.getReadCount().compareTo(a.getReadCount()))
                .limit(20)
                .collect(Collectors.toList());
        return blogs;
    }
    //根据标签获取推荐的博客
    public Set<Blog> selectRecommendBlog(Blog blog) {
        String tags = blog.getTags();
        Set<Blog> blogSet =new HashSet<>();
        if(ObjectUtil.isNotEmpty(tags)){
            List<Blog> blogs = blogMapper.selectAll(null);
            JSONArray tagsArray= (JSONArray) JSONUtil.parse(tags);
            for(Object tag:tagsArray) {
                Set<Blog> tagBlogs = blogs.stream().filter(b -> b.getTags().contains(tag.toString())
                        &&(b.getId()!=blog.getId())).collect(Collectors.toSet());
                blogSet.addAll(tagBlogs);
            }
        }
        return blogSet.stream().limit(5).collect(Collectors.toSet());
    }
    //根据作者id获取博客数量
    public Integer countOfOneAuthorBlogs(Integer userId) {
        return blogMapper.countOfOneAuthorBlogs(userId);
    }
    //根据作者id获取点赞数量
    public Integer countOfOneAuthorLikes(Integer userId) {
        Integer totalLikes=0;
        List<Blog> blogs = blogMapper.selectAllofOneAuthor(userId);
        for(Blog blog:blogs){
            totalLikes+=blog.getLikesNum();
        }
        return totalLikes;
    }
    //根据作者id获取收藏数量
    public Integer countOfOneAuthorCollects(Integer userId) {
        Integer totalCollects=0;
        List<Blog> blogs = blogMapper.selectAllofOneAuthor(userId);
        for(Blog blog:blogs){
            totalCollects+=blog.getCollectsNum();
        }
        return totalCollects;
    }
}