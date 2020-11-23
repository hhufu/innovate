package com.innovate.modules.points.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.modules.points.entity.InnovateStudentPointsAttachEntity;
import com.innovate.modules.points.service.InnovateStudentPointsAttachService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 学生积分申请附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
@RestController
@RequestMapping("points/innovatestudentpointsattach")
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

}
