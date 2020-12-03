package com.innovate.modules.profess.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import com.innovate.modules.profess.entity.InnovateProfessAchieveEntity;
import com.innovate.modules.profess.entity.ProfessModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 专创成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
public interface InnovateProfessAchieveService extends IService<InnovateProfessAchieveEntity> {

    PageUtils queryPage(Map<String, Object> params);

    @Transactional
    boolean insertModel(ProfessModel professModel);

    R info(Long professAchieveId);

    @Transactional
    boolean update(ProfessModel professModel);
}

