package com.innovate.modules.enterprise.service.impl;

import com.innovate.modules.enterprise.utility.ExportShiro;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.enterprise.dao.InnovateEnterpriseAchieveDao;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAchieveEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAchieveService;


@Service("innovateEnterpriseAchieveService")
public class InnovateEnterpriseAchieveServiceImpl extends ServiceImpl<InnovateEnterpriseAchieveDao, InnovateEnterpriseAchieveEntity> implements InnovateEnterpriseAchieveService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        ExportShiro exportShiro = new ExportShiro<InnovateEnterpriseAchieveEntity>();

        Page<InnovateEnterpriseAchieveEntity> page = this.selectPage(
                new Query<InnovateEnterpriseAchieveEntity>(params).getPage(),
                exportShiro.queryExport(params, new EntityWrapper<InnovateEnterpriseAchieveEntity>()));

        return new PageUtils(page);
    }

    @Override
    public void delList(List<Long> list) {
        baseMapper.delList(list);
    }

    @Override
    public List<InnovateEnterpriseAchieveEntity> queryListByIds(Map<String, Object> params) {
//        return enterpAchieveIds.size()>0 ? this.selectBatchIds(enterpAchieveIds): this.selectList(null);
        return baseMapper.queryListByIds(params);
    }

    @Override
    public InnovateEnterpriseAchieveEntity selectListById(Long enterpAchieveId) {
        return baseMapper.selectListById(enterpAchieveId);
    }

}
