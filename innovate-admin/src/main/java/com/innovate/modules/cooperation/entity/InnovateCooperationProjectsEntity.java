package com.innovate.modules.cooperation.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.innovate.modules.sys.entity.SysUserEntity;
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
	@ExcelIgnore
	private Long cooperationId;
	/**
	 * 企业名称
	 */
	@ExcelProperty(value = {"企业名称"})
	private String enterpriseName;
	/**
	 * 项目名称
	 */
	@ExcelProperty(value = {"项目名称"})
	private String projectName;
	/**
	 * 二级学院  学院表主键
	 */
	@ExcelProperty(value = {"二级学院ID"})
	private Long instituteId;
	/**
	 * 年度
	 */
	@ExcelProperty(value = {"年度"})
	private String cooperationYear;
	/**
	 * 主持人
	 */
	@ExcelIgnore
	private Long userId;
	/**
	 * 起始时间
	 */
	@DateTimeFormat("yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelProperty(value = {"起始时间"})
	private Date startTime;
	/**
	 * 截止时间
	 */
	@DateTimeFormat("yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelProperty(value = {"截止时间"})
	private Date endTime;
	/**
	 * 删除状态
	 */
	@ExcelIgnore
	private Integer isDel;
	/**
	 * 用户信息
	 */
	@TableField(exist = false)
	@ExcelIgnore
	private SysUserEntity userEntity;
	/**
	 * 企业登记表主键
	 */
	@ExcelIgnore
	private Long authenticationId;
}
