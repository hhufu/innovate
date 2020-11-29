package com.innovate.modules.enterprise.entity;

import lombok.Data;

import java.util.List;

@Data
public class InnovateEnterpriseInfoModel {
    InnovateEnterpriseInfoEntity innovateEnterpriseInfoEntity;
    List<InnovateEnterpriseAttachEntity> innovateEnterpriseAttachEntities;
}
