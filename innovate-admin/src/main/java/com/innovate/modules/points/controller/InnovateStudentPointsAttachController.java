package com.innovate.modules.points.controller;

import java.io.File;
import java.util.*;

import com.innovate.common.utils.DateUtils;
import com.innovate.common.utils.OSSUtils;
import com.innovate.modules.finish.entity.FinishAttachEntity;
import com.innovate.modules.points.entity.InnovateStudentActivityEntity;
import com.innovate.modules.util.FileUtils;
import com.innovate.modules.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.points.entity.InnovateStudentPointsAttachEntity;
import com.innovate.modules.points.service.InnovateStudentPointsAttachService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 学生积分申请附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
@RestController
@RequestMapping("points/attach")
@Slf4j
public class InnovateStudentPointsAttachController {
    @Autowired
    private InnovateStudentPointsAttachService innovateStudentPointsAttachService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("points:innovatestudentpointsattach:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateStudentPointsAttachService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attachId}")
    @RequiresPermissions("points:innovatestudentpointsattach:info")
    public R info(@PathVariable("attachId") Long attachId){
		InnovateStudentPointsAttachEntity innovateStudentPointsAttach = innovateStudentPointsAttachService.selectById(attachId);

        return R.ok().put("innovateStudentPointsAttach", innovateStudentPointsAttach);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("points:innovatestudentpointsattach:save")
    public R save(@RequestBody InnovateStudentPointsAttachEntity innovateStudentPointsAttach){
		innovateStudentPointsAttachService.insert(innovateStudentPointsAttach);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("points:innovatestudentpointsattach:update")
    public R update(@RequestBody InnovateStudentPointsAttachEntity innovateStudentPointsAttach){
		innovateStudentPointsAttachService.updateById(innovateStudentPointsAttach);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("points:innovatestudentpointsattach:delete")
    public R delete(@RequestBody Long[] attachIds){
		innovateStudentPointsAttachService.deleteBatchIds(Arrays.asList(attachIds));

        return R.ok();
    }

    /**
     * 文件上传
     * @param files
     * @param request
     * @return
     */
    @PostMapping(value = "/upload")
    @RequiresPermissions("points:innovatestudentpointsapply:save")
    public Object uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) {
        String stuNum = request.getParameter("stuNum");
        if (stuNum==null||stuNum.equals(""))stuNum="StuNumISNull";
//        String UPLOAD_FILES_PATH = ConfigApi.UPLOAD_URL + finishName + "/"+ RandomUtils.getRandomNums()+"/";
        String UPLOAD_FILES_PATH = "pointsApply"+ File.separator + Calendar.getInstance().get(Calendar.YEAR) + File.separator+stuNum + "/"+ RandomUtils.getRandomNums()+"/";
        if (Objects.isNull(files) || files.isEmpty()) {
            return R.error("文件为空，请重新上传");
        }
        InnovateStudentPointsAttachEntity pointsAttachEntity = null;
        for(MultipartFile file : files){

            String fileName = file.getOriginalFilename();
//            String fileName = file.getOriginalFilename() + "(" +DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ")";

//                result = FileUtils.upLoad(UPLOAD_FILES_PATH, fileName, file);
            OSSUtils.upload2OSS(file,UPLOAD_FILES_PATH+fileName);
            UPLOAD_FILES_PATH += fileName;

            pointsAttachEntity = new InnovateStudentPointsAttachEntity();
            pointsAttachEntity.setAttachPath(UPLOAD_FILES_PATH);
            pointsAttachEntity.setAttachName(fileName);
        }
        return R.ok("文件上传成功")
                .put("pointsAttachEntity", pointsAttachEntity);
    }

    /**
     * 文件下载
     */
    @PostMapping(value = "/download")
    @RequiresPermissions("points:innovatestudentpointsapply:info")
    public void downloadFile(final HttpServletResponse response, final HttpServletRequest request) {
        String filePath = request.getParameter("filePath");
        FileUtils.download(response, filePath);
    }
}
