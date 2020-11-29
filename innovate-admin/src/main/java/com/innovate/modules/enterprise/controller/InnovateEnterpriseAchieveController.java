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

import com.innovate.modules.enterprise.entity.InnovateEnterpriseAchieveEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAchieveService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 企业成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:32
 */
@RestController
@RequestMapping("enterprise/innovateenterpriseachieve")
public class InnovateEnterpriseAchieveController {
    @Autowired
    private InnovateEnterpriseAchieveService innovateEnterpriseAchieveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateEnterpriseAchieveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{enterpAchieveId}")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:info")
    public R info(@PathVariable("enterpAchieveId") Long enterpAchieveId){
		InnovateEnterpriseAchieveEntity innovateEnterpriseAchieve = innovateEnterpriseAchieveService.selectById(enterpAchieveId);
        return R.ok().put("innovateEnterpriseAchieve", innovateEnterpriseAchieve);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:save")
    public R save(@RequestBody InnovateEnterpriseAchieveEntity innovateEnterpriseAchieve){
		innovateEnterpriseAchieveService.insert(innovateEnterpriseAchieve);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:update")
    public R update(@RequestBody InnovateEnterpriseAchieveEntity innovateEnterpriseAchieve){
		innovateEnterpriseAchieveService.updateById(innovateEnterpriseAchieve);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:delete")
    public R delete(@RequestBody Long[] enterpAchieveIds){
//		innovateEnterpriseAchieveService.deleteBatchIds(Arrays.asList(enterpAchieveIds));

        innovateEnterpriseAchieveService.delList(Arrays.asList(enterpAchieveIds));
        return R.ok();
    }

}
