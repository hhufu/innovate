package com.innovate.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.sys.entity.SysMailCodeEntity;

import java.util.Map;

/**
 *
 *
 * @author luo
 * @email 1275986528@qq.com
 * @date 2021-07-13 11:01:39
 */
public interface SysMailCodeService extends IService<SysMailCodeEntity> {

    PageUtils queryPage(Map<String, Object> params);
    SysMailCodeEntity queryMailCode(SysMailCodeEntity sysMailCodeEntity);
}

