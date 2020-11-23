package com.innovate.modules.training.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.modules.training.entity.InnovateTrainingAchieveTypeEntity;
import com.innovate.modules.training.service.InnovateTrainingAchieveTypeService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 实训成果类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@RestController
@RequestMapping("training/innovatetrainingachievetype")
public class InnovateTrainingAchieveTypeController {
    @Autowired
    private InnovateTrainingAchieveTypeService innovateTrainingAchieveTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("training:innovatetrainingachievetype:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateTrainingAchieveTypeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{materialTypeId}")
    @RequiresPermissions("training:innovatetrainingachievetype:info")
    public R info(@PathVariable("materialTypeId") Long materialTypeId){
		InnovateTrainingAchieveTypeEntity innovateTrainingAchieveType = innovateTrainingAchieveTypeService.selectById(materialTypeId);

        return R.ok().put("innovateTrainingAchieveType", innovateTrainingAchieveType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("training:innovatetrainingachievetype:save")
    public R save(@RequestBody InnovateTrainingAchieveTypeEntity innovateTrainingAchieveType){
		innovateTrainingAchieveTypeService.insert(innovateTrainingAchieveType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("training:innovatetrainingachievetype:update")
    public R update(@RequestBody InnovateTrainingAchieveTypeEntity innovateTrainingAchieveType){
		innovateTrainingAchieveTypeService.updateById(innovateTrainingAchieveType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("training:innovatetrainingachievetype:delete")
    public R delete(@RequestBody Long[] materialTypeIds){
		innovateTrainingAchieveTypeService.deleteBatchIds(Arrays.asList(materialTypeIds));

        return R.ok();
    }

}
