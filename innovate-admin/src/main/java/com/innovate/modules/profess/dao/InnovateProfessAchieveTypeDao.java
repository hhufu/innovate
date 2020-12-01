package com.innovate.modules.profess.dao;

import com.innovate.modules.innovate.entity.InnovateInstituteEntity;
import com.innovate.modules.profess.entity.InnovateProfessAchieveTypeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 专创成果类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@Mapper
public interface InnovateProfessAchieveTypeDao extends BaseMapper<InnovateProfessAchieveTypeEntity> {

    InnovateProfessAchieveTypeEntity queryByProfessAchieveTypeId(Map<String, Object> params);

    List<InnovateProfessAchieveTypeEntity> queryAllProfessAchieveType();

}
