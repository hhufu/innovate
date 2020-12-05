package com.innovate.modules.cooperation.dao;

import com.innovate.modules.cooperation.entity.InnovateCooperationAgreementEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 企业协议管理
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:20
 */
@Mapper
public interface InnovateCooperationAgreementDao extends BaseMapper<InnovateCooperationAgreementEntity> {

    void deleteList(List<Long> list);

    int insertE(InnovateCooperationAgreementEntity cooperationAgreementEntity);

    List<InnovateCooperationAgreementEntity> selectAgreementYear(Map<String, Object> map);
}
