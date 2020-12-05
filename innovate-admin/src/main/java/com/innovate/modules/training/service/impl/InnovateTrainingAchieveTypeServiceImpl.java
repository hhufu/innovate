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
        EntityWrapper<InnovateTrainingAchieveTypeEntity> entityWrapper = new EntityWrapper<>();
        if (params.get("trainingAchieveType") != null) entityWrapper.like("training_achieve_type", params.get("trainingAchieveType").toString());
        // 按时间倒序
        entityWrapper.orderBy("material_type_id", false);
        Page<InnovateTrainingAchieveTypeEntity> page = this.selectPage(
                new Query<InnovateTrainingAchieveTypeEntity>(params).getPage(),
                entityWrapper
        );

        return new PageUtils(page);
    }

}
