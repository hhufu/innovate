package com.innovate.modules.cooperation.service.impl;

import com.innovate.common.utils.R;
import com.innovate.modules.cooperation.entity.InnovateCooperationAttachModel;
import com.innovate.modules.cooperation.entity.InnovateCooperationMaterialsEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationMaterialsService;
import com.innovate.modules.points.entity.InnovateStudentPointsEntity;
import com.innovate.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.cooperation.dao.InnovateCooperationProjectsDao;
import com.innovate.modules.cooperation.entity.InnovateCooperationProjectsEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationProjectsService;


@Service("innovateCooperationProjectsService")
public class InnovateCooperationProjectsServiceImpl extends ServiceImpl<InnovateCooperationProjectsDao, InnovateCooperationProjectsEntity> implements InnovateCooperationProjectsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private InnovateCooperationMaterialsService innovateCooperationMaterialsService;
    @Autowired
    private InnovateCooperationProjectsDao innovateCooperationProjectsDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<InnovateCooperationProjectsEntity> entityWrapper = new EntityWrapper<>();
        if (params.get("projectName") != null) entityWrapper.like("project_name", params.get("projectName").toString());
        if (params.get("cooperationYear") != null) entityWrapper.eq("cooperation_year", params.get("cooperationYear").toString());
        if (params.get("isDel") != null) entityWrapper.eq("is_del", Integer.parseInt(params.get("isDel").toString()));
        if (params.get("instituteId") != null) entityWrapper.eq("institute_id", Integer.parseInt(params.get("instituteId").toString()));
        // 按时间倒序
        entityWrapper.orderBy("cooperation_id", false);
        Page<InnovateCooperationProjectsEntity> page = this.selectPage(
                new Query<InnovateCooperationProjectsEntity>(params).getPage(),
                entityWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteList(List<Long> list) {
        baseMapper.deleteList(list);
    }

    @Override
    public R insertModel(InnovateCooperationAttachModel attachModel) {
        InnovateCooperationProjectsEntity cooperationProjectsEntity = attachModel.getCooperationProjectsEntity();
        int i = baseMapper.insertE(cooperationProjectsEntity);
        if (attachModel.getCooperationMaterialsList() != null) {
            for (InnovateCooperationMaterialsEntity a : attachModel.getCooperationMaterialsList()) {
                a.setAttachTime(new Date());
                a.setFunctionId("A-"+cooperationProjectsEntity.getCooperationId());
                innovateCooperationMaterialsService.insert(a);
            }
        }
        if (i == 1)
            return R.ok();
        return R.error();
    }

    @Override
    public boolean update(InnovateCooperationAttachModel attachModel) {
        baseMapper.updateById(attachModel.getCooperationProjectsEntity());
        if (attachModel.getDelMaterialsList() != null)
            for (InnovateCooperationMaterialsEntity att : attachModel.getDelMaterialsList()) {
                if (att.getMaterialsId() != null) {// 删除附件
                    att.setIsDel(1);
                    innovateCooperationMaterialsService.updateById(att);
                }
            }
        if (attachModel.getCooperationMaterialsList() != null) // 修改或添加附件
            for (InnovateCooperationMaterialsEntity a : attachModel.getCooperationMaterialsList()) {
                a.setAttachTime(new Date());
                a.setFunctionId("A-"+attachModel.getCooperationProjectsEntity().getCooperationId());
                innovateCooperationMaterialsService.insertOrUpdate(a);
            }
        return false;
    }

    @Override
    public R info(Long cooperationId) {
        InnovateCooperationProjectsEntity innovateCooperationProjectsEntity = baseMapper.selectById(cooperationId);
        // 获取申请用户信息
        innovateCooperationProjectsEntity.setUserEntity(sysUserService.selectById(innovateCooperationProjectsEntity.getUserId()));
        // 获取申请附件信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("function_id", "A-" + cooperationId);
        map.put("is_del", 0);
        List<InnovateCooperationMaterialsEntity> materialsEntityList = innovateCooperationMaterialsService.selectByMap(map);
        return R.ok().put("innovateCooperationProjectsEntity", innovateCooperationProjectsEntity)
                .put("materialsEntityList", materialsEntityList);
    }

    @Override
    public List<InnovateCooperationProjectsEntity> queryListByIds(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        map.put("cooperationYear", null);
        if (params.get("ids") != null && !params.get("ids").toString().equals("[]")) {
            map.put("ids", params.get("ids"));
        } else {
            map.put("ids", null);
        }
        map.put("cooperationYear", params.get("cooperationYear"));
        map.put("instituteId", params.get("instituteId"));

        return baseMapper.selectCooperationYear(map);
    }
}
