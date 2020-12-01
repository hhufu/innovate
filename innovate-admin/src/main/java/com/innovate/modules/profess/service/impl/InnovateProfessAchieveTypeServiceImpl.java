package com.innovate.modules.profess.service.impl;

import com.innovate.modules.innovate.service.InnovateInstituteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;
import com.innovate.modules.profess.dao.InnovateProfessAchieveTypeDao;
import com.innovate.modules.profess.entity.InnovateProfessAchieveTypeEntity;
import com.innovate.modules.profess.service.InnovateProfessAchieveTypeService;


@Service
public class InnovateProfessAchieveTypeServiceImpl extends ServiceImpl<InnovateProfessAchieveTypeDao, InnovateProfessAchieveTypeEntity> implements InnovateProfessAchieveTypeService {
    @Autowired
    private InnovateProfessAchieveTypeService professachievetypeService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateProfessAchieveTypeEntity> page = this.selectPage(
                new Query<InnovateProfessAchieveTypeEntity>(params).getPage(),
                new EntityWrapper<InnovateProfessAchieveTypeEntity>()
                        .like(StringUtils.isNotBlank(params.get("professAchieveType").toString()), "profess_achieve_type", params.get("professAchieveType").toString())
        );

        return new PageUtils(page);
    }

    @Override
    public List<InnovateProfessAchieveTypeEntity> queryAllProfessAchieveType() {
        return baseMapper.queryAllProfessAchieveType();
    }

    @Override
    public void total(Map<String, Object> params) {
        InnovateProfessAchieveTypeEntity professachievetypeEntity = professachievetypeService.queryByProfessAchieveTypeId(params);

    }

    @Override
    public InnovateProfessAchieveTypeEntity queryByProfessAchieveTypeId(Map<String, Object> params) {
        return baseMapper.queryByProfessAchieveTypeId(params);
    }

}
