package com.innovate.modules.points.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.points.dao.InnovateSysPointsDao;
import com.innovate.modules.points.entity.InnovateSysPointsEntity;
import com.innovate.modules.points.service.InnovateSysPointsService;


@Service("innovateSysPointsService")
public class InnovateSysPointsServiceImpl extends ServiceImpl<InnovateSysPointsDao, InnovateSysPointsEntity> implements InnovateSysPointsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateSysPointsEntity> page = this.selectPage(
                new Query<InnovateSysPointsEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

}
