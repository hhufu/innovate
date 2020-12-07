package com.innovate.modules.cooperation.controller;

import java.net.URLEncoder;
import java.util.*;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.cooperation.entity.InnovateCooperationAttachModel;
import com.innovate.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.cooperation.entity.InnovateCooperationAgreementEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationAgreementService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 企业协议管理
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:20
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
        R r = innovateCooperationAgreementService.info(enterpriseId);
        return r;
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cooperation:innovatecooperationagreement:save")
    public R save(@RequestBody InnovateCooperationAttachModel attachModel){
        R r = innovateCooperationAgreementService.insertModel(attachModel);
        return r;
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cooperation:innovatecooperationagreement:update")
    public R update(@RequestBody InnovateCooperationAttachModel attachModel){
		innovateCooperationAgreementService.update(attachModel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cooperation:innovatecooperationagreement:delete")
    public R delete(@RequestBody Long[] enterpriseIds){
//		innovateCooperationAgreementService.deleteBatchIds(Arrays.asList(enterpriseIds));
		innovateCooperationAgreementService.deleteList(Arrays.asList(enterpriseIds));

        return R.ok();
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("cooperation:innovatecooperationagreement:list")
    public void export(@RequestBody Map<String, Object> params, HttpServletResponse response){
        List<InnovateCooperationAgreementEntity> cooperationagreementIdsList = new ArrayList<>();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;
        try {
            String fileName = URLEncoder.encode("企业协议管理信息", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateCooperationAgreementEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "企业协议管理信息").build();
                cooperationagreementIdsList = innovateCooperationAgreementService.queryListByIds(params);
                excelWriter.write(cooperationagreementIdsList,writeSheet);
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

}
