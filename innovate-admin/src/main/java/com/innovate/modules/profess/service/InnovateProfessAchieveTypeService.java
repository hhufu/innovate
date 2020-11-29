package com.innovate.modules.profess.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.profess.entity.InnovateProfessAchieveTypeEntity;

import java.util.Map;

/**
 * 专创成果类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
public interface InnovateProfessAchieveTypeService extends IService<InnovateProfessAchieveTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
