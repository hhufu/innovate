package com.innovate.modules.cooperation.dao;

import com.innovate.modules.cooperation.entity.InnovateCooperationMaterialsEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 校政企合作附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@Mapper
public interface InnovateCooperationMaterialsDao extends BaseMapper<InnovateCooperationMaterialsEntity> {

    void deleteList(List<Long> list);
}
