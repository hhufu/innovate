package com.innovate.modules.cooperation.entity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;


@Data
public class InnovateCooperationAttachModel implements Serializable {
    private InnovateCooperationAgreementEntity cooperationAgreementEntity;
    private InnovateCooperationProjectsEntity cooperationProjectsEntity;
    private List<InnovateCooperationMaterialsEntity> cooperationMaterialsList;
    private List<InnovateCooperationMaterialsEntity> delMaterialsList;
}
