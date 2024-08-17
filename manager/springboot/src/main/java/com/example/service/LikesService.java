package com.example.service;

import com.example.common.enums.ModuleCategoryEnum;
import com.example.entity.Account;
import com.example.entity.Blog;
import com.example.entity.Likes;
import com.example.mapper.BlogMapper;
import com.example.mapper.LikesMapper;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LikesService {
    @Resource
    private LikesMapper likesMapper;
    @Resource
    private BlogMapper blogMapper;

    public Integer getLikesNum(Integer fid, String module) {
        return likesMapper.selectLikesNum(fid, module);
    }

    public void changeLikes(Likes likes) {
        Account currentUser = TokenUtils.getCurrentUser();
        likes.setUserId(currentUser.getId());
        Likes likesdb = likesMapper.selectLikes(likes);
        if(likesdb==null){
            likes.setUserId(currentUser.getId());
            likesMapper.insertLikes(likes);
        }else{
            likesMapper.deleteLikes(likesdb);
        }
        //根据点赞的增加和取消结果，更新对应的博客的点赞数
        if(likes.getModule().equals(ModuleCategoryEnum.BLOG.name)){
            Blog blog = blogMapper.selectById(likes.getFid());
            if (likesdb == null) {
                blog.setLikesNum(blog.getLikesNum() + 1);
            } else {
                blog.setLikesNum(blog.getLikesNum() - 1);
            }
            blogMapper.updateById(blog);
        }
    }
    public Likes checkLiked(Integer fid, String module,Integer userId) {
        Likes likes = new Likes();
        likes.setFid(fid);
        likes.setModule(module);
        likes.setUserId(userId);
        return likesMapper.selectLikes(likes);
    }


}
