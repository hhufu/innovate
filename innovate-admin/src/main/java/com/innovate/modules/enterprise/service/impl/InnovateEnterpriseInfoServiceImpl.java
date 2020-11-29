package com.innovate.modules.enterprise.service.impl;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
        Object userId = params.get("project_user_id");
        EntityWrapper<InnovateEnterpriseInfoEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del" ,0).orderBy("apply_status");
        if (userId!=null){
            wrapper.eq("enterprise_user_id",userId.toString());
        }
        Page<InnovateEnterpriseInfoEntity> page = this.selectPage(
                new Query<InnovateEnterpriseInfoEntity>(params).getPage(),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void delList(List<Long> list) {
        baseMapper.delList(list);
    }

}
