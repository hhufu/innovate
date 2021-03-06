package com.innovate.modules.points.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
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
        EntityWrapper<InnovateSysPointsEntity> entityWrapper = new EntityWrapper<>();

        if (params.get("isDel")!=null)entityWrapper.eq("is_del", params.get("isDel"));
        if (params.get("parentId")!=null)entityWrapper.eq("parent_id", params.get("parentId"));
        if (params.get("key")!=null)entityWrapper.eq(params.get("key").toString(), params.get("keyValue"));
        if (params.get("prizeGradeIsNull")!=null)entityWrapper.isNotNull("prize_grade");
        if (params.get("groupBy")!=null)entityWrapper.groupBy(params.get("groupBy").toString());
//        entityWrapper.orderBy(true, "integral_id", false);

//        Page<InnovateSysPointsEntity> page = this.selectPage(
//                new Query<InnovateSysPointsEntity>(params).getPage(),
//                entityWrapper
//        );
        Page<InnovateSysPointsEntity> page = this.selectPage(
                new Query<InnovateSysPointsEntity>(params).getPage(),
                entityWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public int deleteList(List<Long> asList) {

        return baseMapper.deleteList(asList);
    }

    @Override
    public List selectPoints(Map<String, Object> params) {

        return baseMapper.selectPoints(params);
    }

}
