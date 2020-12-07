package com.innovate.modules.cooperation.controller;

import java.net.URLEncoder;
import java.util.*;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.cooperation.entity.*;
import com.innovate.modules.cooperation.service.*;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletResponse;


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
    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("cooperation:innovateregisterauthentication:list")
    public void export(@RequestBody Map<String, Object> params, HttpServletResponse response){
        List<InnovateRegisterAuthenticationEntity> registerauthenticationIdsList = new ArrayList<>();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;
        try {
            String fileName = URLEncoder.encode("企业登记列表", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateRegisterAuthenticationEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "企业登记列表").build();
                registerauthenticationIdsList = innovateRegisterAuthenticationService.queryListByIds(params);
                excelWriter.write(registerauthenticationIdsList,writeSheet);
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
