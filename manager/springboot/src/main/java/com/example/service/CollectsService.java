package com.example.service;

import com.example.common.enums.ModuleCategoryEnum;
import com.example.entity.Account;
import com.example.entity.Blog;
import com.example.entity.Collects;
import com.example.mapper.BlogMapper;
import com.example.mapper.CollectsMapper;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CollectsService {
    @Resource
    private CollectsMapper collectsMapper;
    @Resource
    private BlogMapper blogMapper;

    public Integer getCollectsNum(Integer fid, String module) {
        return collectsMapper.selectCollectsNum(fid, module);
    }

    public void changeCollects(Collects collects) {
        Account currentUser = TokenUtils.getCurrentUser();
        collects.setUserId(currentUser.getId());
        Collects collectsdb = collectsMapper.selectCollects(collects);
        if(collectsdb==null){
            collects.setUserId(currentUser.getId());
            collectsMapper.insertCollects(collects);

        }else{
            collectsMapper.deleteCollects(collectsdb);
        }
        //根据收藏的增加和取消结果，更新对应的博客的收藏数
        if(collects.getModule().equals(ModuleCategoryEnum.BLOG.name)){
            Blog blog = blogMapper.selectById(collects.getFid());
            if (collectsdb == null) {
                blog.setCollectsNum(blog.getCollectsNum() + 1);
            } else {
                blog.setCollectsNum(blog.getCollectsNum() - 1);
            }
            blogMapper.updateById(blog);
        }
    }
    public Collects checkCollected(Integer fid, String module,Integer userId) {
        Collects collects = new Collects();
        collects.setFid(fid);
        collects.setModule(module);
        collects.setUserId(userId);
        return collectsMapper.selectCollects(collects);
    }


}
