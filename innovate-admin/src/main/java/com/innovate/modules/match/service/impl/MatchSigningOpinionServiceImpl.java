package com.innovate.modules.match.service.impl;

import com.innovate.modules.match.service.MatchApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.match.dao.MatchSigningOpinionDao;
import com.innovate.modules.match.entity.MatchSigningOpinionEntity;
import com.innovate.modules.match.service.MatchSigningOpinionService;


@Service("innovateMatchSigningOpinionService")
public class MatchSigningOpinionServiceImpl extends ServiceImpl<MatchSigningOpinionDao, MatchSigningOpinionEntity> implements MatchSigningOpinionService {

    @Autowired
    private MatchApplyService matchApplyService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MatchSigningOpinionEntity> page = this.selectPage(
                new Query<MatchSigningOpinionEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

    /**
     * 添加签署意见
     * @param params
     */
    @Override
    public void addSigningOpinion(Map<String, Object> params) {
        MatchSigningOpinionEntity matchSigningOpinionEntity = new MatchSigningOpinionEntity();

        matchSigningOpinionEntity.setSigningOpinion(params.get("sighingOpinion").toString());

        matchSigningOpinionEntity.setMatchId(Long.parseLong(params.get("matchId").toString()));

        matchSigningOpinionEntity.setUserId(Long.parseLong(params.get("userId").toString()));

        matchSigningOpinionEntity.setSignType((long) Integer.parseInt(params.get("signType").toString()));

        matchSigningOpinionEntity.setSigningOpinionTime(new Date());

        this.insert(matchSigningOpinionEntity);

        matchApplyService.apply(params);

    }

    @Override
    public MatchSigningOpinionEntity querySigningOpinionByMatchIdAndType(Map<String, Object> params) {
        return baseMapper.querySigningOpinionByMatchIdAndType(Long.parseLong(params.get("matchId").toString()), Integer.parseInt(params.get("signType").toString()));
    }

    @Override
    public void remove(Map<String, Object> params) {

        this.deleteByMap(params);

    }

}
