package com.innovate.modules.points.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 积分标准
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
@Data
@TableName("innovate_sys_points")
public class InnovateSysPointsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long integralId;
	/**
	 * 参加类型
	 */
	private String participateType;
	/**
	 * 比赛级别（等级、项目）
	 */
	private String raceGrade;
	/**
	 * 奖项等级
	 */
	private String prizeGrade;
	/**
	 * 参与人类别（1：负责人，2参与成员）
	 */
	private Integer persionType;
	/**
	 * 积分
	 */
	private Integer integral;
	/**
	 * 是否删除
	 */
	private Integer isDel;

}
