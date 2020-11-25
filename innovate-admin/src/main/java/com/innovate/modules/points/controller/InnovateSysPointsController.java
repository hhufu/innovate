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

import com.innovate.modules.points.entity.InnovateSysPointsEntity;
import com.innovate.modules.points.service.InnovateSysPointsService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 积分标准
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
@RestController
@RequestMapping("points/innovatesyspoints")
public class InnovateSysPointsController {
    @Autowired
    private InnovateSysPointsService innovateSysPointsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("points:innovatesyspoints:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = innovateSysPointsService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{integralId}")
    @RequiresPermissions("points:innovatesyspoints:info")
    public R info(@PathVariable("integralId") Long integralId){
		InnovateSysPointsEntity innovateSysPoints = innovateSysPointsService.selectById(integralId);

        return R.ok().put("innovateSysPoints", innovateSysPoints);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("points:innovatesyspoints:save")
    public R save(@RequestBody InnovateSysPointsEntity innovateSysPoints){
		innovateSysPointsService.insert(innovateSysPoints);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("points:innovatesyspoints:update")
    public R update(@RequestBody InnovateSysPointsEntity innovateSysPoints){
		innovateSysPointsService.updateAllColumnById(innovateSysPoints);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("points:innovatesyspoints:delete")
    public R delete(@RequestBody Long[] integralIds){
        for (Long l: integralIds) {
            InnovateSysPointsEntity innovateSysPoints = new InnovateSysPointsEntity();
            innovateSysPoints.setIntegralId(l);
            innovateSysPoints.setIsDel(1);
            innovateSysPointsService.updateById(innovateSysPoints);
        }
//		innovateSysPointsService.deleteBatchIds(Arrays.asList(integralIds));

        return R.ok();
    }

}
