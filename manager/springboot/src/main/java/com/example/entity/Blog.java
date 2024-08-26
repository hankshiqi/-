package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 公告信息表
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 标题 */
    private String title;
    /** 内容 */
    private String content;
    private String descr;
    /** 创建人 */
    private String cover;
    private String tags;
    private Integer userId;
    private String date;
    private Integer readCount;
    private Integer categoryId;
    private String userName;
    private String categoryName;
    private User user;
    private Integer likesNum;
    private Integer collectsNum;
    private Boolean isLiked;
    private Boolean isCollected;

}