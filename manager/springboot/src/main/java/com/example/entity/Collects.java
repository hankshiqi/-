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
public class Collects implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 标题 */
    private Integer fid;
    /** 内容 */
    private Integer userId;
    private String module;


}