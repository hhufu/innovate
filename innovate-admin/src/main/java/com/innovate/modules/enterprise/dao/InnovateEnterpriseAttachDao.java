package com.innovate.modules.enterprise.dao;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 企业附件表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@Mapper
public interface InnovateEnterpriseAttachDao extends BaseMapper<InnovateEnterpriseAttachEntity> {

    void delList(List<Long> list);
}
