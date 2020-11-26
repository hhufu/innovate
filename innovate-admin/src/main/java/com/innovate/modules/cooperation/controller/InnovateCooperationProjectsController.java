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

import com.innovate.modules.cooperation.entity.InnovateCooperationProjectsEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationProjectsService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 校政企合作项目
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@RestController
@RequestMapping("cooperation/innovatecooperationprojects")
public class InnovateCooperationProjectsController {
    @Autowired
    private InnovateCooperationProjectsService innovateCooperationProjectsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cooperation:innovatecooperationprojects:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateCooperationProjectsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{cooperationId}")
    @RequiresPermissions("cooperation:innovatecooperationprojects:info")
    public R info(@PathVariable("cooperationId") Long cooperationId){
		InnovateCooperationProjectsEntity innovateCooperationProjects = innovateCooperationProjectsService.selectById(cooperationId);

        return R.ok().put("innovateCooperationProjects", innovateCooperationProjects);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cooperation:innovatecooperationprojects:save")
    public R save(@RequestBody InnovateCooperationProjectsEntity innovateCooperationProjects){
		innovateCooperationProjectsService.insert(innovateCooperationProjects);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cooperation:innovatecooperationprojects:update")
    public R update(@RequestBody InnovateCooperationProjectsEntity innovateCooperationProjects){
		innovateCooperationProjectsService.updateById(innovateCooperationProjects);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cooperation:innovatecooperationprojects:delete")
    public R delete(@RequestBody Long[] cooperationIds){
		innovateCooperationProjectsService.deleteBatchIds(Arrays.asList(cooperationIds));

        return R.ok();
    }

}
