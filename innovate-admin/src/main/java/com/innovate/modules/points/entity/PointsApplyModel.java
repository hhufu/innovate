package com.innovate.modules.points.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author 冯天华
 * @Date 2020/11/26 9:46
 * @Version 1.0
 */
@Data
public class PointsApplyModel implements Serializable {
    private InnovateStudentPointsApplyEntity pointsApplyEntity;
    private List<InnovateStudentPointsAttachEntity> pointsAttachEntityList;
    private List<InnovateStudentPointsAttachEntity> delAttachLists;
}
