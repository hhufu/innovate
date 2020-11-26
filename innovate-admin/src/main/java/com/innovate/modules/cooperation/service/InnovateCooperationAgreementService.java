package com.innovate.modules.cooperation.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.cooperation.entity.InnovateCooperationAgreementEntity;

import java.util.Map;

/**
 * 企业协议管理
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:20
 */
public interface InnovateCooperationAgreementService extends IService<InnovateCooperationAgreementEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

