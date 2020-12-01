package com.innovate.modules.points.service.impl;

import com.innovate.common.utils.R;
import com.innovate.modules.finish.entity.FinishAttachEntity;
import com.innovate.modules.innovate.entity.UserPersonInfoEntity;
import com.innovate.modules.innovate.service.UserPerInfoService;
import com.innovate.modules.points.entity.InnovateStudentPointsAttachEntity;
import com.innovate.modules.points.entity.PointsApplyModel;
import com.innovate.modules.points.service.InnovateStudentPointsAttachService;
import com.innovate.modules.sys.entity.SysUserEntity;
import com.innovate.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.points.dao.InnovateStudentPointsApplyDao;
import com.innovate.modules.points.entity.InnovateStudentPointsApplyEntity;
import com.innovate.modules.points.service.InnovateStudentPointsApplyService;


@Service("innovateStudentPointsApplyService")
public class InnovateStudentPointsApplyServiceImpl extends ServiceImpl<InnovateStudentPointsApplyDao, InnovateStudentPointsApplyEntity> implements InnovateStudentPointsApplyService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private InnovateStudentPointsApplyDao innovateStudentPointsApplyDao;
    @Autowired
    private InnovateStudentPointsAttachService innovateStudentPointsAttachService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<InnovateStudentPointsApplyEntity> entityWrapper = new EntityWrapper<>();
        if (params.get("applyStatus") != null) entityWrapper.eq("apply_status", params.get("applyStatus"));
        //根据学号查询&&非管理员
        if (params.get("apply_user_id") != null) {
            SysUserEntity user = sysUserService.selectById(Long.parseLong(params.get("apply_user_id").toString()));
            entityWrapper.eq("apply_user_id", user.getUserId());
        }
        entityWrapper.orderBy("apply_time", false);
        Page<InnovateStudentPointsApplyEntity> page = this.selectPage(
                new Query<InnovateStudentPointsApplyEntity>(params).getPage(),
                entityWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public boolean insertModel(PointsApplyModel applyModel) {
        InnovateStudentPointsApplyEntity pointsApplyEntity = applyModel.getPointsApplyEntity();
        pointsApplyEntity.setApplyTime(new Date());
        int i = innovateStudentPointsApplyDao.insertE(pointsApplyEntity);
        for (InnovateStudentPointsAttachEntity a : applyModel.getPointsAttachEntityList()) {
            a.setAttachTime(new Date());
            a.setPointsApplyId(pointsApplyEntity.getIntegralApplyId());
            innovateStudentPointsAttachService.insert(a);
        }
        if (i == 1)
            return true;
        return false;
    }

    @Override
    public boolean update(PointsApplyModel applyModel) {
        innovateStudentPointsApplyDao.updateById(applyModel.getPointsApplyEntity());
        if (applyModel.getDelAttachLists() != null)
            for (InnovateStudentPointsAttachEntity att : applyModel.getDelAttachLists()) {
                if (att.getAttachId() != null) {// 删除附件
                    att.setIsDel(1);
                    innovateStudentPointsAttachService.updateById(att);
                }
            }
        if (applyModel.getPointsAttachEntityList() != null) // 修改或添加附件
            for (InnovateStudentPointsAttachEntity a : applyModel.getPointsAttachEntityList()) {
                a.setAttachTime(new Date());
                a.setPointsApplyId(applyModel.getPointsApplyEntity().getIntegralApplyId());
                innovateStudentPointsAttachService.insertOrUpdate(a);
            }
        return false;
    }

    @Override
    public R info(Long integralApplyId) {
        InnovateStudentPointsApplyEntity innovateStudentPointsApply = innovateStudentPointsApplyDao.selectById(integralApplyId);
        // 获取申请用户信息
        innovateStudentPointsApply.setUserEntity(sysUserService.selectById(innovateStudentPointsApply.getApplyUserId()));
        // 获取申请附件信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("points_apply_id", integralApplyId);
        map.put("is_del", 0);
        List<InnovateStudentPointsAttachEntity> attachEntityList = innovateStudentPointsAttachService.selectByMap(map);
        return R.ok().put("innovateStudentPointsApply", innovateStudentPointsApply)
                .put("attachEntityList", attachEntityList);
    }

}
