package com.innovate.modules.enterprise.service.impl;

import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;
import com.innovate.modules.sys.entity.SysUserEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.enterprise.dao.InnovateEnterpriseInfoDao;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseInfoService;


@Service("innovateEnterpriseInfoService")
public class InnovateEnterpriseInfoServiceImpl extends ServiceImpl<InnovateEnterpriseInfoDao, InnovateEnterpriseInfoEntity> implements InnovateEnterpriseInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //企业名称 搜索信息
        Object enterpriseName = params.get("enterpriseName");
        //用户id
        Object userId = params.get("enterpriseUserId");
        //学院id
        Object instituteId = params.get("instituteId");
        EntityWrapper<InnovateEnterpriseInfoEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del", 0).eq("apply_status",params.get("apply_status"));

        if (userId!=null ){ //空为管理员 不执行
            if (instituteId!=null){   //非空 二级学院管理员
                wrapper.eq("institute_id", instituteId.toString());
            }else {  //无管理权限 只获取自己得信息
                wrapper.eq("enterprise_user_id", userId.toString());
            }
        }
        if (!"".equals(enterpriseName)){  //根据企业名称搜索
            wrapper.like("enterprise_name" , enterpriseName.toString());
        }
        Page<InnovateEnterpriseInfoEntity> page = this.selectPage(
                new Query<InnovateEnterpriseInfoEntity>(params).getPage(),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void delList(List<Long> list) {
        baseMapper.delList(list);
    }

    @Override
    public List<InnovateEnterpriseInfoEntity> queryListByIds(List<Long> trainBaseIds) {
        return trainBaseIds.size() > 0 ? this.selectBatchIds(trainBaseIds)
                : this.selectList(new EntityWrapper<InnovateEnterpriseInfoEntity>().eq("is_del", 0));
    }

}
