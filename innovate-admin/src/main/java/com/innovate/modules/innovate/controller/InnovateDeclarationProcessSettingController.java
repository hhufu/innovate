package com.innovate.modules.innovate.controller;

import java.io.File;
import java.util.*;

import com.innovate.common.utils.OSSUtils;
import com.innovate.modules.util.RandomUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.innovate.entity.InnovateDeclarationProcessSettingEntity;
import com.innovate.modules.innovate.service.InnovateDeclarationProcessSettingService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * 申报流程设置
 *
 * @author HHUFU
 * @email 437213666@qq.com
 * @date 2021-02-03 11:43:20
 */
@RestController
@RequestMapping("innovate/innovatedeclarationprocesssetting")
public class InnovateDeclarationProcessSettingController {
    @Autowired
    private InnovateDeclarationProcessSettingService innovateDeclarationProcessSettingService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("innovate:innovatedeclarationprocesssetting:list")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page = innovateDeclarationProcessSettingService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/queryCount")
    public R queryCount(@RequestBody Map<String, Object> params){
        int i = innovateDeclarationProcessSettingService.queryCount(params);
        if (i == 0) {
            int countStarTime = innovateDeclarationProcessSettingService.queryStartTime(params);
            if (countStarTime == 0) {
                int countEndTime = innovateDeclarationProcessSettingService.queryEndTime(params);
                if (countEndTime > 0)
                   return R.error().put("msg", "您已超出提交时间，将不得再进行申报");
            }

            return R.error().put("msg", "此次申报还未到到提交时间，请在规定时间内才可以申报");
        }
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{dpsId}")
    @RequiresPermissions("innovate:innovatedeclarationprocesssetting:info")
    public R info(@PathVariable("dpsId") Long dpsId){
		InnovateDeclarationProcessSettingEntity innovateDeclarationProcessSetting = innovateDeclarationProcessSettingService.selectById(dpsId);

        return R.ok().put("innovateDeclarationProcessSetting", innovateDeclarationProcessSetting);
    }

    /**
     * 查询
     */
    @RequestMapping("/queryByTime")
    public R info(@RequestParam Map<String, Object> params){
		InnovateDeclarationProcessSettingEntity declarationProcessSetting = innovateDeclarationProcessSettingService.selectByTime(params);

        return R.ok().put("declarationProcessSetting", declarationProcessSetting);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("innovate:innovatedeclarationprocesssetting:save")
    public R save(@RequestBody InnovateDeclarationProcessSettingEntity innovateDeclarationProcessSetting){
		innovateDeclarationProcessSettingService.insert(innovateDeclarationProcessSetting);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("innovate:innovatedeclarationprocesssetting:update")
    public R update(@RequestBody InnovateDeclarationProcessSettingEntity innovateDeclarationProcessSetting){
		innovateDeclarationProcessSettingService.updateById(innovateDeclarationProcessSetting);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("innovate:innovatedeclarationprocesssetting:delete")
    public R delete(@RequestBody Long[] dpsIds){
		innovateDeclarationProcessSettingService.deleteBatchIds(Arrays.asList(dpsIds));

        return R.ok();
    }

    /**
     * 文件上传
     * @param files
     * @param request
     * @return
     */
    @PostMapping(value = "/upload")
    @RequiresPermissions("innovate:innovatedeclarationprocesssetting:save")
    public Object uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) {
        String declareProcessName = request.getParameter("declareProcessName");
        if (declareProcessName==null||declareProcessName.equals(""))declareProcessName="declareProcessNameISNull";
//        String UPLOAD_FILES_PATH = ConfigApi.UPLOAD_URL + matchName + "/" + RandomUtils.getRandomNums() + "/";
        String UPLOAD_FILES_PATH = "declareProcessName"+ File.separator + Calendar.getInstance().get(Calendar.YEAR) + File.separator+ File.separator + declareProcessName + "/" + RandomUtils.getRandomNums() + "/";
        if (Objects.isNull(files) || files.isEmpty()) {
            return R.error("文件为空，请重新上传");
        }
        InnovateDeclarationProcessSettingEntity innovateDeclarationProcessSetting = null;
        for(MultipartFile file : files){
            String fileName = file.getOriginalFilename();
//                result = FileUtils.upLoad(UPLOAD_FILES_PATH, fileName, file);
            OSSUtils.upload2OSS(file,UPLOAD_FILES_PATH+fileName);
            UPLOAD_FILES_PATH += fileName;
            innovateDeclarationProcessSetting = new InnovateDeclarationProcessSettingEntity();
            innovateDeclarationProcessSetting.setAttachPath(UPLOAD_FILES_PATH);
            innovateDeclarationProcessSetting.setAttachName(fileName);
        }
        return R.ok("文件上传成功")
                .put("innovateDeclarationProcessSetting", innovateDeclarationProcessSetting);
    }

}
