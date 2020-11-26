package com.innovate.modules.cooperation.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.cooperation.dao.InnovateCooperationAgreementDao;
import com.innovate.modules.cooperation.entity.InnovateCooperationAgreementEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationAgreementService;


@Service("innovateCooperationAgreementService")
public class InnovateCooperationAgreementServiceImpl extends ServiceImpl<InnovateCooperationAgreementDao, InnovateCooperationAgreementEntity> implements InnovateCooperationAgreementService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateCooperationAgreementEntity> page = this.selectPage(
                new Query<InnovateCooperationAgreementEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
