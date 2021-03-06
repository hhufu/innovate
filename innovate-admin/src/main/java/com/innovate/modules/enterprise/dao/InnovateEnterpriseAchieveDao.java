package com.innovate.modules.enterprise.dao;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseAchieveEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 企业成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:32
 */
@Mapper
public interface InnovateEnterpriseAchieveDao extends BaseMapper<InnovateEnterpriseAchieveEntity> {
    void  delList(List<Long> list);

    List<InnovateEnterpriseAchieveEntity> queryListByIds(Map<String, Object> params);

    InnovateEnterpriseAchieveEntity selectListById(Long enterpAchieveId);
}
