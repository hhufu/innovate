package com.innovate.modules.cooperation.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.cooperation.entity.InnovateCooperationMaterialsEntity;
import com.innovate.modules.points.entity.InnovateStudentPointsAttachEntity;

import java.util.List;
import java.util.Map;

/**
 * 校政企合作附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
public interface InnovateCooperationMaterialsService extends IService<InnovateCooperationMaterialsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void deleteList(List<Long> asList);

}

