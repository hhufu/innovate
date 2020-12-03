package com.innovate.modules.enterprise.entity;

import lombok.Data;

import java.util.List;

@Data
public class InnovateEnterpriseInfoModel {
    //入驻企业
    InnovateEnterpriseInfoEntity infoEntity;
    //企业成果
    InnovateEnterpriseAchieveEntity achieveEntity;
    //企业项目
    InnovateEnterpriseProjectEntity projectEntity;
    //附件集合
    List<InnovateEnterpriseAttachEntity> attachEntities;
}
