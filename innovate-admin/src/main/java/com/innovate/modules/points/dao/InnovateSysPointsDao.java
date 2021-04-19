package com.innovate.modules.points.dao;

import com.innovate.modules.points.entity.InnovateSysPointsEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 积分标准
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
@Mapper
public interface InnovateSysPointsDao extends BaseMapper<InnovateSysPointsEntity> {

    int deleteList(List<Long> asList);

    List selectPoints(Map<String, Object> params);
}
