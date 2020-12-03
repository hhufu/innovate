package com.innovate.modules.profess.controller;

import java.io.File;
import java.util.*;

import com.innovate.common.utils.OSSUtils;
import com.innovate.modules.points.entity.InnovateStudentPointsAttachEntity;
import com.innovate.modules.util.FileUtils;
import com.innovate.modules.util.RandomUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.profess.entity.InnovateProfessAttachEntity;
import com.innovate.modules.profess.service.InnovateProfessAttachService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 专创附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@RestController
@RequestMapping("profess/profess")
public class InnovateProfessAttachController {
    @Autowired
    private InnovateProfessAttachService innovateProfessAttachService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("profess:innovateprofessattach:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateProfessAttachService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attachId}")
    @RequiresPermissions("profess:innovateprofessattach:info")
    public R info(@PathVariable("attachId") Long attachId){
		InnovateProfessAttachEntity innovateProfessAttach = innovateProfessAttachService.selectById(attachId);

        return R.ok().put("innovateProfessAttach", innovateProfessAttach);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("profess:innovateprofessattach:save")
    public R save(@RequestBody InnovateProfessAttachEntity innovateProfessAttach){
		innovateProfessAttachService.insert(innovateProfessAttach);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("profess:innovateprofessattach:update")
    public R update(@RequestBody InnovateProfessAttachEntity innovateProfessAttach){
		innovateProfessAttachService.updateById(innovateProfessAttach);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("profess:innovateprofessattach:delete")
    public R delete(@RequestBody Long[] attachIds){
		innovateProfessAttachService.deleteBatchIds(Arrays.asList(attachIds));

        return R.ok();
    }
    /**
     * 文件上传
     * @param files
     * @param request
     * @return
     */
    @PostMapping(value = "/upload")
    @RequiresPermissions("profess:innovateprofessachieve:save")
    public Object uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) {
        String finishName = request.getParameter("fileName");
//        String UPLOAD_FILES_PATH = ConfigApi.UPLOAD_URL + finishName + "/"+ RandomUtils.getRandomNums()+"/";
        String UPLOAD_FILES_PATH = "professAchieve"+ File.separator + Calendar.getInstance().get(Calendar.YEAR) + File.separator+finishName + "/"+ RandomUtils.getRandomNums()+"/";
        if (Objects.isNull(files) || files.isEmpty()) {
            return R.error("文件为空，请重新上传");
        }
        InnovateProfessAttachEntity professAttachEntity = null;
        for(MultipartFile file : files){

            String fileName = file.getOriginalFilename();
//                result = FileUtils.upLoad(UPLOAD_FILES_PATH, fileName, file);
            OSSUtils.upload2OSS(file,UPLOAD_FILES_PATH+fileName);
            UPLOAD_FILES_PATH += fileName;

            professAttachEntity = new InnovateProfessAttachEntity();
            professAttachEntity.setAttachPath(UPLOAD_FILES_PATH);
            professAttachEntity.setAttachName(fileName);
        }
        return R.ok("文件上传成功")
                .put("professAttachEntity", professAttachEntity);
    }

    /**
     * 文件下载
     */
    @PostMapping(value = "/download")
    @RequiresPermissions("profess:innovateprofessachieve:info")
    public void downloadFile(final HttpServletResponse response, final HttpServletRequest request) {
        String filePath = request.getParameter("filePath");
        FileUtils.download(response, filePath);
    }
}
