package com.innovate.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAchieveEntity;

import java.util.List;
import java.util.Map;

/**
 * 企业成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:32
 */
public interface InnovateEnterpriseAchieveService extends IService<InnovateEnterpriseAchieveEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void delList(List<Long> listId);
}

