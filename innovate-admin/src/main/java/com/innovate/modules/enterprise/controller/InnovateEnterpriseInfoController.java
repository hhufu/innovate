package com.innovate.modules.enterprise.controller;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoModel;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAttachService;
import com.innovate.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseInfoService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;


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
        SysUserEntity loginUser = ShiroUtils.getUserEntity();
        if (!"SuperAdmin".equals(loginUser.getUsername())){
            Long userId = loginUser.getUserId();
            params.put("project_user_id",userId);
        }
        PageUtils page = innovateEnterpriseInfoService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 项目名称列表
     */
    @RequestMapping("/nameList")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public R nameList(){
        SysUserEntity loginUser = ShiroUtils.getUserEntity();
        //apply_status 1：审核通过的企业
        Wrapper<InnovateEnterpriseInfoEntity> wrapper = new EntityWrapper<InnovateEnterpriseInfoEntity>()
                .eq("apply_status", 1);
        if (!"SuperAdmin".equals(loginUser.getUsername())){
            Long userId = loginUser.getUserId();
            wrapper.eq("enterprise_user_id",userId);
        }
        List<InnovateEnterpriseInfoEntity> list = innovateEnterpriseInfoService.selectList(wrapper);
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
        infoModel.setInfoEntity(innovateEnterpriseInfo);
        infoModel.setAttachEntities(list);
        return R.ok().put("infoModel", infoModel);
    }

    /**
     * 保存
     */
    @Transactional
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:save")
    public R save(@RequestBody(required = false) InnovateEnterpriseInfoModel  innovateEnterpriseInfoModel){
        innovateEnterpriseInfoModel.getInfoEntity().setEnterpriseUserId(ShiroUtils.getUserId());
        innovateEnterpriseInfoService.insert(innovateEnterpriseInfoModel.getInfoEntity());
        Long infoId = innovateEnterpriseInfoModel.getInfoEntity().getSettledEnterpId();
        for (InnovateEnterpriseAttachEntity attach:innovateEnterpriseInfoModel.getAttachEntities()){
            attach.setFunctionId(infoId);
        }
        innovateEnterpriseAttachService.insertOrUpdateBatch(innovateEnterpriseInfoModel.getAttachEntities());
        return R.ok();
    }

    /**
     * 修改
     */
    @Transactional
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:update")
    public R update(@RequestBody InnovateEnterpriseInfoModel innovateEnterpriseInfoModel){
		innovateEnterpriseInfoService.updateById(innovateEnterpriseInfoModel.getInfoEntity());
        Long infoId = innovateEnterpriseInfoModel.getInfoEntity().getSettledEnterpId();
        for (InnovateEnterpriseAttachEntity attach:innovateEnterpriseInfoModel.getAttachEntities()){
            attach.setFunctionId(infoId);
        }
        innovateEnterpriseAttachService.insertOrUpdateBatch(innovateEnterpriseInfoModel.getAttachEntities());
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
