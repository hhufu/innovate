package com.innovate.modules.training.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.training.dao.InnovateTrainingAchieveTypeDao;
import com.innovate.modules.training.entity.InnovateTrainingAchieveTypeEntity;
import com.innovate.modules.training.service.InnovateTrainingAchieveTypeService;


@Service("innovateTrainingAchieveTypeService")
public class InnovateTrainingAchieveTypeServiceImpl extends ServiceImpl<InnovateTrainingAchieveTypeDao, InnovateTrainingAchieveTypeEntity> implements InnovateTrainingAchieveTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateTrainingAchieveTypeEntity> page = this.selectPage(
                new Query<InnovateTrainingAchieveTypeEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
