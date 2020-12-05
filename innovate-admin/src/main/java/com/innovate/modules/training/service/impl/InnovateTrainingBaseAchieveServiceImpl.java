package com.innovate.modules.training.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.innovate.common.utils.R;
import com.innovate.modules.innovate.entity.InnovateInstituteEntity;
import com.innovate.modules.innovate.service.InnovateInstituteService;
import com.innovate.modules.sys.service.SysUserService;
import com.innovate.modules.training.entity.InnovateTrainingBaseAttachEntity;
import com.innovate.modules.training.entity.InnovateTrainingBaseAttachModel;
import com.innovate.modules.training.service.InnovateTrainingBaseAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.training.dao.InnovateTrainingBaseAchieveDao;
import com.innovate.modules.training.entity.InnovateTrainingBaseAchieveEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseAchieveService;


@Service("innovateTrainingBaseAchieveService")
public class InnovateTrainingBaseAchieveServiceImpl extends ServiceImpl<InnovateTrainingBaseAchieveDao, InnovateTrainingBaseAchieveEntity> implements InnovateTrainingBaseAchieveService {

    @Autowired
    private InnovateTrainingBaseAchieveDao innovateTrainingBaseAchieveDao;
    @Autowired
    private InnovateTrainingBaseAttachService innovateTrainingBaseAttachService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private InnovateInstituteService innovateInstituteService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<InnovateTrainingBaseAchieveEntity> entityEntityWrapper = new EntityWrapper<>();
        if (params.get("trainingBaseName") != null) entityEntityWrapper.like("training_base_name", params.get("trainingBaseName").toString());
        Page<InnovateTrainingBaseAchieveEntity> page = this.selectPage(
                new Query<InnovateTrainingBaseAchieveEntity>(params).getPage(),
                entityEntityWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 导出
     * @param trainAchiveIds
     * @return
     */
    @Override
    public List<InnovateTrainingBaseAchieveEntity> queryListByIds(Long[] trainAchiveIds) {
        List<InnovateTrainingBaseAchieveEntity> baseAchieveEntities = new ArrayList<>();
        if (trainAchiveIds.length > 0) {
            baseAchieveEntities = this.selectBatchIds(Arrays.asList(trainAchiveIds));
        } else {
            EntityWrapper<InnovateTrainingBaseAchieveEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("is_del", 0);
            baseAchieveEntities = this.selectList(wrapper);
        }
        for (InnovateTrainingBaseAchieveEntity b : baseAchieveEntities) {
            InnovateInstituteEntity innovateInstituteEntity = innovateInstituteService.selectById(b.getInstituteId());
            if (innovateInstituteEntity.getInstituteName() != null)
                b.setInstituteName(innovateInstituteEntity.getInstituteName());
        }

        return baseAchieveEntities;
    }

    @Override
    public R insertModel(InnovateTrainingBaseAttachModel attachModel) {
        InnovateTrainingBaseAchieveEntity trainingBaseAchieveEntity = attachModel.getTrainingBaseAchieveEntity();
        int i = innovateTrainingBaseAchieveDao.insertE(trainingBaseAchieveEntity);
        if (attachModel.getTrainingBaseAttachList() != null) {
            for (InnovateTrainingBaseAttachEntity a : attachModel.getTrainingBaseAttachList()) {
                a.setAttachTime(new Date());
                a.setTrainingAchieveId(trainingBaseAchieveEntity.getTrainingAchieveId());
                innovateTrainingBaseAttachService.insert(a);
            }
        }
        if (i == 1)
            return R.ok();
        return R.error();
    }

    @Override
    public boolean update(InnovateTrainingBaseAttachModel attachModel) {
        innovateTrainingBaseAchieveDao.updateById(attachModel.getTrainingBaseAchieveEntity());
        if (attachModel.getDelBaseAttachList() != null)
            for (InnovateTrainingBaseAttachEntity att : attachModel.getDelBaseAttachList()) {
                if (att.getAttachId() != null) {// 删除附件
                    att.setIsDel(1);
                    innovateTrainingBaseAttachService.updateById(att);
                }
            }
        if (attachModel.getTrainingBaseAttachList() != null) // 修改或添加附件
            for (InnovateTrainingBaseAttachEntity a : attachModel.getTrainingBaseAttachList()) {
                a.setAttachTime(new Date());
                a.setTrainingAchieveId(attachModel.getTrainingBaseAchieveEntity().getTrainingAchieveId());
                innovateTrainingBaseAttachService.insertOrUpdate(a);
            }
        return false;
    }

    @Override
    public R info(Long trainingAchieveId) {
        InnovateTrainingBaseAchieveEntity innovateTrainingBaseAchieveEntity = innovateTrainingBaseAchieveDao.selectById(trainingAchieveId);
        // 获取申请附件信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("training_achieve_id", trainingAchieveId);
        map.put("is_del", 0);
        List<InnovateTrainingBaseAttachEntity> attachEntityList = innovateTrainingBaseAttachService.selectByMap(map);
        return R.ok().put("innovateTrainingBaseAchieveEntity", innovateTrainingBaseAchieveEntity)
                .put("attachEntityList", attachEntityList);
    }

    @Override
    public int deleteList(List<Long> asList) {
        return baseMapper.deleteList(asList);
    }

}
