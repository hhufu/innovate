package com.innovate.modules.enterprise.controller;

import java.io.File;
import java.util.*;

import com.innovate.common.utils.OSSUtils;
import com.innovate.modules.finish.entity.FinishAttachEntity;
import com.innovate.modules.util.RandomUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.enterprise.entity.InnovateEnterpriseAttachEntity;
import com.innovate.modules.enterprise.service.InnovateEnterpriseAttachService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * 企业附件表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@RestController
@RequestMapping("enterprise/innovateenterpriseattach")
public class InnovateEnterpriseAttachController {
    @Autowired
    private InnovateEnterpriseAttachService innovateEnterpriseAttachService;


    /**
     * 文件上传
     * @param files
     * @param request
     * @return
     */
    @PostMapping(value = "/upload")
    @RequiresPermissions("enterprise:innovateenterpriseattach:save")
    public Object uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) {
        String enterpriseName = request.getParameter("enterpriseName");
//        String UPLOAD_FILES_PATH = ConfigApi.UPLOAD_URL + finishName + "/"+ RandomUtils.getRandomNums()+"/";
        String UPLOAD_FILES_PATH = "enterprise"+ File.separator + Calendar.getInstance().get(Calendar.YEAR) + File.separator+enterpriseName + "/"+ RandomUtils.getRandomNums()+"/";
        if (Objects.isNull(files) || files.isEmpty()) {
            return R.error("文件为空，请重新上传");
        }
        InnovateEnterpriseAttachEntity innovateEnterpriseAttachEntity=null;
        for(MultipartFile file : files){

            String fileName = file.getOriginalFilename();
//                result = FileUtils.upLoad(UPLOAD_FILES_PATH, fileName, file);
            OSSUtils.upload2OSS(file,UPLOAD_FILES_PATH+fileName);
            UPLOAD_FILES_PATH += fileName;
            innovateEnterpriseAttachEntity = new InnovateEnterpriseAttachEntity();
            innovateEnterpriseAttachEntity.setAttachName(fileName);
            innovateEnterpriseAttachEntity.setAttachPath(UPLOAD_FILES_PATH);
            innovateEnterpriseAttachEntity.setAttachType(1);
            //            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            innovateEnterpriseAttachEntity.setAttachTime(new Date());// new Date()为获取当前系统时间
        }
        return R.ok("文件上传成功")
                .put("innovateEnterpriseAttachEntity", innovateEnterpriseAttachEntity);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("enterprise:innovateenterpriseattach:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateEnterpriseAttachService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attachId}")
    @RequiresPermissions("enterprise:innovateenterpriseattach:info")
    public R info(@PathVariable("attachId") Long attachId){
		InnovateEnterpriseAttachEntity innovateEnterpriseAttach = innovateEnterpriseAttachService.selectById(attachId);

        return R.ok().put("innovateEnterpriseAttach", innovateEnterpriseAttach);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("enterprise:innovateenterpriseattach:save")
    public R save(@RequestBody InnovateEnterpriseAttachEntity innovateEnterpriseAttach){
		innovateEnterpriseAttachService.insert(innovateEnterpriseAttach);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("enterprise:innovateenterpriseattach:update")
    public R update(@RequestBody InnovateEnterpriseAttachEntity innovateEnterpriseAttach){
		innovateEnterpriseAttachService.updateById(innovateEnterpriseAttach);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("enterprise:innovateenterpriseattach:delete")
    public R delete(@RequestBody Long[] attachIds){
		innovateEnterpriseAttachService.deleteBatchIds(Arrays.asList(attachIds));

        return R.ok();
    }

}
