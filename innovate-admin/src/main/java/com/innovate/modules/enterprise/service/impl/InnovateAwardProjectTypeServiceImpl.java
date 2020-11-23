package com.innovate.modules.enterprise.service.impl;

import org.springframework.stereotype.Service;
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
        Page<InnovateAwardProjectTypeEntity> page = this.selectPage(
                new Query<InnovateAwardProjectTypeEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
