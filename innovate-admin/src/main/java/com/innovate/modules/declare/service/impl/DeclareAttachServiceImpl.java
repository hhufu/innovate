package com.innovate.modules.declare.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.modules.declare.dao.DeclareAttachDao;
import com.innovate.modules.declare.entity.DeclareAttachEntity;
import com.innovate.modules.declare.entity.DeclareInfoModel;
import com.innovate.modules.declare.service.DeclareAttachService;
import com.innovate.modules.finish.entity.FinishAttachEntity;
import com.innovate.modules.finish.entity.FinishInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class DeclareAttachServiceImpl extends ServiceImpl<DeclareAttachDao, DeclareAttachEntity> implements DeclareAttachService {

    @Autowired
    private DeclareAttachService declareAttachService;

    @Override
    public List<DeclareAttachEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public void remove(Map<String, Object> params) {
        baseMapper.remove(params);
    }

    @Override
    public void delAttachLists(DeclareInfoModel declareInfoModel) {
        if (declareInfoModel.getDelAttachLists() != null) {
            for (DeclareAttachEntity a: declareInfoModel.getDelAttachLists()) {
                if (a.getAttachId() != null) {
                    a.setIsDel((long) 1);
                    declareAttachService.updateById(a);
                }
            }

        }
    }

    @Override
    public List<DeclareAttachEntity> queryAllAttach(Map<String, Object> params) {
        return baseMapper.queryAllAttach(params);
    }
}
