package com.innovate.modules.points.dao;

import com.innovate.modules.points.entity.InnovateStudentPointsApplyEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生积分申请表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
@Mapper
public interface InnovateStudentPointsApplyDao extends BaseMapper<InnovateStudentPointsApplyEntity> {
    InnovateStudentPointsApplyEntity insertE(InnovateStudentPointsApplyEntity innovateStudentPointsApplyEntity);
}
