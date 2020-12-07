package com.innovate.modules.training.service.impl;

import com.innovate.modules.innovate.entity.InnovateInstituteEntity;
import com.innovate.modules.innovate.service.InnovateInstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        EntityWrapper<InnovateTrainingBaseInfoEntity> entityWrapper = new EntityWrapper<>();
        if (params.get("trainingBaseName") != null) entityWrapper.like("training_base_name", params.get("trainingBaseName").toString());
        if (params.get("instituteId") != null) entityWrapper.eq("institute_id", Integer.parseInt(params.get("instituteId").toString()));
        if (params.get("isDel") != null) entityWrapper.eq("is_del", Integer.parseInt(params.get("isDel").toString()));
        Page<InnovateTrainingBaseInfoEntity> page = this.selectPage(
                new Query<InnovateTrainingBaseInfoEntity>(params).getPage(),
                entityWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 依据实训基地id查询
     * @param
     * @return
     */
    @Override
    public List<InnovateTrainingBaseInfoEntity> queryListByDeptAndIds(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        if (params.get("ids") != null && !params.get("ids").toString().equals("[]")) {
            map.put("ids", params.get("ids"));
        } else {
            map.put("ids", null);
        }
        map.put("instituteId", params.get("instituteId"));
        map.put("trainingBaseName", params.get("trainingBaseName"));

        return baseMapper.selectMap(map);
    }

    @Override
    public int deleteList(List<Long> asList) {
        return baseMapper.deleteList(asList);
    }

}
