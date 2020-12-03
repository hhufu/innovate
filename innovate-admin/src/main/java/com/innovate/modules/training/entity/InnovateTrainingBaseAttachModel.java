package com.innovate.modules.training.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InnovateTrainingBaseAttachModel implements Serializable {
    private InnovateTrainingBaseAchieveEntity trainingBaseAchieveEntity;
    private List<InnovateTrainingBaseAttachEntity> trainingBaseAttachList;
    private List<InnovateTrainingBaseAttachEntity> delBaseAttachList;
}
