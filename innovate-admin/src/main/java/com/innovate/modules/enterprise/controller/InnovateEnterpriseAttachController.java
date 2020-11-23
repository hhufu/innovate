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

import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAttachService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 企业附件表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@RestController
@RequestMapping("enterprise/innovateenterpriseattach")
public class InnovateEnterpriseAttachController {
    @Autowired
    private InnovateEnterpriseAttachService innovateEnterpriseAttachService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateenterpriseattach:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateEnterpriseAttachService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attachId}")
    @RequiresPermissions("enterprise:innovateenterpriseattach:info")
    public R info(@PathVariable("attachId") Long attachId){
		InnovateEnterpriseAttachEntity innovateEnterpriseAttach = innovateEnterpriseAttachService.selectById(attachId);

        return R.ok().put("innovateEnterpriseAttach", innovateEnterpriseAttach);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateenterpriseattach:save")
    public R save(@RequestBody InnovateEnterpriseAttachEntity innovateEnterpriseAttach){
		innovateEnterpriseAttachService.insert(innovateEnterpriseAttach);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseattach:update")
    public R update(@RequestBody InnovateEnterpriseAttachEntity innovateEnterpriseAttach){
		innovateEnterpriseAttachService.updateById(innovateEnterpriseAttach);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("enterprise:innovateenterpriseattach:delete")
    public R delete(@RequestBody Long[] attachIds){
		innovateEnterpriseAttachService.deleteBatchIds(Arrays.asList(attachIds));

        return R.ok();
    }

}
