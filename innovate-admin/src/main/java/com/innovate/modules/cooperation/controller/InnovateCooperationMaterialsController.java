package com.innovate.modules.cooperation.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.modules.cooperation.entity.InnovateCooperationMaterialsEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationMaterialsService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 校政企合作附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@RestController
@RequestMapping("cooperation/innovatecooperationmaterials")
public class InnovateCooperationMaterialsController {
    @Autowired
    private InnovateCooperationMaterialsService innovateCooperationMaterialsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateCooperationMaterialsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{materialsId}")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:info")
    public R info(@PathVariable("materialsId") Long materialsId){
		InnovateCooperationMaterialsEntity innovateCooperationMaterials = innovateCooperationMaterialsService.selectById(materialsId);

        return R.ok().put("innovateCooperationMaterials", innovateCooperationMaterials);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:save")
    public R save(@RequestBody InnovateCooperationMaterialsEntity innovateCooperationMaterials){
		innovateCooperationMaterialsService.insert(innovateCooperationMaterials);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:update")
    public R update(@RequestBody InnovateCooperationMaterialsEntity innovateCooperationMaterials){
		innovateCooperationMaterialsService.updateById(innovateCooperationMaterials);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:delete")
    public R delete(@RequestBody Long[] materialsIds){
		innovateCooperationMaterialsService.deleteBatchIds(Arrays.asList(materialsIds));

        return R.ok();
    }

}
