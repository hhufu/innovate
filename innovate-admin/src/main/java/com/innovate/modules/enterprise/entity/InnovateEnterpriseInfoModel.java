package com.innovate.modules.enterprise.entity;

import lombok.Data;

import java.util.List;

@Data
public class InnovateEnterpriseInfoModel {
    //入驻企业
    private InnovateEnterpriseInfoEntity infoEntity;
    //企业成果
    private InnovateEnterpriseAchieveEntity achieveEntity;
    //企业项目
    private InnovateEnterpriseProjectEntity projectEntity;
    //附件集合
    private List<InnovateEnterpriseAttachEntity> attachEntities;
    //删除附件
    private List<InnovateEnterpriseAttachEntity> delAttachLists;

}
