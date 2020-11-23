package com.innovate.modules.points.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.points.dao.InnovateStudentPointsApplyDao;
import com.innovate.modules.points.entity.InnovateStudentPointsApplyEntity;
import com.innovate.modules.points.service.InnovateStudentPointsApplyService;


@Service("innovateStudentPointsApplyService")
public class InnovateStudentPointsApplyServiceImpl extends ServiceImpl<InnovateStudentPointsApplyDao, InnovateStudentPointsApplyEntity> implements InnovateStudentPointsApplyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateStudentPointsApplyEntity> page = this.selectPage(
                new Query<InnovateStudentPointsApplyEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
