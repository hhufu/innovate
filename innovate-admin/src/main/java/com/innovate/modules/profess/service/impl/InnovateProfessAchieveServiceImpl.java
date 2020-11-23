package com.innovate.modules.profess.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.profess.dao.InnovateProfessAchieveDao;
import com.innovate.modules.profess.entity.InnovateProfessAchieveEntity;
import com.innovate.modules.profess.service.InnovateProfessAchieveService;


@Service("innovateProfessAchieveService")
public class InnovateProfessAchieveServiceImpl extends ServiceImpl<InnovateProfessAchieveDao, InnovateProfessAchieveEntity> implements InnovateProfessAchieveService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateProfessAchieveEntity> page = this.selectPage(
                new Query<InnovateProfessAchieveEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
