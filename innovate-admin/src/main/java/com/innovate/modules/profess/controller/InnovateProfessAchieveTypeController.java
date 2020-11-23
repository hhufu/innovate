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

import com.innovate.modules.profess.entity.InnovateProfessAchieveTypeEntity;
import com.innovate.modules.profess.service.InnovateProfessAchieveTypeService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 专创成果类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@RestController
@RequestMapping("profess/innovateprofessachievetype")
public class InnovateProfessAchieveTypeController {
    @Autowired
    private InnovateProfessAchieveTypeService innovateProfessAchieveTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("profess:innovateprofessachievetype:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateProfessAchieveTypeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{professAchieveTypeId}")
    @RequiresPermissions("profess:innovateprofessachievetype:info")
    public R info(@PathVariable("professAchieveTypeId") Long professAchieveTypeId){
		InnovateProfessAchieveTypeEntity innovateProfessAchieveType = innovateProfessAchieveTypeService.selectById(professAchieveTypeId);

        return R.ok().put("innovateProfessAchieveType", innovateProfessAchieveType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("profess:innovateprofessachievetype:save")
    public R save(@RequestBody InnovateProfessAchieveTypeEntity innovateProfessAchieveType){
		innovateProfessAchieveTypeService.insert(innovateProfessAchieveType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("profess:innovateprofessachievetype:update")
    public R update(@RequestBody InnovateProfessAchieveTypeEntity innovateProfessAchieveType){
		innovateProfessAchieveTypeService.updateById(innovateProfessAchieveType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("profess:innovateprofessachievetype:delete")
    public R delete(@RequestBody Long[] professAchieveTypeIds){
		innovateProfessAchieveTypeService.deleteBatchIds(Arrays.asList(professAchieveTypeIds));

        return R.ok();
    }

}
