package com.innovate.modules.points.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.points.entity.InnovateSysPointsEntity;

import java.util.List;
import java.util.Map;

/**
 * 积分标准
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
public interface InnovateSysPointsService extends IService<InnovateSysPointsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    int deleteList(List<Long> asList);
}

