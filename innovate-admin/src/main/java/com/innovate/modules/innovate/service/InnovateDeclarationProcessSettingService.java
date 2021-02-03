package com.innovate.modules.innovate.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.innovate.entity.InnovateDeclarationProcessSettingEntity;

import java.util.Map;

/**
 * 申报流程设置
 *
 * @author HHUFU
 * @email 437213666@qq.com
 * @date 2021-02-03 11:43:20
 */
public interface InnovateDeclarationProcessSettingService extends IService<InnovateDeclarationProcessSettingEntity> {

    PageUtils queryPage(Map<String, Object> params);

    int queryCount(Map<String, Object> params);

    int queryStartTime(Map<String, Object> params);

    int queryEndTime(Map<String, Object> params);
}

