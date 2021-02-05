package com.innovate.modules.match.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class MatchUnScoreTeacherEntity implements Serializable {
    private String name;
    private String email;
    private String mobile;
    private Long instituteId;
    private Long teacherSex;
    private String teacherPost;
    private Long teacherTitle;
}
