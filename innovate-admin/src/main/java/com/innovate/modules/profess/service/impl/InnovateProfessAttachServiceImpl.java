package com.innovate.modules.profess.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.profess.dao.InnovateProfessAttachDao;
import com.innovate.modules.profess.entity.InnovateProfessAttachEntity;
import com.innovate.modules.profess.service.InnovateProfessAttachService;


@Service("innovateProfessAttachService")
public class InnovateProfessAttachServiceImpl extends ServiceImpl<InnovateProfessAttachDao, InnovateProfessAttachEntity> implements InnovateProfessAttachService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateProfessAttachEntity> page = this.selectPage(
                new Query<InnovateProfessAttachEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
