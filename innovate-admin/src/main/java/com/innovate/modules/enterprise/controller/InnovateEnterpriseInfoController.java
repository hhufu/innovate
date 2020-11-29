package com.innovate.modules.enterprise.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoModel;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAttachService;
import com.innovate.modules.innovate.config.ConfigApi;
import com.innovate.modules.innovate.entity.ProjectAttachEntity;
import com.innovate.modules.util.FileUtils;
import com.innovate.modules.util.RandomUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseInfoService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * 入驻企业表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@RestController
@RequestMapping("enterprise/innovateenterpriseinfo")
public class InnovateEnterpriseInfoController {
    @Autowired
    private InnovateEnterpriseInfoService innovateEnterpriseInfoService;

    @Autowired
    private InnovateEnterpriseAttachService innovateEnterpriseAttachService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateEnterpriseInfoService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 项目名称列表
     */
    @RequestMapping("/nameList")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public R nameList(){
        List<InnovateEnterpriseInfoEntity> list = innovateEnterpriseInfoService.selectList(new EntityWrapper<InnovateEnterpriseInfoEntity>()
        .eq("apply_status",1));
        return R.ok().put("list", list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{settledEnterpId}")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:info")
    public R info(@PathVariable("settledEnterpId") Long settledEnterpId){
		InnovateEnterpriseInfoEntity innovateEnterpriseInfo = innovateEnterpriseInfoService.selectById(settledEnterpId);
        List<InnovateEnterpriseAttachEntity> list = innovateEnterpriseAttachService.selectList(
                new EntityWrapper<InnovateEnterpriseAttachEntity>()
                        .eq("function_id",settledEnterpId)
        );
        InnovateEnterpriseInfoModel infoModel= new InnovateEnterpriseInfoModel();
        infoModel.setInnovateEnterpriseInfoEntity(innovateEnterpriseInfo);
        infoModel.setInnovateEnterpriseAttachEntities(list);
        return R.ok().put("infoModel", infoModel);
    }

/*    *//**
     * 保存
     *//*
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:save")
    public R save(@RequestBody InnovateEnterpriseInfoEntity innovateEnterpriseInfo){
		innovateEnterpriseInfoService.insert(innovateEnterpriseInfo);

        return R.ok();
    }*/

    /**
     * 保存
     */
    @Transactional
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:save")
    public R save(@RequestBody(required = false) InnovateEnterpriseInfoModel  innovateEnterpriseInfoModel){
        innovateEnterpriseInfoService.insert(innovateEnterpriseInfoModel.getInnovateEnterpriseInfoEntity());
        Long infoId = innovateEnterpriseInfoModel.getInnovateEnterpriseInfoEntity().getSettledEnterpId();
        for (InnovateEnterpriseAttachEntity attach:innovateEnterpriseInfoModel.getInnovateEnterpriseAttachEntities()){
            attach.setFunctionId(infoId);
        }
        innovateEnterpriseAttachService.insertOrUpdateBatch(innovateEnterpriseInfoModel.getInnovateEnterpriseAttachEntities());
        return R.ok();
    }

    /**
     * 修改
     */
    @Transactional
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:update")
    public R update(@RequestBody InnovateEnterpriseInfoModel innovateEnterpriseInfoModel){
		innovateEnterpriseInfoService.updateById(innovateEnterpriseInfoModel.getInnovateEnterpriseInfoEntity());
        Long infoId = innovateEnterpriseInfoModel.getInnovateEnterpriseInfoEntity().getSettledEnterpId();
        for (InnovateEnterpriseAttachEntity attach:innovateEnterpriseInfoModel.getInnovateEnterpriseAttachEntities()){
            attach.setFunctionId(infoId);
        }
        innovateEnterpriseAttachService.insertOrUpdateBatch(innovateEnterpriseInfoModel.getInnovateEnterpriseAttachEntities());
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:delete")
    public R delete(@RequestBody Long[] settledEnterpIds){
        innovateEnterpriseInfoService.delList(Arrays.asList(settledEnterpIds));
//		innovateEnterpriseInfoService.deleteBatchIds(Arrays.asList(settledEnterpIds));
        return R.ok();
    }

}
