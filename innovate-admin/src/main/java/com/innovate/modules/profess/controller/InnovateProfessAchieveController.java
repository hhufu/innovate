package com.innovate.modules.profess.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.modules.profess.entity.InnovateProfessAchieveEntity;
import com.innovate.modules.profess.service.InnovateProfessAchieveService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 专创成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@RestController
@RequestMapping("profess/innovateprofessachieve")
public class InnovateProfessAchieveController {
    @Autowired
    private InnovateProfessAchieveService innovateProfessAchieveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("profess:innovateprofessachieve:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateProfessAchieveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{professAchieveId}")
    @RequiresPermissions("profess:innovateprofessachieve:info")
    public R info(@PathVariable("professAchieveId") Long professAchieveId){
		InnovateProfessAchieveEntity innovateProfessAchieve = innovateProfessAchieveService.selectById(professAchieveId);

        return R.ok().put("innovateProfessAchieve", innovateProfessAchieve);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("profess:innovateprofessachieve:save")
    public R save(@RequestBody InnovateProfessAchieveEntity innovateProfessAchieve){
		innovateProfessAchieveService.insert(innovateProfessAchieve);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("profess:innovateprofessachieve:update")
    public R update(@RequestBody InnovateProfessAchieveEntity innovateProfessAchieve){
		innovateProfessAchieveService.updateById(innovateProfessAchieve);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("profess:innovateprofessachieve:delete")
    public R delete(@RequestBody Long[] professAchieveIds){
		innovateProfessAchieveService.deleteBatchIds(Arrays.asList(professAchieveIds));

        return R.ok();
    }

}
