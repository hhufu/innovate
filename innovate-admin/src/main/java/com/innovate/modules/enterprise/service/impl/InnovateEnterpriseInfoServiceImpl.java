package com.innovate.modules.enterprise.service.impl;
import com.innovate.modules.enterprise.utility.ExportShiro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;
import com.innovate.modules.enterprise.dao.InnovateEnterpriseInfoDao;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseInfoService;


@Service("innovateEnterpriseInfoService")
public class InnovateEnterpriseInfoServiceImpl extends ServiceImpl<InnovateEnterpriseInfoDao, InnovateEnterpriseInfoEntity> implements InnovateEnterpriseInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        ExportShiro exportShiro = new ExportShiro<InnovateEnterpriseInfoEntity>();

        Page<InnovateEnterpriseInfoEntity> page = this.selectPage(
                new Query<InnovateEnterpriseInfoEntity>(params).getPage(),
                exportShiro.queryExport(params, new EntityWrapper<InnovateEnterpriseInfoEntity>()));
        return new PageUtils(page);
    }

    @Override
    public void delList(List<Long> list) {
        baseMapper.delList(list);
    }

    @Override
    public List<InnovateEnterpriseInfoEntity> queryListByIds(List<Long> trainBaseIds, Map<String, Object> params) {

        ExportShiro exportShiro = new ExportShiro<InnovateEnterpriseInfoEntity>();
        return trainBaseIds.size() > 0 ? this.selectBatchIds(trainBaseIds)
                : this.selectList(exportShiro.queryExport(params, new EntityWrapper<InnovateEnterpriseInfoEntity>()));
    }

}
