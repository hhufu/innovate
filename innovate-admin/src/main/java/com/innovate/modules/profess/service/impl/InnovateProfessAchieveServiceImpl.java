package com.innovate.modules.profess.service.impl;

import com.innovate.common.utils.R;
import com.innovate.modules.points.entity.InnovateStudentPointsApplyEntity;
import com.innovate.modules.points.entity.InnovateStudentPointsAttachEntity;
import com.innovate.modules.points.entity.PointsApplyModel;
import com.innovate.modules.profess.entity.InnovateProfessAttachEntity;
import com.innovate.modules.profess.entity.ProfessModel;
import com.innovate.modules.profess.service.InnovateProfessAttachService;
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

import com.innovate.modules.profess.dao.InnovateProfessAchieveDao;
import com.innovate.modules.profess.entity.InnovateProfessAchieveEntity;
import com.innovate.modules.profess.service.InnovateProfessAchieveService;


@Service("innovateProfessAchieveService")
public class InnovateProfessAchieveServiceImpl extends ServiceImpl<InnovateProfessAchieveDao, InnovateProfessAchieveEntity> implements InnovateProfessAchieveService {
    @Autowired
    InnovateProfessAchieveDao professAchieveDao;
    @Autowired
    InnovateProfessAttachService professAttachService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InnovateProfessAchieveEntity> page = this.selectPage(
                new Query<InnovateProfessAchieveEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean insertModel(ProfessModel professModel) {
        InnovateProfessAchieveEntity professAchieveEntity = professModel.getProfessAchieveEntity();

        int i = professAchieveDao.insertE(professAchieveEntity);
        for (InnovateProfessAttachEntity p : professModel.getAttachEntityList()) {
            p.setAttachTime(new Date());
            p.setProfessAchieveId(professAchieveEntity.getProfessAchieveId());
            professAttachService.insert(p);
        }
        if (i == 1)
            return true;
        return false;
    }

    @Override
    public boolean update(ProfessModel professModel) {
        professAchieveDao.updateById(professModel.getProfessAchieveEntity());
        if (professModel.getDelAttachEntityList() != null)
            for (InnovateProfessAttachEntity att : professModel.getDelAttachEntityList()) {
                if (att.getAttachId() != null) {// 删除附件
                    att.setIsDel(1);
                    professAttachService.updateById(att);
                }
            }
        if (professModel.getAttachEntityList() != null) // 修改或添加附件
            for (InnovateProfessAttachEntity a : professModel.getAttachEntityList()) {
                a.setAttachTime(new Date());
                a.setProfessAchieveId(professModel.getProfessAchieveEntity().getProfessAchieveId());
                professAttachService.insertOrUpdate(a);
            }
        return false;
    }

    /**
     *
     * @param professAchieveIds
     * @return 导出
     */
    @Override
    public List<InnovateProfessAchieveEntity> queryListByIds(Long[] professAchieveIds) {
        Map<String, Object> map = new HashMap<>();
        if (professAchieveIds.length > 0) {
            map.put("professAchieveIds", professAchieveIds);
        } else {
            map.put("professAchieveIds", null);
        }

        return professAchieveDao.selectProfessAchieveIds(map);
    }

    @Override
    public R info(Long professAchieveId) {
        InnovateProfessAchieveEntity professAchieveEntity = professAchieveDao.selectById(professAchieveId);
        // 获取申请附件信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("profess_achieve_id", professAchieveId);
        map.put("is_del", 0);
        List<InnovateProfessAttachEntity> professAttachEntities = professAttachService.selectByMap(map);
        return R.ok().put("professAchieveEntity", professAchieveEntity)
                .put("professAttachEntities", professAttachEntities);
    }
}
