package com.innovate.modules.enterprise.service.impl;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
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
        Object userId = params.get("enterpriseUserId");
        Object instituteId = params.get("instituteId");
        Object projectName = params.get("project_name");
        Object projectYear = params.get("projectYear");
        EntityWrapper<InnovateEnterpriseProjectEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del", 0);

        if (userId!=null ){
//            if (instituteId!=null){
//                wrapper.eq("institute_id", instituteId.toString());
//            }else {
                wrapper.eq("project_user_id", userId.toString());
//            }
        }

        if (!"".equals(projectName)){
            wrapper.like("project_name" , projectName.toString());
        }

        if (projectYear != null){
            wrapper.eq("project_year" , projectYear.toString());
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

    @Override
    public List<InnovateEnterpriseProjectEntity> queryListByIds(List<Long> enterpProjIds) {
        return enterpProjIds.size() > 0 ? this.selectBatchIds(enterpProjIds) : this.selectList(new EntityWrapper<InnovateEnterpriseProjectEntity>().eq("is_del", 0));
    }

}
