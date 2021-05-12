package com.innovate.modules.training.controller;

import java.io.File;
import java.util.*;

import com.innovate.common.utils.OSSUtils;
import com.innovate.modules.util.FileUtils;
import com.innovate.modules.util.RandomUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.training.entity.InnovateTrainingBaseAttachEntity;
import com.innovate.modules.training.service.InnovateTrainingBaseAttachService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 实训基地附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@RestController
@RequestMapping("training/innovatetrainingbaseattach")
public class InnovateTrainingBaseAttachController {
    @Autowired
    private InnovateTrainingBaseAttachService innovateTrainingBaseAttachService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("training:innovatetrainingbaseattach:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateTrainingBaseAttachService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attachId}")
    @RequiresPermissions("training:innovatetrainingbaseattach:info")
    public R info(@PathVariable("attachId") Long attachId){
		InnovateTrainingBaseAttachEntity innovateTrainingBaseAttach = innovateTrainingBaseAttachService.selectById(attachId);

        return R.ok().put("innovateTrainingBaseAttach", innovateTrainingBaseAttach);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("training:innovatetrainingbaseattach:save")
    public R save(@RequestBody InnovateTrainingBaseAttachEntity innovateTrainingBaseAttach){
		innovateTrainingBaseAttachService.insert(innovateTrainingBaseAttach);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("training:innovatetrainingbaseattach:update")
    public R update(@RequestBody InnovateTrainingBaseAttachEntity innovateTrainingBaseAttach){
		innovateTrainingBaseAttachService.updateById(innovateTrainingBaseAttach);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("training:innovatetrainingbaseattach:delete")
    public R delete(@RequestBody Long[] attachIds){
		innovateTrainingBaseAttachService.deleteBatchIds(Arrays.asList(attachIds));

        return R.ok();
    }

    /**
     * 文件上传
     * @param files
     * @param request
     * @return
     */
    @PostMapping(value = "/upload")
    @RequiresPermissions("training:innovatetrainingbaseachieve:list")
    public Object uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) {
        String trainingBaseName = request.getParameter("trainingBaseName");
//        String UPLOAD_FILES_PATH = ConfigApi.UPLOAD_URL + enterpriseName + "/"+ RandomUtils.getRandomNums()+"/";
        String UPLOAD_FILES_PATH = "innovatetrainingbaseattach"+ File.separator + Calendar.getInstance().get(Calendar.YEAR) + File.separator+trainingBaseName + "/"+ RandomUtils.getRandomNums()+"/";
        if (Objects.isNull(files) || files.isEmpty()) {
            return R.error("文件为空，请重新上传");
        }
        InnovateTrainingBaseAttachEntity trainingBaseAttachEntity = null;
        for(MultipartFile file : files){

            String fileName = file.getOriginalFilename();
//                result = FileUtils.upLoad(UPLOAD_FILES_PATH, fileName, file);
            OSSUtils.upload2OSS(file,UPLOAD_FILES_PATH+fileName);
            UPLOAD_FILES_PATH += fileName;

            trainingBaseAttachEntity = new InnovateTrainingBaseAttachEntity();
            trainingBaseAttachEntity.setAttachPath(UPLOAD_FILES_PATH);
            trainingBaseAttachEntity.setAttachName(fileName);
        }
        return R.ok("文件上传成功")
                .put("trainingBaseAttachEntity", trainingBaseAttachEntity);
    }

    /**
     * 文件下载
     */
    @PostMapping(value = "/download")
    @RequiresPermissions("training:innovatetrainingbaseachieve:list")
    public void downloadFile(final HttpServletResponse response, final HttpServletRequest request) {
        String filePath = request.getParameter("filePath");
        FileUtils.download(response, filePath);
    }

}
