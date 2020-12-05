package com.innovate.modules.training.dao;

import com.innovate.modules.training.entity.InnovateTrainingBaseInfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 实训基地信息
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@Mapper
public interface InnovateTrainingBaseInfoDao extends BaseMapper<InnovateTrainingBaseInfoEntity> {
    int deleteList(List<Long> asList);
}
