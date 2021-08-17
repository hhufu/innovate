package com.innovate.modules.sys.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.innovate.modules.sys.entity.SysMailCodeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 *
 * @author luo
 * @email 1275986528@qq.com
 * @date 2021-07-13 11:01:39
 */
@Mapper
public interface SysMailCodeDao extends BaseMapper<SysMailCodeEntity> {
    SysMailCodeEntity queryMailCode(SysMailCodeEntity sysMailCodeEntity);
}
