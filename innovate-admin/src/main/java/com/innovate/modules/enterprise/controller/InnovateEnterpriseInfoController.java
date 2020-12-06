package com.innovate.modules.enterprise.controller;

import java.net.URLEncoder;
import java.util.*;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoModel;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAttachService;
import com.innovate.modules.finish.service.FinishAttachService;
import com.innovate.modules.sys.entity.SysUserEntity;
import com.innovate.modules.training.entity.InnovateTrainingBaseInfoEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseInfoEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseInfoService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletResponse;


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

    @Autowired
    private FinishAttachService finishAttachService;
    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public void export(@RequestBody List<Long> settledEnterpIds, HttpServletResponse response) {
        List<InnovateEnterpriseInfoEntity> trainBaseInfoList;
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;

        try {
            String fileName = URLEncoder.encode("企业入驻信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename" + fileName + ".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true) {
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateEnterpriseInfoEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "企业入驻信息").build();
                trainBaseInfoList = innovateEnterpriseInfoService.queryListByIds(settledEnterpIds);
                excelWriter.write(trainBaseInfoList, writeSheet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = innovateEnterpriseInfoService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 项目名称列表
     */
    @RequestMapping("/nameList")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public R nameList() {
        SysUserEntity loginUser = ShiroUtils.getUserEntity();
        //apply_status 1：审核通过的企业
        Wrapper<InnovateEnterpriseInfoEntity> wrapper = new EntityWrapper<InnovateEnterpriseInfoEntity>()
                .eq("apply_status", 1);
        wrapper.eq("is_del", 0);
        if (!"SuperAdmin".equals(loginUser.getUsername())) {
            Long userId = loginUser.getUserId();
            wrapper.eq("enterprise_user_id", userId);
        }
        List<InnovateEnterpriseInfoEntity> list = innovateEnterpriseInfoService.selectList(wrapper);
        return R.ok().put("list", list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{settledEnterpId}")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:info")
    public R info(@PathVariable("settledEnterpId") Long settledEnterpId) {
        InnovateEnterpriseInfoEntity innovateEnterpriseInfo = innovateEnterpriseInfoService.selectById(settledEnterpId);
        //获取所以附件信息
        List<InnovateEnterpriseAttachEntity> list = innovateEnterpriseAttachService.selectList(
                new EntityWrapper<InnovateEnterpriseAttachEntity>()
                        .eq("function_id", settledEnterpId).eq("is_del", 0).eq("attach_type",1)
        );
        InnovateEnterpriseInfoModel infoModel = new InnovateEnterpriseInfoModel();
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
    public R save(@RequestBody(required = false) InnovateEnterpriseInfoModel innovateEnterpriseInfoModel) {
        innovateEnterpriseInfoModel.getInfoEntity().setEnterpriseUserId(ShiroUtils.getUserId());
        innovateEnterpriseInfoService.insert(innovateEnterpriseInfoModel.getInfoEntity());
        //附件不为空时保存附件集合
        if (!innovateEnterpriseInfoModel.getAttachEntities().isEmpty()) {
            Long infoId = innovateEnterpriseInfoModel.getInfoEntity().getSettledEnterpId();
            for (InnovateEnterpriseAttachEntity attach : innovateEnterpriseInfoModel.getAttachEntities()) {
                attach.setFunctionId(infoId);
            }
            innovateEnterpriseAttachService.insertOrUpdateBatch(innovateEnterpriseInfoModel.getAttachEntities());
        }

        return R.ok();
    }

    /**
     * 修改
     */
    @Transactional
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:update")
    public R update(@RequestBody InnovateEnterpriseInfoModel innovateEnterpriseInfoModel) {
        innovateEnterpriseInfoService.updateById(innovateEnterpriseInfoModel.getInfoEntity());
        //附件不为空时保存或附件集合
        if (!innovateEnterpriseInfoModel.getAttachEntities().isEmpty()) {
            Long infoId = innovateEnterpriseInfoModel.getInfoEntity().getSettledEnterpId();
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
    @RequiresPermissions("enterprise:innovateenterpriseinfo:delete")
    public R delete(@RequestBody Long[] settledEnterpIds) {
        //伪删除
        innovateEnterpriseInfoService.delList(Arrays.asList(settledEnterpIds));
//		innovateEnterpriseInfoService.deleteBatchIds(Arrays.asList(settledEnterpIds));
        return R.ok();
    }

}
