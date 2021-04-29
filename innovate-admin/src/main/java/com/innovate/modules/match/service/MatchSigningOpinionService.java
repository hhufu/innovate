package com.innovate.modules.match.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.match.entity.MatchSigningOpinionEntity;

import java.util.Map;

/**
 * 双创赛事签署意见
 *
 * @author 
 * @email 
 * @date 2021-04-29 15:58:05
 */
public interface MatchSigningOpinionService extends IService<MatchSigningOpinionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addSigningOpinion(Map<String, Object> params);

    MatchSigningOpinionEntity querySigningOpinionByMatchIdAndType(Map<String, Object> params);

    void remove(Map<String, Object> params);
}

