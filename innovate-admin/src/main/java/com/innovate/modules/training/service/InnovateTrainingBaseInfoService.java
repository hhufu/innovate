package com.innovate.modules.training.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.training.entity.InnovateTrainingBaseInfoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 实训基地信息
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
public interface InnovateTrainingBaseInfoService extends IService<InnovateTrainingBaseInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<InnovateTrainingBaseInfoEntity> queryListByDeptAndIds(Long[] trainBaseIds);
}

