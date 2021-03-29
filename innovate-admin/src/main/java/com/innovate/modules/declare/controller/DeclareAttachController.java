package com.innovate.modules.declare.controller;

import com.innovate.common.utils.OSSUtils;
import com.innovate.common.utils.R;
import com.innovate.modules.declare.entity.DeclareAttachEntity;
import com.innovate.modules.declare.service.DeclareAttachService;
import com.innovate.modules.innovate.config.ConfigApi;
import com.innovate.modules.sys.controller.AbstractController;
import com.innovate.modules.util.FileUtils;
import com.innovate.modules.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: 尧志欣
 * Email：1160086364@qq.com
 * Date: 2018/12/5
 * 附件Controller
 **/
@RestController
@RequestMapping("innovate/declare/attach")
@Slf4j
public class DeclareAttachController extends AbstractController {

    @Autowired
    private DeclareAttachService declareAttachService;

    /**
     * 文件上传
     * @param files
     * @param request
     * @return
     */
    @PostMapping(value = "/upload")
    @RequiresPermissions("innovate:declare:save")
    public Object uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) {

        String declareName = request.getParameter("declareName");

//        String UPLOAD_FILES_PATH = ConfigApi.UPLOAD_URL + declareName + "/"+ RandomUtils.getRandomNums() + "/";
        String UPLOAD_FILES_PATH = "declare"+File.separator + Calendar.getInstance().get(Calendar.YEAR) + File.separator+declareName + "/"+ RandomUtils.getRandomNums() + "/";
        if (Objects.isNull(files) || files.isEmpty()) {
            return R.error("文件为空，请重新上传");
        }
        DeclareAttachEntity declareAttachEntity = null;
        for(MultipartFile file : files){
            String fileName = file.getOriginalFilename();
            OSSUtils.upload2OSS(file,UPLOAD_FILES_PATH+fileName);
            UPLOAD_FILES_PATH += fileName;
            declareAttachEntity = new DeclareAttachEntity();
            declareAttachEntity.setAttachPath(UPLOAD_FILES_PATH);
            declareAttachEntity.setAttachName(fileName);
            declareAttachEntity.setIsDel(0L);
        }
        return R.ok("文件上传成功").put("declareAttachEntity", declareAttachEntity);
    }

    /**
     * 文件下载
     */
    @PostMapping(value = "/download")
    @RequiresPermissions("innovate:declare:list")
    public void downloadFile(final HttpServletResponse response, final HttpServletRequest request) {
        String filePath = request.getParameter("filePath");
        FileUtils.download(response, filePath);
    }


    /**
     * 文件下载
     */
    @PostMapping(value = "/downloadurl")
    @RequiresPermissions("innovate:declare:list")
    public R downloadFileUrl(@RequestParam("filePath") String filePath) {

        return R.ok().put("url", FileUtils.getUrl(filePath));
    }
}
