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

import com.innovate.modules.training.entity.InnovateTrainingBaseAttachEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseAttachService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 实训基地附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@RestController
@RequestMapping("training/innovatetrainingbaseattach")
public class InnovateTrainingBaseAttachController {
    @Autowired
    private InnovateTrainingBaseAttachService innovateTrainingBaseAttachService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("training:innovatetrainingbaseattach:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateTrainingBaseAttachService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attachId}")
    @RequiresPermissions("training:innovatetrainingbaseattach:info")
    public R info(@PathVariable("attachId") Long attachId){
		InnovateTrainingBaseAttachEntity innovateTrainingBaseAttach = innovateTrainingBaseAttachService.selectById(attachId);

        return R.ok().put("innovateTrainingBaseAttach", innovateTrainingBaseAttach);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("training:innovatetrainingbaseattach:save")
    public R save(@RequestBody InnovateTrainingBaseAttachEntity innovateTrainingBaseAttach){
		innovateTrainingBaseAttachService.insert(innovateTrainingBaseAttach);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("training:innovatetrainingbaseattach:update")
    public R update(@RequestBody InnovateTrainingBaseAttachEntity innovateTrainingBaseAttach){
		innovateTrainingBaseAttachService.updateById(innovateTrainingBaseAttach);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("training:innovatetrainingbaseattach:delete")
    public R delete(@RequestBody Long[] attachIds){
		innovateTrainingBaseAttachService.deleteBatchIds(Arrays.asList(attachIds));

        return R.ok();
    }

}
