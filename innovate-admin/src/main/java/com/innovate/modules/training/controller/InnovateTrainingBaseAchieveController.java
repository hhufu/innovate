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

import com.innovate.modules.training.entity.InnovateTrainingBaseAchieveEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseAchieveService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 实训基地成果表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@RestController
@RequestMapping("training/innovatetrainingbaseachieve")
public class InnovateTrainingBaseAchieveController {
    @Autowired
    private InnovateTrainingBaseAchieveService innovateTrainingBaseAchieveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("training:innovatetrainingbaseachieve:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateTrainingBaseAchieveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{trainingAchieveId}")
    @RequiresPermissions("training:innovatetrainingbaseachieve:info")
    public R info(@PathVariable("trainingAchieveId") Long trainingAchieveId){
		InnovateTrainingBaseAchieveEntity innovateTrainingBaseAchieve = innovateTrainingBaseAchieveService.selectById(trainingAchieveId);

        return R.ok().put("innovateTrainingBaseAchieve", innovateTrainingBaseAchieve);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("training:innovatetrainingbaseachieve:save")
    public R save(@RequestBody InnovateTrainingBaseAchieveEntity innovateTrainingBaseAchieve){
		innovateTrainingBaseAchieveService.insert(innovateTrainingBaseAchieve);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("training:innovatetrainingbaseachieve:update")
    public R update(@RequestBody InnovateTrainingBaseAchieveEntity innovateTrainingBaseAchieve){
		innovateTrainingBaseAchieveService.updateById(innovateTrainingBaseAchieve);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("training:innovatetrainingbaseachieve:delete")
    public R delete(@RequestBody Long[] trainingAchieveIds){
		innovateTrainingBaseAchieveService.deleteBatchIds(Arrays.asList(trainingAchieveIds));

        return R.ok();
    }

}
