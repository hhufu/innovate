package com.innovate.modules.match.dao;

import com.innovate.modules.match.entity.MatchSigningOpinionEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 双创赛事签署意见
 *
 * @author 
 * @email 
 * @date 2021-04-29 15:58:05
 */
@Mapper
public interface MatchSigningOpinionDao extends BaseMapper<MatchSigningOpinionEntity> {

    MatchSigningOpinionEntity querySigningOpinionByMatchIdAndType(long matchId, int signType);
}
