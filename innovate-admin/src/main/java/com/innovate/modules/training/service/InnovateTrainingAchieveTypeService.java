package com.innovate.modules.training.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.training.entity.InnovateTrainingAchieveTypeEntity;

import java.util.Map;

/**
 * 实训成果类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
public interface InnovateTrainingAchieveTypeService extends IService<InnovateTrainingAchieveTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

