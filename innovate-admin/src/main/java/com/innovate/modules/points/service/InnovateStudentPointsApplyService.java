package com.innovate.modules.points.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.points.entity.InnovateStudentPointsApplyEntity;

import java.util.Map;

/**
 * 学生积分申请表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
public interface InnovateStudentPointsApplyService extends IService<InnovateStudentPointsApplyEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

