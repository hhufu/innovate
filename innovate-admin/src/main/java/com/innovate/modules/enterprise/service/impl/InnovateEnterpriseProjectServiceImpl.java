package com.innovate.modules.enterprise.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.enterprise.dao.InnovateEnterpriseProjectDao;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseProjectService;


@Service("innovateEnterpriseProjectService")
public class InnovateEnterpriseProjectServiceImpl extends ServiceImpl<InnovateEnterpriseProjectDao, InnovateEnterpriseProjectEntity> implements InnovateEnterpriseProjectService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Object userId = params.get("project_user_id");
        EntityWrapper<InnovateEnterpriseProjectEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del" ,0);
        if (userId!=null){
            wrapper.eq("project_user_id",userId.toString());
        }
        Page<InnovateEnterpriseProjectEntity> page = this.selectPage(
                new Query<InnovateEnterpriseProjectEntity>(params).getPage(),
               wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void delList(List<Long> list) {
        baseMapper.delList(list);
    }

}
