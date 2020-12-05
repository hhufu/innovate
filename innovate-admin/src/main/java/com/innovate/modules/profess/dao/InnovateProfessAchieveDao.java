package com.innovate.modules.profess.dao;

import com.innovate.modules.profess.entity.InnovateProfessAchieveEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 专创成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@Mapper
public interface InnovateProfessAchieveDao extends BaseMapper<InnovateProfessAchieveEntity> {

    int insertE(InnovateProfessAchieveEntity professAchieveEntity);

    List<InnovateProfessAchieveEntity> selectProfessAchieveIds(Map<String, Object> map);

    int deleteList(List<Long> asList);
}
