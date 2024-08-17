package com.example.service;

import com.example.entity.Account;
import com.example.entity.Likes;
import com.example.mapper.LikesMapper;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LikesService {
    @Resource
    private LikesMapper likesMapper;

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
    }
    public Likes checkLiked(Integer fid, String module,Integer userId) {
        Likes likes = new Likes();
        likes.setFid(fid);
        likes.setModule(module);
        likes.setUserId(userId);
        return likesMapper.selectLikes(likes);
    }
}
