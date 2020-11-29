package com.innovate.modules.enterprise.controller;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.enterprise.entity.InnovateEnterpriseProjectEntity;
import com.innovate.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.enterprise.entity.InnovateAwardProjectTypeEntity;
import com.innovate.modules.enterprise.service.InnovateAwardProjectTypeService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 企业获奖项目类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@RestController
@RequestMapping("enterprise/innovateawardprojecttype")
public class InnovateAwardProjectTypeController {
    @Autowired
    private InnovateAwardProjectTypeService innovateAwardProjectTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateawardprojecttype:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateAwardProjectTypeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{awardProjectjTypeId}")
    @RequiresPermissions("enterprise:innovateawardprojecttype:info")
    public R info(@PathVariable("awardProjectjTypeId") Long awardProjectjTypeId){
		InnovateAwardProjectTypeEntity innovateAwardProjectType = innovateAwardProjectTypeService.selectById(awardProjectjTypeId);

        return R.ok().put("innovateAwardProjectType", innovateAwardProjectType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateawardprojecttype:save")
    public R save(@RequestBody InnovateAwardProjectTypeEntity innovateAwardProjectType){
		innovateAwardProjectTypeService.insert(innovateAwardProjectType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateawardprojecttype:update")
    public R update(@RequestBody InnovateAwardProjectTypeEntity innovateAwardProjectType){
		innovateAwardProjectTypeService.updateById(innovateAwardProjectType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("enterprise:innovateawardprojecttype:delete")
    public R delete(@RequestBody Long[] awardProjectjTypeIds){
		innovateAwardProjectTypeService.deleteBatchIds(Arrays.asList(awardProjectjTypeIds));

        return R.ok();
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public void export(@RequestBody List<Long> awardProjectjTypeIds, HttpServletResponse response){
        List<InnovateAwardProjectTypeEntity> projectEntities;
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;

        try {
            String fileName = URLEncoder.encode("企业获奖项目类型信息", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateAwardProjectTypeEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "企业获奖项目类型信息").build();
                projectEntities = innovateAwardProjectTypeService.queryListByIds(awardProjectjTypeIds);
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

}
