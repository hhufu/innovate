package com.innovate.modules.profess.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.profess.entity.InnovateProfessAttachEntity;

import java.util.Map;

/**
 * 专创附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
public interface InnovateProfessAttachService extends IService<InnovateProfessAttachEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

