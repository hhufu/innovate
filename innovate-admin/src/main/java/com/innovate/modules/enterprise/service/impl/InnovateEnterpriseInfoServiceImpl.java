package com.innovate.modules.enterprise.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.enterprise.dao.InnovateEnterpriseInfoDao;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseInfoService;


@Service("innovateEnterpriseInfoService")
public class InnovateEnterpriseInfoServiceImpl extends ServiceImpl<InnovateEnterpriseInfoDao, InnovateEnterpriseInfoEntity> implements InnovateEnterpriseInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateEnterpriseInfoEntity> page = this.selectPage(
                new Query<InnovateEnterpriseInfoEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
