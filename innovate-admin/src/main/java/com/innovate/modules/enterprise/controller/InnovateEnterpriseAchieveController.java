package com.innovate.modules.enterprise.controller;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.sys.entity.SysUserEntity;
import com.innovate.modules.util.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseAchieveEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAchieveService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 企业成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:32
 */
@RestController
@RequestMapping("enterprise/innovateenterpriseachieve")
public class InnovateEnterpriseAchieveController {
    @Autowired
    private InnovateEnterpriseAchieveService innovateEnterpriseAchieveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateEnterpriseAchieveService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{enterpAchieveId}")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:info")
    public R info(@PathVariable("enterpAchieveId") Long enterpAchieveId){
		InnovateEnterpriseAchieveEntity innovateEnterpriseAchieve = innovateEnterpriseAchieveService.selectById(enterpAchieveId);
        return R.ok().put("innovateEnterpriseAchieve", innovateEnterpriseAchieve);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:save")
    public R save(@RequestBody InnovateEnterpriseAchieveEntity innovateEnterpriseAchieve){

        innovateEnterpriseAchieve.setEnterpriseUserId(ShiroUtils.getUserId());

		innovateEnterpriseAchieveService.insert(innovateEnterpriseAchieve);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:update")
    public R update(@RequestBody InnovateEnterpriseAchieveEntity innovateEnterpriseAchieve){
		innovateEnterpriseAchieveService.updateById(innovateEnterpriseAchieve);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("enterprise:innovateenterpriseachieve:delete")
    public R delete(@RequestBody Long[] enterpAchieveIds){
//		innovateEnterpriseAchieveService.deleteBatchIds(Arrays.asList(enterpAchieveIds));

        //伪删除
        innovateEnterpriseAchieveService.delList(Arrays.asList(enterpAchieveIds));
        return R.ok();
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("enterprise:innovateenterpriseinfo:list")
    public void export(@RequestBody List<Long> enterpAchieveIds, HttpServletResponse response){
        List<InnovateEnterpriseAchieveEntity> projectEntities;
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;

        try {
            String fileName = URLEncoder.encode("企业成果信息", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateEnterpriseAchieveEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "企业成果信息").build();
//                projectEntities = innovateEnterpriseAchieveService.queryListByIds(enterpAchieveIds);
                projectEntities = innovateEnterpriseAchieveService.queryListByIds(userEntity.getUserId());
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
