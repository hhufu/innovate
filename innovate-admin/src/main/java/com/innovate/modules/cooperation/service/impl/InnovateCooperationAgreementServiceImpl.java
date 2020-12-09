package com.innovate.modules.cooperation.service.impl;

import com.innovate.common.utils.R;
import com.innovate.modules.cooperation.entity.InnovateCooperationAttachModel;
import com.innovate.modules.cooperation.entity.InnovateCooperationMaterialsEntity;
import com.innovate.modules.cooperation.entity.InnovateCooperationProjectsEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationMaterialsService;
import com.innovate.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.cooperation.dao.InnovateCooperationAgreementDao;
import com.innovate.modules.cooperation.entity.InnovateCooperationAgreementEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationAgreementService;


@Service("innovateCooperationAgreementService")
public class InnovateCooperationAgreementServiceImpl extends ServiceImpl<InnovateCooperationAgreementDao, InnovateCooperationAgreementEntity> implements InnovateCooperationAgreementService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private InnovateCooperationMaterialsService innovateCooperationMaterialsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<InnovateCooperationAgreementEntity> entityWrapper = new EntityWrapper<>();
        if (params.get("enterpriseName") != null) entityWrapper.like("enterprise_name", params.get("enterpriseName").toString());
        if (params.get("agreementYear") != null) entityWrapper.eq("agreement_year", params.get("agreementYear").toString());
        if (params.get("isDel") != null) entityWrapper.eq("is_del", Integer.parseInt(params.get("isDel").toString()));
        if (params.get("instituteId") != null) entityWrapper.eq("institute_id", Integer.parseInt(params.get("instituteId").toString()));
        // 按时间倒序
        entityWrapper.orderBy("enterprise_id", false);
        Page<InnovateCooperationAgreementEntity> page = this.selectPage(
                new Query<InnovateCooperationAgreementEntity>(params).getPage(),
                entityWrapper
        );

        return new PageUtils(page);
    }


    @Override
    public List<InnovateCooperationAgreementEntity> queryListByIds(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        if (params.get("ids") != null && !params.get("ids").toString().equals("[]")) {
            map.put("ids", params.get("ids"));
        } else {
            map.put("ids", null);
        }
        map.put("agreementYear", params.get("agreementYear"));
        map.put("instituteId", params.get("instituteId"));
        map.put("enterpriseName", params.get("enterpriseName"));

        return baseMapper.selectAgreementYear(map);
    }

    @Override
    public void deleteList(List<Long> list) {
        baseMapper.deleteList(list);
    }

    @Override
    public R insertModel(InnovateCooperationAttachModel attachModel) {
        InnovateCooperationAgreementEntity cooperationAgreementEntity = attachModel.getCooperationAgreementEntity();
        int i = baseMapper.insertE(cooperationAgreementEntity);
        if (attachModel.getCooperationMaterialsList() != null) {
            for (InnovateCooperationMaterialsEntity a : attachModel.getCooperationMaterialsList()) {
                a.setAttachTime(new Date());
                a.setFunctionId("P-"+cooperationAgreementEntity.getEnterpriseId());
                innovateCooperationMaterialsService.insert(a);
            }
        }
        if (i == 1)
            return R.ok();
        return R.error();
    }

    @Override
    public boolean update(InnovateCooperationAttachModel attachModel) {
        baseMapper.updateById(attachModel.getCooperationAgreementEntity());
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
                a.setFunctionId("P-"+attachModel.getCooperationAgreementEntity().getEnterpriseId());
                innovateCooperationMaterialsService.insertOrUpdate(a);
            }
        return false;
    }
    @Override
    public R info(Long enterpriseId) {
        InnovateCooperationAgreementEntity innovateCooperationAgreement = baseMapper.selectById(enterpriseId);
        // 获取申请用户信息
        innovateCooperationAgreement.setUserEntity(sysUserService.selectById(innovateCooperationAgreement.getUserId()));
        // 获取申请附件信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("function_id", "P-" + enterpriseId);
        map.put("is_del", 0);
        List<InnovateCooperationMaterialsEntity> materialsEntityList = innovateCooperationMaterialsService.selectByMap(map);
        return R.ok().put("innovateCooperationAgreement", innovateCooperationAgreement)
                .put("materialsEntityList", materialsEntityList);
    }

}
