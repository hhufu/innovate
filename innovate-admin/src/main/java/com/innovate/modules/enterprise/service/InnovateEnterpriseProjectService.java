package com.innovate.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;

import java.util.List;
import java.util.Map;

/**
 * 企业项目表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
public interface InnovateEnterpriseProjectService extends IService<InnovateEnterpriseProjectEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //伪删除
    void  delList(List<Long> list);
}

