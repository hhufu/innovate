package com.innovate.modules.training.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.training.entity.InnovateTrainingBaseAttachEntity;

import java.util.Map;

/**
 * 实训基地附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
public interface InnovateTrainingBaseAttachService extends IService<InnovateTrainingBaseAttachEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

