package com.innovate.modules.points.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
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
	 * 申请时间
	 */
	private Date applyTime;
	/**
	 * 是否删除
	 */
	private Integer isDel;
	/**
	 * 积分标准表id
	 */
	private Long integralId;
	/**
	 * 参加类型
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
	 * 参与人类别（1：负责人，2参与成员）
	 */
	private String participateType;

}
