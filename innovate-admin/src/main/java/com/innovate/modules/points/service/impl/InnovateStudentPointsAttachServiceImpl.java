package com.innovate.modules.points.service.impl;

import com.innovate.modules.points.entity.PointsApplyModel;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private InnovateStudentPointsAttachService innovateStudentPointsAttachService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateStudentPointsAttachEntity> page = this.selectPage(
                new Query<InnovateStudentPointsAttachEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void delAttachLists(PointsApplyModel pointsApplyModel) {
        if (pointsApplyModel.getDelAttachLists() != null) {
            for (InnovateStudentPointsAttachEntity a: pointsApplyModel.getDelAttachLists()) {
                if (a.getAttachId() != null) {
                    a.setIsDel((int) 1);
                    innovateStudentPointsAttachService.updateById(a);
                }
            }

        }
    }

}
