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
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 标题 */
    private String name;
    /** 内容 */
    private String content;
    private String descr;
    /** 创建人 */
    private String cover;
    private String start;
    private String end;
    private String form;
    private String address;
    private Integer readCount;
    private String host;
    private boolean isDue;
    private boolean isSign;
    private Boolean isLiked;
    private Boolean isCollected;
    private Integer likeCount;
    private Integer collectCount;
}