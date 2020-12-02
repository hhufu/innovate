package com.innovate.modules.cooperation.dao;

import com.innovate.modules.cooperation.entity.InnovateCooperationProjectsEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 校政企合作项目
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:21
 */
@Mapper
public interface InnovateCooperationProjectsDao extends BaseMapper<InnovateCooperationProjectsEntity> {

    void deleteList(List<Long> list);

    int insertE(InnovateCooperationProjectsEntity cooperationProjectsEntity);
}
