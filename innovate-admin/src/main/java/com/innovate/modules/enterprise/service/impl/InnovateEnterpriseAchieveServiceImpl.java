package com.innovate.modules.enterprise.service.impl;

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
        //获奖名称 搜索信息
        Object awardProjectName = params.get("awardProjectName");

        //用户id
        Object userId = params.get("enterpriseUserId");

        //学院id
        Object instituteId = params.get("instituteId");

        EntityWrapper<InnovateEnterpriseAchieveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del", 0)
                .eq("apply_status",params.get("apply_status"));

        if (userId!=null ){ //空为管理员 不执行
            if (instituteId != null){   //非空 二级学院管理员
                wrapper.eq("institute_id", instituteId.toString());
            }else {  //无管理权限 只获取自己得信息
                wrapper.eq("enterprise_user_id", userId.toString());
            }
        }

        if (!"".equals(awardProjectName)){  //根据获奖名称搜索
            wrapper.like("award_project_name" , awardProjectName.toString());
        }

        Page<InnovateEnterpriseAchieveEntity> page = this.selectPage(
                new Query<InnovateEnterpriseAchieveEntity>(params).getPage(), wrapper);

        return new PageUtils(page);
    }

    @Override
    public void delList(List<Long> list) {
        baseMapper.delList(list);
    }

    @Override
    public List<InnovateEnterpriseAchieveEntity> queryListByIds(Long enterpAchieveIds) {

//        return enterpAchieveIds.size()>0 ? this.selectBatchIds(enterpAchieveIds): this.selectList(null);
        return baseMapper.queryListByIds(enterpAchieveIds);
    }

    @Override
    public InnovateEnterpriseAchieveEntity selectListById(Long enterpAchieveId) {
        return baseMapper.selectListById(enterpAchieveId);
    }

}
