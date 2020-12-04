package com.innovate.modules.enterprise.controller;

import java.io.File;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.innovate.common.utils.OSSUtils;
import com.innovate.modules.finish.entity.FinishAttachEntity;
import com.innovate.modules.innovate.config.ConfigApi;
import com.innovate.modules.util.FileUtils;
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
import javax.servlet.http.HttpServletResponse;


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
    @RequiresPermissions("enterprise:innovateenterpriseinfo:save")
    public Object uploadFile(@RequestParam("file") List<MultipartFile> files,HttpServletRequest request) {
        String enterpriseName = request.getParameter("enterpriseName");
        String attachType = request.getParameter("attachType");
        String UPLOAD_FILES_PATH = "enterprise"+ File.separator + Calendar.getInstance().get(Calendar.YEAR) + File.separator+enterpriseName + "/"+ RandomUtils.getRandomNums()+"/";
        if (Objects.isNull(files) || files.isEmpty()) {
            return R.error("文件为空，请重新上传");
        }
        InnovateEnterpriseAttachEntity innovateEnterpriseAttachEntity=null;
        for(MultipartFile file : files){
            String fileName = file.getOriginalFilename();
            OSSUtils.upload2OSS(file,UPLOAD_FILES_PATH+fileName);
            UPLOAD_FILES_PATH += fileName;
            innovateEnterpriseAttachEntity = new InnovateEnterpriseAttachEntity();
            innovateEnterpriseAttachEntity.setAttachName(fileName);
            innovateEnterpriseAttachEntity.setAttachPath(UPLOAD_FILES_PATH);
            innovateEnterpriseAttachEntity.setAttachType(Integer.parseInt(attachType));
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
		InnovateEnterpriseAttachEntity innovateEnterpriseAttach = innovateEnterpriseAttachService.selectOne(new EntityWrapper<InnovateEnterpriseAttachEntity>()
        .eq("attach_id",attachId).eq("is_del",0));

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
        innovateEnterpriseAttachService.delList(Arrays.asList(attachIds));
//		innovateEnterpriseAttachService.deleteBatchIds(Arrays.asList(attachIds));

        return R.ok();
    }




}
