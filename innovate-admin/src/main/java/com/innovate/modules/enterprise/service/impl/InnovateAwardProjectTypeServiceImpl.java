package com.innovate.modules.enterprise.service.impl;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.enterprise.dao.InnovateAwardProjectTypeDao;
import com.innovate.modules.enterprise.entity.InnovateAwardProjectTypeEntity;
import com.innovate.modules.enterprise.service.InnovateAwardProjectTypeService;


@Service("innovateAwardProjectTypeService")
public class InnovateAwardProjectTypeServiceImpl extends ServiceImpl<InnovateAwardProjectTypeDao, InnovateAwardProjectTypeEntity> implements InnovateAwardProjectTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Object awardProjectType = params.get("award_project_type");
        EntityWrapper<InnovateAwardProjectTypeEntity> wrapper = new EntityWrapper<>();
        if (!"".equals(awardProjectType)){
            wrapper.like("award_project_type",awardProjectType.toString());
        }
        Page<InnovateAwardProjectTypeEntity> page = this.selectPage(
                new Query<InnovateAwardProjectTypeEntity>(params).getPage(),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<InnovateAwardProjectTypeEntity> queryListByIds(List<Long> awardProjectjTypeIds) {
        return awardProjectjTypeIds.size()>0 ? this.selectBatchIds(awardProjectjTypeIds): this.selectList(null);
    }

}
