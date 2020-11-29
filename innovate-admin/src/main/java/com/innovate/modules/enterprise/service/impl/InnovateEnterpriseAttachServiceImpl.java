package com.innovate.modules.enterprise.service.impl;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.enterprise.dao.InnovateEnterpriseAttachDao;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAttachService;


@Service("innovateEnterpriseAttachService")
public class InnovateEnterpriseAttachServiceImpl extends ServiceImpl<InnovateEnterpriseAttachDao, InnovateEnterpriseAttachEntity> implements InnovateEnterpriseAttachService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateEnterpriseAttachEntity> page = this.selectPage(
                new Query<InnovateEnterpriseAttachEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }



}
