package com.innovate.modules.cooperation.controller;

import java.io.File;
import java.util.*;

import com.innovate.common.utils.OSSUtils;
import com.innovate.modules.util.FileUtils;
import com.innovate.modules.util.RandomUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.cooperation.entity.InnovateCooperationMaterialsEntity;
import com.innovate.modules.cooperation.service.InnovateCooperationMaterialsService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 校政企合作附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@RestController
@RequestMapping("cooperation/innovatecooperationmaterials")
public class InnovateCooperationMaterialsController {
    @Autowired
    private InnovateCooperationMaterialsService innovateCooperationMaterialsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateCooperationMaterialsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{materialsId}")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:info")
    public R info(@PathVariable("materialsId") Long materialsId){
		InnovateCooperationMaterialsEntity innovateCooperationMaterials = innovateCooperationMaterialsService.selectById(materialsId);

        return R.ok().put("innovateCooperationMaterials", innovateCooperationMaterials);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:save")
    public R save(@RequestBody InnovateCooperationMaterialsEntity innovateCooperationMaterials){
		innovateCooperationMaterialsService.insert(innovateCooperationMaterials);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:update")
    public R update(@RequestBody InnovateCooperationMaterialsEntity innovateCooperationMaterials){
		innovateCooperationMaterialsService.updateById(innovateCooperationMaterials);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:delete")
    public R delete(@RequestBody Long[] materialsIds){
//		innovateCooperationMaterialsService.deleteBatchIds(Arrays.asList(materialsIds));
		innovateCooperationMaterialsService.deleteList(Arrays.asList(materialsIds));


        return R.ok();
    }

    /**
     * 文件上传
     * @param files
     * @param request
     * @return
     */
    @PostMapping(value = "/upload")
    @RequiresPermissions("cooperation:innovatecooperationmaterials:save")
    public Object uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) {
        String name = request.getParameter("fileName");
//        String UPLOAD_FILES_PATH = ConfigApi.UPLOAD_URL + enterpriseName + "/"+ RandomUtils.getRandomNums()+"/";
        String UPLOAD_FILES_PATH = "innovatecooperationmaterials"+ File.separator + Calendar.getInstance().get(Calendar.YEAR) + File.separator+name + "/"+ RandomUtils.getRandomNums()+"/";
        if (Objects.isNull(files) || files.isEmpty()) {
            return R.error("文件为空，请重新上传");
        }
        InnovateCooperationMaterialsEntity cooperationMaterialsEntity = null;
        for(MultipartFile file : files){
                String fileName = file.getOriginalFilename();
//                result = FileUtils.upLoad(UPLOAD_FILES_PATH, fileName, file);
                OSSUtils.upload2OSS(file, UPLOAD_FILES_PATH + fileName);
                UPLOAD_FILES_PATH += fileName;

                cooperationMaterialsEntity = new InnovateCooperationMaterialsEntity();
                cooperationMaterialsEntity.setAttachPath(UPLOAD_FILES_PATH);
                cooperationMaterialsEntity.setAttachName(fileName);


        }
        return R.ok("文件上传成功")
                .put("cooperationMaterialsEntity", cooperationMaterialsEntity);
    }

    /**
     * 文件下载
     */
    @PostMapping(value = "/download")
    @RequiresPermissions("innovate:innovatecooperationmaterials:list")
    public void downloadFile(final HttpServletResponse response, final HttpServletRequest request) {
        String filePath = request.getParameter("filePath");
        FileUtils.download(response, filePath);
    }
}
