package com.innovate.modules.points.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.innovate.common.utils.ShiroUtils;
import com.innovate.modules.innovate.config.ConfigApi;
import com.innovate.modules.points.utils.InnovateStudentPointsEntityListener;
import com.innovate.modules.profess.entity.InnovateProfessAchieveEntity;
import com.innovate.modules.sys.entity.SysUserEntity;
import com.innovate.modules.util.FileUtils;
import com.innovate.modules.util.RandomUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.points.entity.InnovateStudentPointsEntity;
import com.innovate.modules.points.service.InnovateStudentPointsService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 *
 * @author Mikey
 * @email biaogejiushibiao@outlook.com
 * @date 2020-08-09 09:13:27
 */
@RestController
@RequestMapping("points/innovatestudentpoints")
public class InnovateStudentPointsController {
    @Autowired
    private InnovateStudentPointsService innovateStudentPointsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("points:innovatestudentpoints:list")
    public R list(@RequestParam Map<String, Object> params){
        //记录
        PageUtils page = innovateStudentPointsService.queryPage(params);
        //总分
        Integer total = null;
        if (params.get("userId")!=null)total = innovateStudentPointsService.queryPointByParams(params);
        return R.ok().put("page", page).put("total",total);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("points:innovatestudentpoints:info")
    public R info(@PathVariable("id") Long id){
		InnovateStudentPointsEntity innovateStudentPoints = innovateStudentPointsService.selectById(id);

        return R.ok().put("innovateStudentPoints", innovateStudentPoints);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("points:innovatestudentpoints:save")
    public R save(@RequestBody InnovateStudentPointsEntity innovateStudentPoints){
        //保存积分
		R r = innovateStudentPointsService.insertAndCheck(innovateStudentPoints);

        return r;
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("points:innovatestudentpoints:update")
    public R update(@RequestBody InnovateStudentPointsEntity innovateStudentPoints){
		innovateStudentPointsService.updateById(innovateStudentPoints);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("points:innovatestudentpoints:delete")
    public R delete(@RequestBody Long[] ids){
		innovateStudentPointsService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 文件上传导入分数
     * @param file
     * @return
     */
    @PostMapping(value = "/input")
    @RequiresPermissions("points:innovatestudentpoints:save")
    public Object uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            EasyExcel.read(file.getInputStream(), InnovateStudentPointsEntity.class, new InnovateStudentPointsEntityListener(innovateStudentPointsService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.ok("导入成功");
    }

    /**
     * 保存申请的积分
     */
    @RequestMapping("/saveApplyPoints")
    @RequiresPermissions("points:innovatestudentpoints:save")
    public R saveApplyPoints(@RequestBody InnovateStudentPointsEntity innovateStudentPoints){
        innovateStudentPoints.setOperationTime(new Date());
        Boolean f = innovateStudentPointsService.insert(innovateStudentPoints);

        return R.ok("保存成功");
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @RequiresPermissions("points:innovatestudentpoints:export")
    public void export(@RequestBody Map<String, Object> params, HttpServletResponse response){
        List<InnovateStudentPointsEntity> studentPointsEntities = new ArrayList<>();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        /* 获取当前登录用户 */
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        String adminName = userEntity.getUsername();
        ExcelWriter excelWriter = null;
        try {
            String fileName = URLEncoder.encode("学生积分数据", "UTF-8");
            response.setHeader("Content-disposition","attachment;filename"+fileName+".xlsx");
            /* 权限判断：当前用户为管理员(暂时不做权限限制) */
            if ("wzxyGLY".equals(adminName) || true){
                excelWriter = EasyExcel.write(response.getOutputStream(), InnovateStudentPointsEntity.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "学生积分数据").build();
                studentPointsEntities = innovateStudentPointsService.queryListByIds(params);
                excelWriter.write(studentPointsEntities,writeSheet);
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
