package com.innovate.modules.cooperation.service;

import com.baomidou.mybatisplus.service.IService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import com.innovate.modules.cooperation.entity.InnovateCooperationAgreementEntity;
import com.innovate.modules.cooperation.entity.InnovateCooperationAttachModel;
import com.innovate.modules.points.entity.PointsApplyModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 企业协议管理
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:20
 */
public interface InnovateCooperationAgreementService extends IService<InnovateCooperationAgreementEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<InnovateCooperationAgreementEntity> queryListByIds(Long[] cooperationagreementIds);

    void deleteList(List<Long> asList);

    @Transactional
    R insertModel(InnovateCooperationAttachModel attachModel);

    @Transactional
    boolean update(InnovateCooperationAttachModel attachModel);

    R info(Long integralApplyId);
}

