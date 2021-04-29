package com.innovate.modules.match.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.innovate.modules.match.entity.MatchSigningOpinionEntity;
import com.innovate.modules.match.service.MatchSigningOpinionService;
import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.R;



/**
 * 双创赛事签署意见
 *
 * @author 
 * @email 
 * @date 2021-04-29 15:58:05
 */
@RestController
@RequestMapping("innovate/match/signingopinion")
public class MatchSigningOpinionController {
    @Autowired
    private MatchSigningOpinionService matchSigningOpinionService;


    /**
     * 添加签署意见
     * @param params
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions("innovate:signingopinion:save")
    public R addSigningOpinion(@RequestParam Map<String, Object> params){

        matchSigningOpinionService.addSigningOpinion(params);

        return R.ok();
    }
    /**
     * 查询签署意见
     * @param params
     * @return
     */
    @GetMapping("/info")
    @RequiresPermissions("innovate:match:info")
    public R queryByMatchId(@RequestParam Map<String, Object> params){

        return R.ok().put("sighingOpinion",matchSigningOpinionService.querySigningOpinionByMatchIdAndType(params));

    }

    /**签署意见
     * 删除
     * @param params
     */
    @PostMapping("/delete")
    @RequiresPermissions("innovate:signingopinion:delete")
    public R remove(@RequestParam Map<String, Object> params){

        matchSigningOpinionService.remove(params);

        return R.ok();

    }

}
