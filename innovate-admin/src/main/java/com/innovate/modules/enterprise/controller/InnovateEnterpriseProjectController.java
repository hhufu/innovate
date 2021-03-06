package com.innovate.modules.enterprise.controller;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoModel;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAttachService;
import com.innovate.modules.finish.service.FinishAttachService;
import com.innovate.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseProjectService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletResponse;


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

    @Autowired
    private FinishAttachService finishAttachService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateenterpriseproject:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = innovateEnterpriseProjectService.queryPage(params);

        return R.ok().put("page", page);
    }



    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public void export(@RequestBody List<Long> enterpProjIds,@RequestParam Map<String, Object> params, HttpServletResponse response){
        List<InnovateEnterpriseProjectEntity> projectEntities;
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;

        try {
            String fileName = URLEncoder.encode("企业项目信息", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateEnterpriseProjectEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "企业项目信息").build();
                projectEntities = innovateEnterpriseProjectService.queryListByIds(enterpProjIds,params);
                excelWriter.write(projectEntities,writeSheet);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }




    /**
     * 信息
     */
    @RequestMapping("/info/{enterpProjId}")
    @RequiresPermissions("enterprise:innovateenterpriseproject:info")
    public R info(@PathVariable("enterpProjId") Long enterpProjId) {
        InnovateEnterpriseProjectEntity innovateEnterpriseProject = innovateEnterpriseProjectService.selectById(enterpProjId);
        //获取所以附件信息
        List<InnovateEnterpriseAttachEntity> list = innovateEnterpriseAttachService.selectList(
                new EntityWrapper<InnovateEnterpriseAttachEntity>()
                        .eq("function_id",enterpProjId).eq("is_del", 0).eq("attach_type",2)
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
        //附件不为空时保存或附件集合
        if (!innovateEnterpriseInfoModel.getAttachEntities().isEmpty()){
            Long infoId = innovateEnterpriseInfoModel.getProjectEntity().getEnterpProjId();
            for (InnovateEnterpriseAttachEntity attach:innovateEnterpriseInfoModel.getAttachEntities()){
                attach.setFunctionId(infoId);
            }
            innovateEnterpriseAttachService.insertOrUpdateBatch(innovateEnterpriseInfoModel.getAttachEntities());
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseproject:update")
    public R update(@RequestBody InnovateEnterpriseInfoModel innovateEnterpriseInfoModel) {
        innovateEnterpriseProjectService.updateById(innovateEnterpriseInfoModel.getProjectEntity());
        if (!innovateEnterpriseInfoModel.getAttachEntities().isEmpty()) {
            Long infoId = innovateEnterpriseInfoModel.getProjectEntity().getEnterpProjId();
            for (InnovateEnterpriseAttachEntity attach : innovateEnterpriseInfoModel.getAttachEntities()) {
                attach.setFunctionId(infoId);
            }
            innovateEnterpriseAttachService.insertOrUpdateBatch(innovateEnterpriseInfoModel.getAttachEntities());
        }

        finishAttachService.delAttachLists(innovateEnterpriseInfoModel);

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
