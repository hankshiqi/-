package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id;
    private String content;
    private Integer userId;
    private Integer pid;
    private Integer rootId;
    private String time;
    private Integer fid;
    private String module;
    private String userName;
    private String userAvatar;
    private List<Comment> children;
    private String replyName;
}
