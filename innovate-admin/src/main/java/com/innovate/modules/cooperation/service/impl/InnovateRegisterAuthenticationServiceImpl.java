package com.innovate.modules.cooperation.service.impl;

import org.springframework.stereotype.Service;
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
        Page<InnovateRegisterAuthenticationEntity> page = this.selectPage(
                new Query<InnovateRegisterAuthenticationEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
