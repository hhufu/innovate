package com.innovate.modules.training.service.impl;

import com.innovate.modules.training.entity.InnovateTrainingAchieveTypeEntity;
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
        EntityWrapper<InnovateTrainingBaseAttachEntity> entityWrapper = new EntityWrapper<>();
        // 按时间倒序
        entityWrapper.orderBy("attach_id", false);
        Page<InnovateTrainingBaseAttachEntity> page = this.selectPage(
                new Query<InnovateTrainingBaseAttachEntity>(params).getPage(),
                entityWrapper
        );

        return new PageUtils(page);
    }

}
