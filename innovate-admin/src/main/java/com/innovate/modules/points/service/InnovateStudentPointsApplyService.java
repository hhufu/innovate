package com.innovate.modules.points.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import com.innovate.modules.points.entity.InnovateStudentPointsApplyEntity;
import com.innovate.modules.points.entity.PointsApplyModel;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    boolean insertModel(PointsApplyModel applyModel);

    @Transactional
    boolean update(PointsApplyModel applyModel);

    R info(Long integralApplyId);
}

