package com.innovate.modules.cooperation.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.cooperation.dao.InnovateCooperationProjectsDao;
import com.innovate.modules.cooperation.entity.InnovateCooperationProjectsEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationProjectsService;


@Service("innovateCooperationProjectsService")
public class InnovateCooperationProjectsServiceImpl extends ServiceImpl<InnovateCooperationProjectsDao, InnovateCooperationProjectsEntity> implements InnovateCooperationProjectsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateCooperationProjectsEntity> page = this.selectPage(
                new Query<InnovateCooperationProjectsEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
