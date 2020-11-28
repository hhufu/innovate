package com.innovate.modules.training.service.impl;

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
        return trainBaseIds.length > 0
                ? this.selectBatchIds(Arrays.asList(trainBaseIds))
                : this.selectList(null);
    }

}
