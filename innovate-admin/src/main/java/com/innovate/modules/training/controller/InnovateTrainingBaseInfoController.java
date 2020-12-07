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
import com.innovate.modules.training.dao.InnovateTrainingBaseInfoDao;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.training.entity.InnovateTrainingBaseInfoEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseInfoService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;


/**
 * 实训基地信息
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@RestController
@RequestMapping("training/innovatetrainingbaseinfo")
public class InnovateTrainingBaseInfoController {
    @Autowired
    private InnovateTrainingBaseInfoService innovateTrainingBaseInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("training:innovatetrainingbaseinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateTrainingBaseInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{trainingBaseId}")
    @RequiresPermissions("training:innovatetrainingbaseinfo:info")
    public R info(@PathVariable("trainingBaseId") Long trainingBaseId){
		InnovateTrainingBaseInfoEntity innovateTrainingBaseInfo = innovateTrainingBaseInfoService.selectById(trainingBaseId);

        return R.ok().put("innovateTrainingBaseInfo", innovateTrainingBaseInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("training:innovatetrainingbaseinfo:save")
    public R save(@RequestBody InnovateTrainingBaseInfoEntity innovateTrainingBaseInfo){
		innovateTrainingBaseInfoService.insert(innovateTrainingBaseInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("training:innovatetrainingbaseinfo:update")
    public R update(@RequestBody InnovateTrainingBaseInfoEntity innovateTrainingBaseInfo){
		innovateTrainingBaseInfoService.updateById(innovateTrainingBaseInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("training:innovatetrainingbaseinfo:delete")
    public R delete(@RequestBody Long[] trainingBaseIds){
		innovateTrainingBaseInfoService.deleteList(Arrays.asList(trainingBaseIds));

        return R.ok();
    }


    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("training:innovatetrainingbaseinfo:list")
    public void export(@RequestBody Map<String, Object> params, HttpServletResponse response){
        List<InnovateTrainingBaseInfoEntity> trainBaseInfoList = new ArrayList<>();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();

        ExcelWriter excelWriter = null;

        try {
            String fileName = URLEncoder.encode("实训基地信息", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateTrainingBaseInfoEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "实训基地信息").build();
                trainBaseInfoList = innovateTrainingBaseInfoService.queryListByDeptAndIds(params);
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
