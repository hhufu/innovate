package com.innovate.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import com.innovate.modules.sys.entity.SysMailCodeEntity;
import com.innovate.modules.sys.service.SysMailCodeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 *
 *
 * @author luo
 * @email 1275986528@qq.com
 * @date 2021-07-13 11:01:39
 */
@RestController
@RequestMapping("innovate/sysmailcode")
public class SysMailCodeController {
    @Autowired
    private SysMailCodeService sysMailCodeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("innovate:sysmailcode:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysMailCodeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("innovate:sysmailcode:info")
    public R info(@PathVariable("id") Long id){
		SysMailCodeEntity sysMailCode = sysMailCodeService.selectById(id);

        return R.ok().put("sysMailCode", sysMailCode);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("innovate:sysmailcode:save")
    public R save(@RequestBody SysMailCodeEntity sysMailCode){
		sysMailCodeService.insert(sysMailCode);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("innovate:sysmailcode:update")
    public R update(@RequestBody SysMailCodeEntity sysMailCode){
		sysMailCodeService.updateById(sysMailCode);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("innovate:sysmailcode:delete")
    public R delete(@RequestBody Long[] ids){
		sysMailCodeService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
