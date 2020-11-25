package com.innovate.modules.points.service.impl;

import com.innovate.modules.innovate.entity.UserPersonInfoEntity;
import com.innovate.modules.innovate.service.UserPerInfoService;
import com.innovate.modules.sys.entity.SysUserEntity;
import com.innovate.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<InnovateStudentPointsApplyEntity> entityWrapper = new EntityWrapper<>();

        //根据学号查询&&非管理员
        if (params.get("apply_user_id")!=null){
            SysUserEntity user = sysUserService.selectById(Long.parseLong(params.get("apply_user_id").toString()));
            entityWrapper.eq("apply_user_id",user.getUserId());
        }
        Page<InnovateStudentPointsApplyEntity> page = this.selectPage(
                new Query<InnovateStudentPointsApplyEntity>(params).getPage(),
                entityWrapper
        );
        return new PageUtils(page);
    }

}
