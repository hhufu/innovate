package com.innovate.modules.points.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.points.entity.InnovateStudentPointsAttachEntity;
import com.innovate.modules.points.entity.PointsApplyModel;

import java.util.Map;

/**
 * 学生积分申请附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
public interface InnovateStudentPointsAttachService extends IService<InnovateStudentPointsAttachEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void delAttachLists(PointsApplyModel pointsApplyModel);
}

