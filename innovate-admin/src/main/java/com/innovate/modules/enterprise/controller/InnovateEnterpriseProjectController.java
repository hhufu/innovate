package com.innovate.modules.enterprise.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoModel;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAttachService;
import com.innovate.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseProjectService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;


/**
 * 企业项目表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@RestController
@RequestMapping("enterprise/innovateenterpriseproject")
public class InnovateEnterpriseProjectController {
    @Autowired
    private InnovateEnterpriseProjectService innovateEnterpriseProjectService;

    @Autowired
    private InnovateEnterpriseAttachService innovateEnterpriseAttachService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateenterpriseproject:list")
    public R list(@RequestParam Map<String, Object> params) {
        SysUserEntity loginUser = ShiroUtils.getUserEntity();
        if (!"SuperAdmin".equals(loginUser.getUsername())){
            Long userId = loginUser.getUserId();
            params.put("project_user_id",userId);
        }
        PageUtils page = innovateEnterpriseProjectService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{enterpProjId}")
    @RequiresPermissions("enterprise:innovateenterpriseproject:info")
    public R info(@PathVariable("enterpProjId") Long enterpProjId) {
        InnovateEnterpriseProjectEntity innovateEnterpriseProject = innovateEnterpriseProjectService.selectById(enterpProjId);

        List<InnovateEnterpriseAttachEntity> list = innovateEnterpriseAttachService.selectList(
                new EntityWrapper<InnovateEnterpriseAttachEntity>()
                        .eq("function_id",enterpProjId)
        );
        InnovateEnterpriseInfoModel infoModel= new InnovateEnterpriseInfoModel();
        infoModel.setProjectEntity(innovateEnterpriseProject);
        infoModel.setAttachEntities(list);
        return R.ok().put("infoModel", infoModel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateenterpriseproject:save")
    public R save(@RequestBody InnovateEnterpriseInfoModel innovateEnterpriseInfoModel) {
        innovateEnterpriseInfoModel.getProjectEntity().setProjectUserId(ShiroUtils.getUserId());
        innovateEnterpriseProjectService.insert( innovateEnterpriseInfoModel.getProjectEntity());
        Long infoId = innovateEnterpriseInfoModel.getProjectEntity().getEnterpProjId();
        for (InnovateEnterpriseAttachEntity attach:innovateEnterpriseInfoModel.getAttachEntities()){
            attach.setFunctionId(infoId);
        }
        innovateEnterpriseAttachService.insertOrUpdateBatch(innovateEnterpriseInfoModel.getAttachEntities());
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseproject:update")
    public R update(@RequestBody InnovateEnterpriseProjectEntity innovateEnterpriseProject) {
        innovateEnterpriseProjectService.updateById(innovateEnterpriseProject);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("enterprise:innovateenterpriseproject:delete")
    public R delete(@RequestBody Long[] enterpProjIds) {
//		innovateEnterpriseProjectService.deleteBatchIds(Arrays.asList(enterpProjIds));
        innovateEnterpriseProjectService.delList(Arrays.asList(enterpProjIds));
        return R.ok();
    }

}
