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

import com.innovate.modules.enterprise.dao.InnovateEnterpriseProjectDao;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseProjectService;


@Service("innovateEnterpriseProjectService")
public class InnovateEnterpriseProjectServiceImpl extends ServiceImpl<InnovateEnterpriseProjectDao, InnovateEnterpriseProjectEntity> implements InnovateEnterpriseProjectService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Page<InnovateEnterpriseProjectEntity> page = this.selectPage(
                new Query<InnovateEnterpriseProjectEntity>(params).getPage(),
                queryExport(params)
        );

        return new PageUtils(page);
    }

    @Override
    public void delList(List<Long> list) {
        baseMapper.delList(list);
    }

    @Override
    public List<InnovateEnterpriseProjectEntity> queryListByIds(List<Long> enterpProjIds, Map<String, Object> params) {

        return enterpProjIds.size() > 0 ? this.selectBatchIds(enterpProjIds)
                : this.selectList(queryExport(params));
    }

    //权限判断
    public EntityWrapper<InnovateEnterpriseProjectEntity> queryExport(Map<String, Object> params){
        // 用户id
        Object userId = params.get("enterpriseUserId");
        //学院id
        Object instituteId = params.get("instituteId");
        //项目名称
        Object projectName = params.get("project_name");
        //项目年度
        Object projectYear = params.get("projectYear");
        //审核状态
        Object applyStatus = params.get("applyStatus");
        EntityWrapper<InnovateEnterpriseProjectEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del", 0)
                .eq("apply_status", params.get("apply_status"));
        if (instituteId!=null)
           wrapper.eq("institute_id", Long.parseLong(instituteId.toString()));
        if (userId != null) { //非空无管理权限
//            if (instituteId!=null){
//                wrapper.eq("institute_id", instituteId.toString());
//            }else {
            wrapper.eq("project_user_id", userId.toString());
//            }
        }

        if (projectName != null && !"".equals(projectName)) { //根据项目名称搜索
            wrapper.like("project_name", projectName.toString());
        }

        if (projectYear != null) { //根据年份搜索
            wrapper.eq("project_year", projectYear.toString());
        }

        if (applyStatus != null) {
            wrapper.eq("apply_status", applyStatus.toString());
        }
    return wrapper;
    }
}
