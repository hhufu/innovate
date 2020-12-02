package com.innovate.modules.cooperation.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.cooperation.entity.InnovateCooperationAttachModel;
import com.innovate.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.cooperation.entity.InnovateCooperationProjectsEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationProjectsService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 校政企合作项目
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:21
 */
@RestController
@RequestMapping("cooperation/innovatecooperationprojects")
public class InnovateCooperationProjectsController {
    @Autowired
    private InnovateCooperationProjectsService innovateCooperationProjectsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cooperation:innovatecooperationprojects:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateCooperationProjectsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{cooperationId}")
    @RequiresPermissions("cooperation:innovatecooperationprojects:info")
    public R info(@PathVariable("cooperationId") Long cooperationId){
        R r = innovateCooperationProjectsService.info(cooperationId);
        return r;
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cooperation:innovatecooperationprojects:save")
    public R save(@RequestBody InnovateCooperationAttachModel attachModel){
        R r = innovateCooperationProjectsService.insertModel(attachModel);
        return r;
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cooperation:innovatecooperationprojects:update")
    public R update(@RequestBody InnovateCooperationAttachModel attachModel){
		innovateCooperationProjectsService.update(attachModel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cooperation:innovatecooperationprojects:delete")
    public R delete(@RequestBody Long[] cooperationIds){
//		innovateCooperationProjectsService.deleteBatchIds(Arrays.asList(cooperationIds));
		innovateCooperationProjectsService.deleteList(Arrays.asList(cooperationIds));

        return R.ok();
    }
    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("cooperation:innovatecooperationprojects:list")
    public void export(@RequestBody Long[] cooperationProjectsInfoIds, HttpServletResponse response){
        List<InnovateCooperationProjectsEntity> cooperationProjectsInfoList = new ArrayList<>();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;
        try {
            String fileName = URLEncoder.encode("校政企合作项目信息", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateCooperationProjectsEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "校政企合作项目信息").build();
                cooperationProjectsInfoList = innovateCooperationProjectsService.queryListByIds(cooperationProjectsInfoIds);
                excelWriter.write(cooperationProjectsInfoList,writeSheet);
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
