package com.innovate.modules.training.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.sys.entity.SysUserEntity;
import com.innovate.modules.training.entity.InnovateTrainingBaseAttachModel;
import com.innovate.modules.training.entity.InnovateTrainingBaseInfoEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.training.entity.InnovateTrainingBaseAchieveEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseAchieveService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 实训基地成果表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@RestController
@RequestMapping("training/innovatetrainingbaseachieve")
public class InnovateTrainingBaseAchieveController {
    @Autowired
    private InnovateTrainingBaseAchieveService innovateTrainingBaseAchieveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("training:innovatetrainingbaseachieve:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateTrainingBaseAchieveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{trainingAchieveId}")
    @RequiresPermissions("training:innovatetrainingbaseachieve:info")
    public R info(@PathVariable("trainingAchieveId") Long trainingAchieveId){
        R r = innovateTrainingBaseAchieveService.info(trainingAchieveId);
        return r;
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("training:innovatetrainingbaseachieve:save")
    public R save(@RequestBody InnovateTrainingBaseAttachModel attachModel){
        R r = innovateTrainingBaseAchieveService.insertModel(attachModel);
        return r;
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("training:innovatetrainingbaseachieve:update")
    public R update(@RequestBody InnovateTrainingBaseAttachModel attachModel){
        innovateTrainingBaseAchieveService.update(attachModel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("training:innovatetrainingbaseachieve:delete")
    public R delete(@RequestBody Long[] trainingAchieveIds){
		innovateTrainingBaseAchieveService.deleteBatchIds(Arrays.asList(trainingAchieveIds));

        return R.ok();
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("training:innovatetrainingbaseinfo:list")
    public void export(@RequestBody Long[] trainBaseIds, HttpServletResponse response){
        List<InnovateTrainingBaseAchieveEntity> trainBaseInfoList = new ArrayList<>();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;
        try {
            String fileName = URLEncoder.encode("实训基地成果信息", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateTrainingBaseAchieveEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "实训基地成果信息").build();
                trainBaseInfoList = innovateTrainingBaseAchieveService.queryListByIds(trainBaseIds);
                excelWriter.write(trainBaseInfoList,writeSheet);
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
