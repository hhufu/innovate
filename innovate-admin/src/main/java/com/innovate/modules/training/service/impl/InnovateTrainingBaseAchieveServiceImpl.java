package com.innovate.modules.training.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.training.dao.InnovateTrainingBaseAchieveDao;
import com.innovate.modules.training.entity.InnovateTrainingBaseAchieveEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseAchieveService;


@Service("innovateTrainingBaseAchieveService")
public class InnovateTrainingBaseAchieveServiceImpl extends ServiceImpl<InnovateTrainingBaseAchieveDao, InnovateTrainingBaseAchieveEntity> implements InnovateTrainingBaseAchieveService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateTrainingBaseAchieveEntity> page = this.selectPage(
                new Query<InnovateTrainingBaseAchieveEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
