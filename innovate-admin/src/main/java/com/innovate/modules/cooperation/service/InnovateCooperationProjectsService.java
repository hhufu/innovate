package com.innovate.modules.cooperation.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import com.innovate.modules.cooperation.entity.InnovateCooperationAttachModel;
import com.innovate.modules.cooperation.entity.InnovateCooperationProjectsEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 校政企合作项目
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:21
 */
public interface InnovateCooperationProjectsService extends IService<InnovateCooperationProjectsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<InnovateCooperationProjectsEntity> queryListByIds(Map<String, Object> params);

    void deleteList(List<Long> asList);

    @Transactional
    boolean update(InnovateCooperationAttachModel attachModel);

    @Transactional
    R insertModel(InnovateCooperationAttachModel attachModel);

    R info(Long cooperationId);
}

