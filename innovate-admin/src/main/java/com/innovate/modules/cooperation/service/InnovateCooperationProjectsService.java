package com.innovate.modules.cooperation.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.cooperation.entity.InnovateCooperationProjectsEntity;

import java.util.Map;

/**
 * 校政企合作项目
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:21
 */
public interface InnovateCooperationProjectsService extends IService<InnovateCooperationProjectsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

