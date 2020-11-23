package com.innovate.modules.enterprise.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseInfoService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 入驻企业表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@RestController
@RequestMapping("enterprise/innovateenterpriseinfo")
public class InnovateEnterpriseInfoController {
    @Autowired
    private InnovateEnterpriseInfoService innovateEnterpriseInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateEnterpriseInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{settledEnterpId}")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:info")
    public R info(@PathVariable("settledEnterpId") Long settledEnterpId){
		InnovateEnterpriseInfoEntity innovateEnterpriseInfo = innovateEnterpriseInfoService.selectById(settledEnterpId);

        return R.ok().put("innovateEnterpriseInfo", innovateEnterpriseInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:save")
    public R save(@RequestBody InnovateEnterpriseInfoEntity innovateEnterpriseInfo){
		innovateEnterpriseInfoService.insert(innovateEnterpriseInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:update")
    public R update(@RequestBody InnovateEnterpriseInfoEntity innovateEnterpriseInfo){
		innovateEnterpriseInfoService.updateById(innovateEnterpriseInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:delete")
    public R delete(@RequestBody Long[] settledEnterpIds){
		innovateEnterpriseInfoService.deleteBatchIds(Arrays.asList(settledEnterpIds));

        return R.ok();
    }

}
