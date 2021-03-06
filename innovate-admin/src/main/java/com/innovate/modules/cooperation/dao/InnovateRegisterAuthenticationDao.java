package com.innovate.modules.cooperation.dao;

import com.innovate.modules.cooperation.entity.InnovateRegisterAuthenticationEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 企业登记认证表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@Mapper
public interface InnovateRegisterAuthenticationDao extends BaseMapper<InnovateRegisterAuthenticationEntity> {
    /**
     * 获得企业ID
     */
    InnovateRegisterAuthenticationEntity queryById(Long authenticationId);
    /**
     * 删除企业登记信息
     */
    void deleteList(List<Long> list);

    List<InnovateRegisterAuthenticationEntity> selectMap(Map<String, Object> map);

    void deleteAgreementList(List<Long> list);

    void deleteProjectsList(List<Long> list);
}
