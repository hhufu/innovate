package com.innovate.modules.points.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.innovate.common.utils.R;
import com.innovate.modules.innovate.entity.UserPersonInfoEntity;
import com.innovate.modules.innovate.service.UserPerInfoService;
import com.innovate.modules.sys.entity.SysUserEntity;
import com.innovate.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innovate.common.utils.PageUtils;
import com.innovate.common.utils.Query;

import com.innovate.modules.points.dao.InnovateStudentPointsDao;
import com.innovate.modules.points.entity.InnovateStudentPointsEntity;
import com.innovate.modules.points.service.InnovateStudentPointsService;


@Service("innovateStudentPointsService")
public class InnovateStudentPointsServiceImpl extends ServiceImpl<InnovateStudentPointsDao, InnovateStudentPointsEntity> implements InnovateStudentPointsService {

    @Autowired
    private UserPerInfoService userPerInfoService;
    @Autowired
    private SysUserService sysUserService;
    /**
     * 查询列表
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Integer totalPage  = baseMapper.queryCountPage(params);
        Integer currPage  = 1;
        Integer pageSize  = 10;
        try {
            if (params.get("currPage")!=null&&params.get("pageSize")!=null) {

                currPage = Integer.parseInt(params.get("currPage").toString());
                pageSize = Integer.parseInt(params.get("pageSize").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer startPage = 0 + pageSize * (currPage - 1);
        Integer endPage = pageSize;

        params.put("startPage", startPage);
        params.put("endPage", endPage);

        List<InnovateStudentPointsEntity> innovateStudentPointsEntities = baseMapper.queryPageByMap(params);
//        //根据学号查询&&非管理员
//        if (params.get("userId")!=null){
//            UserPersonInfoEntity user = userPerInfoService.queryUserByUserId(Long.parseLong(params.get("userId").toString()));
//            entityWrapper.eq("stu_num",user.getPerStuNo());
//        }

        return new PageUtils(innovateStudentPointsEntities, totalPage, pageSize, currPage);
    }

    /**
     * 查询总积分
     * @param params
     * @return
     */
    @Override
    public Integer queryPointByParams(Map<String, Object> params) {

        EntityWrapper<InnovateStudentPointsEntity> entityWrapper = new EntityWrapper<>();

        UserPersonInfoEntity user = userPerInfoService.queryUserByUserId(Long.parseLong(params.get("userId").toString()));

        entityWrapper.eq("stu_num",user.getPerStuNo());

        List<InnovateStudentPointsEntity> innovateStudentPointsEntities = this.selectList(entityWrapper);

        return innovateStudentPointsEntities.stream().mapToInt(value -> value.getPoints()).sum();
    }


    public R insertAndCheck(InnovateStudentPointsEntity innovateStudentPoints) {
        //判断是否存在该学生学号
        SysUserEntity sysUserEntity = sysUserService.queryByUserName(innovateStudentPoints.getStuNum());
        if (sysUserEntity == null) {
            return R.error("该学号不存在");
        }

        //判断是否已经签到

        EntityWrapper<InnovateStudentPointsEntity> entityWrapper = new EntityWrapper<>();

        entityWrapper.eq("activity_id",innovateStudentPoints.getActivityId());
        entityWrapper.eq("stu_num",innovateStudentPoints.getStuNum());

        InnovateStudentPointsEntity innovateStudentPointsEntity = this.selectOne(entityWrapper);

        //不可重复签到
        if (innovateStudentPointsEntity==null){
            innovateStudentPoints.setOperationTime(new Timestamp(System.currentTimeMillis()));
            insert(innovateStudentPoints);
            return R.ok();
        }else return R.error("你已经签到过了，请勿重复签到");
    }

    @Override
    public List<InnovateStudentPointsEntity> queryListByIds(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        map.put("stuNum", null);
        if (params.get("ids") != null && !params.get("ids").toString().equals("[]")) {
            map.put("ids", params.get("ids"));
        } else {
            map.put("ids", null);
        }
        map.put("stuNum", params.get("stuNum"));
        map.put("instituteId", params.get("instituteId"));

        return baseMapper.selectPointsIds(map);
    }


}
