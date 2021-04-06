package com.innovate.modules.innovate.service.impl;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.innovate.dao.InnovateDeclarationProcessSettingDao;
import com.innovate.modules.innovate.entity.InnovateDeclarationProcessSettingEntity;
import com.innovate.modules.innovate.service.InnovateDeclarationProcessSettingService;


@Service("innovateDeclarationProcessSettingService")
public class InnovateDeclarationProcessSettingServiceImpl extends ServiceImpl<InnovateDeclarationProcessSettingDao, InnovateDeclarationProcessSettingEntity> implements InnovateDeclarationProcessSettingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<InnovateDeclarationProcessSettingEntity> entityWrapper = new EntityWrapper<>();
        if (params.get("key") != null && !params.get("key").equals("")) entityWrapper.eq("declare_process_name", Integer.parseInt(params.get("key").toString()));
        entityWrapper.orderBy("start_time", false);
        Page<InnovateDeclarationProcessSettingEntity> page = this.selectPage(
                new Query<InnovateDeclarationProcessSettingEntity>(params).getPage(),
                entityWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public int queryCount(Map<String, Object> params) {
        return baseMapper.queryCount(params);
    }

    @Override
    public int queryStartTime(Map<String, Object> params) {
        return baseMapper.queryStartTime(params);
    }

    @Override
    public int queryEndTime(Map<String, Object> params) {
        return baseMapper.queryEndTime(params);
    }

    @Override
    public InnovateDeclarationProcessSettingEntity selectByTime(Map<String, Object> params) {
        return baseMapper.selectByTime(params);
    }

}
