package com.innovate.modules.points.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.innovate.modules.sys.entity.SysUserEntity;
import lombok.Data;

/**
 * 学生积分申请表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
@Data
@TableName("innovate_student_points_apply")
public class InnovateStudentPointsApplyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long integralApplyId;
	/**
	 * 积分标准id
	 */
	private Long sysPointsId;
	/**
	 * 申请人
	 */
	private Long applyUserId;
	/**
	 * 申请时间
	 */
	private Date applyTime;
	/**
	 * 是否删除
	 */
	private Integer isDel;
	/**
	 * 是否删除
	 */
	private Integer applyStatus;
	/**
	 * 申请积分
	 */
	private Long applyIntegral;
	/**
	 * 参与人类别（1：负责人，2参与成员）
	 */
	private Integer persionType;
	/**
	 * 奖项等级
	 */
	private String prizeGrade;
	/**
	 * 比赛级别（等级、项目）
	 */
	private String raceGrade;
	/**
	 * 申请类型
	 */
	private String participateType;
	// 用户信息
	@TableField(exist = false)
	private SysUserEntity userEntity;

}
