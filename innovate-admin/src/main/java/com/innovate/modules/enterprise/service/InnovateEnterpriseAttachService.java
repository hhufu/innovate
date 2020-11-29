package com.innovate.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.innovate.modules.training.entity.InnovateTrainingBaseInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 企业附件表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
public interface InnovateEnterpriseAttachService extends IService<InnovateEnterpriseAttachEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

