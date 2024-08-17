package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
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
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    /**
     * 新增
     */
    public void add(Comment comment) {
        Account account = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(account.getRole())){
            comment.setUserId(account.getId());
        }
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);
        if(comment.getRootId()==null){
            comment.setRootId(comment.getId());
            commentMapper.updateById(comment);
        }

    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    /**
     * 根据ID查询
     */
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }

    /**
     * 分页查询
     */
    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }

    public List<Comment> selectForUser(Comment comment) {
        //获取所有的根评论
        List<Comment> commentList=commentMapper.selectForUser(comment);
        for(Comment c:commentList){
            Comment comment1 = new Comment();
            comment1.setRootId(c.getId());
            //获取子评论
            List<Comment> childComments = this.selectAll(comment1);
            childComments =childComments.stream().filter(child->!child.getId().equals(child.getRootId())).collect(Collectors.toList());
            c.setChildren(childComments);
        }
        return commentList;
    }


    public Integer getCount(Comment comment) {
        return commentMapper.getCount(comment);
    }
}