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

import com.innovate.modules.cooperation.entity.InnovateCooperationAgreementEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationAgreementService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 企业协议管理
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@RestController
@RequestMapping("cooperation/innovatecooperationagreement")
public class InnovateCooperationAgreementController {
    @Autowired
    private InnovateCooperationAgreementService innovateCooperationAgreementService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cooperation:innovatecooperationagreement:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateCooperationAgreementService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{enterpriseId}")
    @RequiresPermissions("cooperation:innovatecooperationagreement:info")
    public R info(@PathVariable("enterpriseId") Long enterpriseId){
		InnovateCooperationAgreementEntity innovateCooperationAgreement = innovateCooperationAgreementService.selectById(enterpriseId);

        return R.ok().put("innovateCooperationAgreement", innovateCooperationAgreement);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cooperation:innovatecooperationagreement:save")
    public R save(@RequestBody InnovateCooperationAgreementEntity innovateCooperationAgreement){
		innovateCooperationAgreementService.insert(innovateCooperationAgreement);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cooperation:innovatecooperationagreement:update")
    public R update(@RequestBody InnovateCooperationAgreementEntity innovateCooperationAgreement){
		innovateCooperationAgreementService.updateById(innovateCooperationAgreement);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cooperation:innovatecooperationagreement:delete")
    public R delete(@RequestBody Long[] enterpriseIds){
		innovateCooperationAgreementService.deleteBatchIds(Arrays.asList(enterpriseIds));

        return R.ok();
    }

}
