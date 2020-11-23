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

import com.innovate.modules.training.entity.InnovateTrainingBaseInfoEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseInfoService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 实训基地信息
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@RestController
@RequestMapping("training/innovatetrainingbaseinfo")
public class InnovateTrainingBaseInfoController {
    @Autowired
    private InnovateTrainingBaseInfoService innovateTrainingBaseInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("training:innovatetrainingbaseinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateTrainingBaseInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{trainingBaseId}")
    @RequiresPermissions("training:innovatetrainingbaseinfo:info")
    public R info(@PathVariable("trainingBaseId") Long trainingBaseId){
		InnovateTrainingBaseInfoEntity innovateTrainingBaseInfo = innovateTrainingBaseInfoService.selectById(trainingBaseId);

        return R.ok().put("innovateTrainingBaseInfo", innovateTrainingBaseInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("training:innovatetrainingbaseinfo:save")
    public R save(@RequestBody InnovateTrainingBaseInfoEntity innovateTrainingBaseInfo){
		innovateTrainingBaseInfoService.insert(innovateTrainingBaseInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("training:innovatetrainingbaseinfo:update")
    public R update(@RequestBody InnovateTrainingBaseInfoEntity innovateTrainingBaseInfo){
		innovateTrainingBaseInfoService.updateById(innovateTrainingBaseInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("training:innovatetrainingbaseinfo:delete")
    public R delete(@RequestBody Long[] trainingBaseIds){
		innovateTrainingBaseInfoService.deleteBatchIds(Arrays.asList(trainingBaseIds));

        return R.ok();
    }

}
