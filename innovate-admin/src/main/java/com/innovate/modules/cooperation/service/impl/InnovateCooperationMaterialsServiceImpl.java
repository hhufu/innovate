package com.innovate.modules.cooperation.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.cooperation.dao.InnovateCooperationMaterialsDao;
import com.innovate.modules.cooperation.entity.InnovateCooperationMaterialsEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationMaterialsService;


@Service("innovateCooperationMaterialsService")
public class InnovateCooperationMaterialsServiceImpl extends ServiceImpl<InnovateCooperationMaterialsDao, InnovateCooperationMaterialsEntity> implements InnovateCooperationMaterialsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateCooperationMaterialsEntity> page = this.selectPage(
                new Query<InnovateCooperationMaterialsEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
