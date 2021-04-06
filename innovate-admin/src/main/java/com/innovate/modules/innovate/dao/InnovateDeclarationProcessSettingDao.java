package com.innovate.modules.innovate.dao;

import com.innovate.modules.innovate.entity.InnovateDeclarationProcessSettingEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.Map;

/**
 * 申报流程设置
 *
 * @author HHUFU
 * @email 437213666@qq.com
 * @date 2021-02-03 11:43:20
 */
@Mapper
public interface InnovateDeclarationProcessSettingDao extends BaseMapper<InnovateDeclarationProcessSettingEntity> {

    int queryCount(Map<String, Object> params);

    int queryStartTime(Map<String, Object> params);

    int queryEndTime(Map<String, Object> params);

    InnovateDeclarationProcessSettingEntity selectByTime(Map<String, Object> params);
}
