package com.innovate.modules.profess.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.profess.entity.ProfessModel;
import com.innovate.modules.sys.entity.SysUserEntity;
import com.innovate.modules.training.entity.InnovateTrainingBaseAchieveEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.profess.entity.InnovateProfessAchieveEntity;
import com.innovate.modules.profess.service.InnovateProfessAchieveService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 专创成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@RestController
@RequestMapping("profess/innovateprofessachieve")
public class InnovateProfessAchieveController {
    @Autowired
    private InnovateProfessAchieveService innovateProfessAchieveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("profess:innovateprofessachieve:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateProfessAchieveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{professAchieveId}")
    @RequiresPermissions("profess:innovateprofessachieve:info")
    public R info(@PathVariable("professAchieveId") Long professAchieveId){
		R r = innovateProfessAchieveService.info(professAchieveId);

        return r;
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("profess:innovateprofessachieve:save")
    public R save(@RequestBody ProfessModel professModel){
        innovateProfessAchieveService.insertModel(professModel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("profess:innovateprofessachieve:update")
    public R update(@RequestBody ProfessModel professModel){
		innovateProfessAchieveService.update(professModel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("profess:innovateprofessachieve:delete")
    public R delete(@RequestBody Long[] professAchieveIds){
		innovateProfessAchieveService.deleteBatchIds(Arrays.asList(professAchieveIds));

        return R.ok();
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("training:innovateprofessachieve:export")
    public void export(@RequestBody Long[] professAchieveIds, HttpServletResponse response){
        List<InnovateProfessAchieveEntity> professAchieveEntities = new ArrayList<>();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;
        try {
            String fileName = URLEncoder.encode("专创结合成果", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateProfessAchieveEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "专创结合成果").build();
                professAchieveEntities = innovateProfessAchieveService.queryListByIds(professAchieveIds);
                excelWriter.write(professAchieveEntities,writeSheet);
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
