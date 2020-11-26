package com.innovate.modules.cooperation.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.cooperation.entity.InnovateRegisterAuthenticationEntity;

import java.util.Map;

/**
 * 企业登记认证表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
public interface InnovateRegisterAuthenticationService extends IService<InnovateRegisterAuthenticationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

