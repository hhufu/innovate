package com.innovate.modules.training.service.impl;

import com.innovate.modules.innovate.entity.InnovateInstituteEntity;
import com.innovate.modules.innovate.service.InnovateInstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.training.dao.InnovateTrainingBaseInfoDao;
import com.innovate.modules.training.entity.InnovateTrainingBaseInfoEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseInfoService;


@Service("innovateTrainingBaseInfoService")
public class InnovateTrainingBaseInfoServiceImpl extends ServiceImpl<InnovateTrainingBaseInfoDao, InnovateTrainingBaseInfoEntity> implements InnovateTrainingBaseInfoService {



    @Autowired
    private InnovateInstituteService innovateInstituteService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateTrainingBaseInfoEntity> page = this.selectPage(
                new Query<InnovateTrainingBaseInfoEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

    /**
     * 依据实训基地id查询
     * @param trainBaseIds
     * @return
     */
    @Override
    public List<InnovateTrainingBaseInfoEntity> queryListByDeptAndIds(Long[] trainBaseIds) {
        List<InnovateTrainingBaseInfoEntity> baseInfoEntities = new ArrayList<>();
        if (trainBaseIds.length > 0) {
            baseInfoEntities = this.selectBatchIds(Arrays.asList(trainBaseIds));
        } else {
            baseInfoEntities = this.selectList(null);
        }

        for (InnovateTrainingBaseInfoEntity baseInfo: baseInfoEntities) {
            InnovateInstituteEntity innovateInstituteEntity = innovateInstituteService.selectById(baseInfo.getInstituteId());
            baseInfo.setInstituteName(innovateInstituteEntity.getInstituteName());
        }

        return baseInfoEntities;
    }

}
