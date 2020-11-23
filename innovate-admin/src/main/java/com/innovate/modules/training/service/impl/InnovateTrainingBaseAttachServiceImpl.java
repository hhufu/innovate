package com.innovate.modules.training.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.training.dao.InnovateTrainingBaseAttachDao;
import com.innovate.modules.training.entity.InnovateTrainingBaseAttachEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseAttachService;


@Service("innovateTrainingBaseAttachService")
public class InnovateTrainingBaseAttachServiceImpl extends ServiceImpl<InnovateTrainingBaseAttachDao, InnovateTrainingBaseAttachEntity> implements InnovateTrainingBaseAttachService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateTrainingBaseAttachEntity> page = this.selectPage(
                new Query<InnovateTrainingBaseAttachEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
