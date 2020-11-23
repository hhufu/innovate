package com.innovate.modules.profess.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.modules.profess.entity.InnovateProfessAttachEntity;
import com.innovate.modules.profess.service.InnovateProfessAttachService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 专创附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@RestController
@RequestMapping("profess/innovateprofessattach")
public class InnovateProfessAttachController {
    @Autowired
    private InnovateProfessAttachService innovateProfessAttachService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("profess:innovateprofessattach:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateProfessAttachService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attachId}")
    @RequiresPermissions("profess:innovateprofessattach:info")
    public R info(@PathVariable("attachId") Long attachId){
		InnovateProfessAttachEntity innovateProfessAttach = innovateProfessAttachService.selectById(attachId);

        return R.ok().put("innovateProfessAttach", innovateProfessAttach);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("profess:innovateprofessattach:save")
    public R save(@RequestBody InnovateProfessAttachEntity innovateProfessAttach){
		innovateProfessAttachService.insert(innovateProfessAttach);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("profess:innovateprofessattach:update")
    public R update(@RequestBody InnovateProfessAttachEntity innovateProfessAttach){
		innovateProfessAttachService.updateById(innovateProfessAttach);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("profess:innovateprofessattach:delete")
    public R delete(@RequestBody Long[] attachIds){
		innovateProfessAttachService.deleteBatchIds(Arrays.asList(attachIds));

        return R.ok();
    }

}
