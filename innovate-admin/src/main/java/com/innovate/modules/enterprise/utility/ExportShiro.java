package com.innovate.modules.enterprise.utility;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import java.util.Map;

//导出权限判断
public class ExportShiro<T> {
    public EntityWrapper<T> queryExport(Map<String, Object> params, EntityWrapper<T> wrapper){
        //企业名称 搜索信息
        Object enterpriseName = params.get("enterpriseName");
        //入驻时间 搜索信息
        Object settledTime = params.get("settledTime");
        //获奖时间 搜索信息
        Object awardTime = params.get("awardTime");
        //用户id
        Object userId = params.get("enterpriseUserId");
        //学院id
        Object instituteId = params.get("instituteId");

        wrapper.eq("is_del", 0)
                .eq("apply_status",params.get("apply_status"));

        if (userId!=null ){ //空为管理员 不执行
            if (instituteId!=null){   //非空 二级学院管理员
                wrapper.eq("institute_id", instituteId.toString());
            }else {  //无管理权限 只获取自己得信息
                wrapper.eq("enterprise_user_id", userId.toString());
            }
        }
        if (enterpriseName != null && !"".equals(enterpriseName)){  //根据企业名称搜索
            wrapper.like("enterprise_name" , enterpriseName.toString());
        }
        if (settledTime != null && !"".equals(settledTime)){  //根据入驻时间搜索
            wrapper.where("YEAR(settled_time)={0}" , settledTime.toString());
        }
        if (awardTime != null && !"".equals(awardTime)){  //根据获奖时间搜索
            wrapper.where("YEAR(award_time)={0}" , awardTime.toString());
        }

        return wrapper;
    }
}
