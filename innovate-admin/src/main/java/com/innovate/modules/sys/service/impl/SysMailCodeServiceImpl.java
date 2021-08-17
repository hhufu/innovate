package com.innovate.modules.sys.service.impl;

import com.innovate.modules.sys.dao.SysMailCodeDao;
import com.innovate.modules.sys.entity.SysMailCodeEntity;
import com.innovate.modules.sys.service.SysMailCodeService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;




@Service("sysMailCodeService")
public class SysMailCodeServiceImpl extends ServiceImpl<SysMailCodeDao, SysMailCodeEntity> implements SysMailCodeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SysMailCodeEntity> page = this.selectPage(
                new Query<SysMailCodeEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public SysMailCodeEntity queryMailCode(SysMailCodeEntity sysMailCodeEntity) {
        return baseMapper.queryMailCode(sysMailCodeEntity);
    }

}
