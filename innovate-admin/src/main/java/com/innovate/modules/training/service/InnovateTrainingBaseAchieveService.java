package com.innovate.modules.training.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.training.entity.InnovateTrainingBaseAchieveEntity;

import java.util.Map;

/**
 * 实训基地成果表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
public interface InnovateTrainingBaseAchieveService extends IService<InnovateTrainingBaseAchieveEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

