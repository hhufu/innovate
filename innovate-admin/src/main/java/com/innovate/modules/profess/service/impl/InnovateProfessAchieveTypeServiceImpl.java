package com.innovate.modules.profess.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.profess.dao.InnovateProfessAchieveTypeDao;
import com.innovate.modules.profess.entity.InnovateProfessAchieveTypeEntity;
import com.innovate.modules.profess.service.InnovateProfessAchieveTypeService;


@Service("innovateProfessAchieveTypeService")
public class InnovateProfessAchieveTypeServiceImpl extends ServiceImpl<InnovateProfessAchieveTypeDao, InnovateProfessAchieveTypeEntity> implements InnovateProfessAchieveTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateProfessAchieveTypeEntity> page = this.selectPage(
                new Query<InnovateProfessAchieveTypeEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
