package com.innovate.modules.match.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.modules.match.entity.MatchAttachEntity;

import java.util.List;
import java.util.Map;

/**
 */
public interface MatchAttachService extends IService<MatchAttachEntity> {

    List<MatchAttachEntity> queryMatchByTime(Map<String, Object> params);

    List<MatchAttachEntity> queryAll(Map<String, Object> params);

    void remove(Map<String, Object> params);

    Integer queryTotal(Map<String, Object> params);
}
