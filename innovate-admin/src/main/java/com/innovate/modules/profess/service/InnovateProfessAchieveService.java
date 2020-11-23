package com.innovate.modules.profess.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.profess.entity.InnovateProfessAchieveEntity;

import java.util.Map;

/**
 * 专创成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
public interface InnovateProfessAchieveService extends IService<InnovateProfessAchieveEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

