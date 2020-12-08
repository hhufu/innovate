package com.innovate.modules.cooperation.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.innovate.modules.cooperation.entity.InnovateCooperationAgreementEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationAgreementService;
import com.innovate.modules.innovate.entity.ProjectInfoEntity;
import com.innovate.modules.training.entity.InnovateTrainingBaseAchieveEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
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

    @Autowired
    InnovateCooperationAgreementService agreementService;

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
        baseMapper.deleteAgreementList(list);
        baseMapper.deleteProjectsList(list);
    }
    @Override
    public List<InnovateRegisterAuthenticationEntity> queryListByIds(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        if (params.get("ids") != null && !params.get("ids").toString().equals("[]")) {
            map.put("ids", params.get("ids"));
        } else {
            map.put("ids", null);
        }
        map.put("enterpriseName", params.get("enterpriseName"));

        return baseMapper.selectMap(map);
    }


}
