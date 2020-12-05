package com.innovate.modules.cooperation.service.impl;

import com.innovate.modules.innovate.entity.ProjectInfoEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.cooperation.dao.InnovateRegisterAuthenticationDao;
import com.innovate.modules.cooperation.entity.InnovateRegisterAuthenticationEntity;
import com.innovate.modules.cooperation.service.InnovateRegisterAuthenticationService;


@Service("innovateRegisterAuthenticationService")
public class InnovateRegisterAuthenticationServiceImpl extends ServiceImpl<InnovateRegisterAuthenticationDao, InnovateRegisterAuthenticationEntity> implements InnovateRegisterAuthenticationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<InnovateRegisterAuthenticationEntity> entityWrapper = new EntityWrapper<>();
        if (params.get("enterpriseName") != null) entityWrapper.like("enterprise_name", params.get("enterpriseName").toString());
        if (params.get("isDel") != null) entityWrapper.eq("is_del", Integer.parseInt(params.get("isDel").toString()));
        // 按时间倒序
        entityWrapper.orderBy("authentication_id", false);
        Page<InnovateRegisterAuthenticationEntity> page = this.selectPage(
                new Query<InnovateRegisterAuthenticationEntity>(params).getPage(),
                entityWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public InnovateRegisterAuthenticationEntity queryById(Long authenticationId) {
        return baseMapper.queryById(authenticationId);
    }
    @Override
    public void deleteList(List<Long> list) {
        baseMapper.deleteList(list);
    }

}
