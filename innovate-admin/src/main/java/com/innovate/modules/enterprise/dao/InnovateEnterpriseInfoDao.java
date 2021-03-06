package com.innovate.modules.enterprise.dao;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 入驻企业表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@Mapper
public interface InnovateEnterpriseInfoDao extends BaseMapper<InnovateEnterpriseInfoEntity> {
    //伪删除
    void  delList(List<Long> list);
}
