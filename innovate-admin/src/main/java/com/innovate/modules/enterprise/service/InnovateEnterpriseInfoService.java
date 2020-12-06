package com.innovate.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 入驻企业表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
public interface InnovateEnterpriseInfoService extends IService<InnovateEnterpriseInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void  delList(List<Long> list);

    //导出
    List<InnovateEnterpriseInfoEntity> queryListByIds(List<Long> trainBaseIds ,Map<String, Object> params);

}

