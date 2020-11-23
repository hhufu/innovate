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

import com.innovate.modules.enterprise.entity.InnovateAwardProjectTypeEntity;
import com.innovate.modules.enterprise.service.InnovateAwardProjectTypeService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 企业获奖项目类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@RestController
@RequestMapping("enterprise/innovateawardprojecttype")
public class InnovateAwardProjectTypeController {
    @Autowired
    private InnovateAwardProjectTypeService innovateAwardProjectTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateawardprojecttype:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateAwardProjectTypeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{awardProjectjTypeId}")
    @RequiresPermissions("enterprise:innovateawardprojecttype:info")
    public R info(@PathVariable("awardProjectjTypeId") Long awardProjectjTypeId){
		InnovateAwardProjectTypeEntity innovateAwardProjectType = innovateAwardProjectTypeService.selectById(awardProjectjTypeId);

        return R.ok().put("innovateAwardProjectType", innovateAwardProjectType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateawardprojecttype:save")
    public R save(@RequestBody InnovateAwardProjectTypeEntity innovateAwardProjectType){
		innovateAwardProjectTypeService.insert(innovateAwardProjectType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateawardprojecttype:update")
    public R update(@RequestBody InnovateAwardProjectTypeEntity innovateAwardProjectType){
		innovateAwardProjectTypeService.updateById(innovateAwardProjectType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("enterprise:innovateawardprojecttype:delete")
    public R delete(@RequestBody Long[] awardProjectjTypeIds){
		innovateAwardProjectTypeService.deleteBatchIds(Arrays.asList(awardProjectjTypeIds));

        return R.ok();
    }

}
