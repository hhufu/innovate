package com.innovate.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.modules.enterprise.entity.InnovateAwardProjectTypeEntity;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;

import java.util.List;
import java.util.Map;

/**
 * 企业获奖项目类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
public interface InnovateAwardProjectTypeService extends IService<InnovateAwardProjectTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //导出
    List<InnovateAwardProjectTypeEntity> queryListByIds(List<Long> awardProjectjTypeIds);
}

