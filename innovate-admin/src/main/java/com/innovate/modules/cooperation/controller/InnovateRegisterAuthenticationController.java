package com.innovate.modules.cooperation.controller;

import java.util.*;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.modules.cooperation.entity.*;
import com.innovate.modules.cooperation.service.*;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 企业登记认证表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@RestController
@RequestMapping("cooperation/innovateregisterauthentication")
public class InnovateRegisterAuthenticationController {
    @Autowired
    private InnovateRegisterAuthenticationService innovateRegisterAuthenticationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cooperation:innovateregisterauthentication:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateRegisterAuthenticationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{authenticationId}")
    @RequiresPermissions("cooperation:innovateregisterauthentication:info")
    public R info(@PathVariable("authenticationId") Long authenticationId){
		InnovateRegisterAuthenticationEntity innovateRegisterAuthentication = innovateRegisterAuthenticationService.selectById(authenticationId);

        return R.ok().put("innovateRegisterAuthentication", innovateRegisterAuthentication);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cooperation:innovateregisterauthentication:save")
    public R save(@RequestBody InnovateRegisterAuthenticationEntity innovateRegisterAuthentication){
		innovateRegisterAuthenticationService.insert(innovateRegisterAuthentication);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cooperation:innovateregisterauthentication:update")
    public R update(@RequestBody InnovateRegisterAuthenticationEntity innovateRegisterAuthentication){
        innovateRegisterAuthenticationService.updateById(innovateRegisterAuthentication);

		return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cooperation:innovateregisterauthentication:delete")
    public R delete(@RequestBody Long[] authenticationIds){
//		innovateRegisterAuthenticationService.deleteBatchIds(Arrays.asList(authenticationIds));
        innovateRegisterAuthenticationService.deleteList(Arrays.asList(authenticationIds));

        return R.ok();
    }

}
