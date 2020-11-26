package com.innovate.modules.cooperation.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 校政企合作项目
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:21
 */
@Data
@TableName("innovate_cooperation_projects")
public class InnovateCooperationProjectsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long cooperationId;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 二级学院  学院表主键
	 */
	private Long instituteId;
	/**
	 * 企业名称
	 */
	private String enterpriseName;
	/**
	 * 年度
	 */
	private String cooperationYear;
	/**
	 * 主持人
	 */
	private Long userId;
	/**
	 * 起始时间
	 */
	private Date startTime;
	/**
	 * 截止时间
	 */
	private Date endTime;

}
