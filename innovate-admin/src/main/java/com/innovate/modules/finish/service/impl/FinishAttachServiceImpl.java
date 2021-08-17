package com.innovate.modules.finish.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoModel;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAttachService;
import com.innovate.modules.finish.entity.FinishAttachEntity;
import com.innovate.modules.finish.dao.FinishAttachDao;
import com.innovate.modules.finish.entity.FinishInfoModel;
import com.innovate.modules.finish.service.FinishAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Mikey
 * @Title:
 * @Description:
 * @Email:1625017540@qq.com
 * @date 2018/11/19 18:47
 * @Version 1.0
 */
@Service
public class FinishAttachServiceImpl extends ServiceImpl<FinishAttachDao, FinishAttachEntity> implements FinishAttachService {
    @Autowired
    private InnovateEnterpriseAttachService innovateEnterpriseAttachService;
    @Autowired
    private FinishAttachService finishAttachService;


    @Override
    public List<FinishAttachEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public void remove(Map<String, Object> params) {
        baseMapper.remove(params);
    }

    @Override
    public void delAttachLists(InnovateEnterpriseInfoModel innovateEnterpriseInfoModel) {
        if (innovateEnterpriseInfoModel.getDelAttachLists() != null) {
            for (InnovateEnterpriseAttachEntity a: innovateEnterpriseInfoModel.getDelAttachLists()) {
                if (a.getAttachId() != null) {
                    a.setIsDel(1);
                    innovateEnterpriseAttachService.updateById(a);
                }
            }

        }
    }

    @Transactional
    @Override
    public void delAttachLists(FinishInfoModel finishInfoModel) {
        if (finishInfoModel.getDelAttachLists() != null) {
            for (FinishAttachEntity a: finishInfoModel.getDelAttachLists()) {
                if (a.getAttachId() != null) {
                    a.setIsDel((long) 1);
                    finishAttachService.updateById(a);
                }
            }

        }
    }

    @Override
    public List<FinishAttachEntity> queryAllAttach(Map<String, Object> params) {
        return baseMapper.queryAllAttach(params);
    }
}
