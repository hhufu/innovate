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
        // 根据申请状态查询
        if (params.get("applyStatus") != null) entityWrapper.eq("apply_status", params.get("applyStatus"));
        // 查詢没删除的记录
        if (params.get("isDel") != null) entityWrapper.eq("is_del", params.get("isDel"));
        // 过滤不通过的记录
        if (params.get("noPass") != null) entityWrapper.gt("apply_status", -1);
        // 过滤未提交的记录
        if (params.get("noApply") != null) entityWrapper.notIn("apply_status", 0);
        //根据用户id查询&&非管理员
        if (params.get("apply_user_id") != null) {
            entityWrapper.eq("apply_user_id", Long.parseLong(params.get("apply_user_id").toString()));
        } else { // 管理员
            if (params.get("noPass") != null) entityWrapper.lt("apply_status", 1);
            entityWrapper.or("adminUserId", Long.parseLong(params.get("apply_user_id").toString()));
        }
        // 根据学号查询
        if (params.get("stuNum") != null) entityWrapper.like("stu_num", params.get("stuNum").toString());
        // 根据申请类型查询
        if (params.get("sysPointsId") != null) entityWrapper.eq("sys_points_id", Long.parseLong(params.get("sysPointsId").toString()));
        // 按学院查询
        if (params.get("instituteId") != null) entityWrapper.eq("institute_id", Long.parseLong(params.get("instituteId").toString()));
        // 按时间倒序
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
