package com.innovate.modules.points.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.innovate.modules.points.entity.InnovateStudentPointsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Mikey
 * @email biaogejiushibiao@outlook.com
 * @date 2020-08-09 09:13:27
 */
@Mapper
public interface InnovateStudentPointsDao extends BaseMapper<InnovateStudentPointsEntity> {

    List<InnovateStudentPointsEntity> selectPointsIds(Map<String, Object> map);

    Integer queryCountPage(Map<String, Object> params);

    List<InnovateStudentPointsEntity> queryPageByMap(Map<String, Object> params);
}
