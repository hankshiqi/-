package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySign {
    private Integer id;
    private Integer actId;
    private Integer userId;
    private String time;
    private String activityName;
    private String userName;
}
