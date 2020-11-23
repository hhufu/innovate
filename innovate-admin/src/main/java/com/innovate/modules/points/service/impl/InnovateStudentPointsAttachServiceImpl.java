package com.innovate.modules.points.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.points.dao.InnovateStudentPointsAttachDao;
import com.innovate.modules.points.entity.InnovateStudentPointsAttachEntity;
import com.innovate.modules.points.service.InnovateStudentPointsAttachService;


@Service("innovateStudentPointsAttachService")
public class InnovateStudentPointsAttachServiceImpl extends ServiceImpl<InnovateStudentPointsAttachDao, InnovateStudentPointsAttachEntity> implements InnovateStudentPointsAttachService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateStudentPointsAttachEntity> page = this.selectPage(
                new Query<InnovateStudentPointsAttachEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
