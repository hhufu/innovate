package com.innovate.modules.points.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.innovate.modules.sys.entity.SysUserEntity;
import com.innovate.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.modules.points.entity.InnovateStudentPointsApplyEntity;
import com.innovate.modules.points.service.InnovateStudentPointsApplyService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 学生积分申请表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
@RestController
@RequestMapping("points/innovatestudentpointsapply")
public class InnovateStudentPointsApplyController {
    @Autowired
    private InnovateStudentPointsApplyService innovateStudentPointsApplyService;
    @Autowired
    private SysUserService sysUserService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("points:innovatestudentpointsapply:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateStudentPointsApplyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{integralApplyId}")
    @RequiresPermissions("points:innovatestudentpointsapply:info")
    public R info(@PathVariable("integralApplyId") Long integralApplyId){
		InnovateStudentPointsApplyEntity innovateStudentPointsApply = innovateStudentPointsApplyService.selectById(integralApplyId);
		// 获取申请用户信息
        innovateStudentPointsApply.setUserEntity(sysUserService.selectById(innovateStudentPointsApply.getApplyUserId()));
        return R.ok().put("innovateStudentPointsApply", innovateStudentPointsApply);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("points:innovatestudentpointsapply:save")
    public R save(@RequestBody InnovateStudentPointsApplyEntity innovateStudentPointsApply){
        innovateStudentPointsApply.setApplyTime(new Date());
		innovateStudentPointsApplyService.insert(innovateStudentPointsApply);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("points:innovatestudentpointsapply:update")
    public R update(@RequestBody InnovateStudentPointsApplyEntity innovateStudentPointsApply){
		innovateStudentPointsApplyService.updateById(innovateStudentPointsApply);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("points:innovatestudentpointsapply:delete")
    public R delete(@RequestBody Long[] integralApplyIds){
		innovateStudentPointsApplyService.deleteBatchIds(Arrays.asList(integralApplyIds));

        return R.ok();
    }

}
