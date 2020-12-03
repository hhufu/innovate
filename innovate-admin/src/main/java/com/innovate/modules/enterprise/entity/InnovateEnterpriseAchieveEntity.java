package com.innovate.modules.enterprise.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotations.TableField;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 企业成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:32
 */
@Data
@TableName("innovate_enterprise_achieve")
public class InnovateEnterpriseAchieveEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	@ExcelIgnore
	private Long enterpAchieveId;
	/**
	 * 企业 id
	 */
	@ExcelIgnore
	private Long enterpriseId;
	/**
	 * 企业名称
	 */
	@ExcelProperty(value = "企业名称")
	private String enterpriseName;
	/**
	 * 负责人
	 */
	@ExcelProperty(value = "负责人")
	private String enterpriseDirector;
	/**
	 * 用户id
	 */
	@ExcelIgnore
	private Long enterpriseUserId;
	/**
	 * 获奖名称（项目名称）
	 */
	@ExcelProperty(value = "获奖名称")
	private String awardProjectName;
	/**
	 * 获奖时间
	 */
	@DateTimeFormat("yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelProperty(value = "获奖时间")
	private Date awardTime;
	/**
	 * 类型（应用成果转化/获奖/著作权/企业证书）
	 */
	@ExcelIgnore
	private String awardProjectType;
	/**
	 * 所属二级学院
	 */
	@ExcelIgnore
	private String instituteId;
	/**
	 * 是否删除
	 */
	@ExcelIgnore
	private Integer isDel;

	@ExcelProperty(value = "类型")
	@TableField(exist = false)
	private String typeName;

	@ExcelProperty(value = "所属二级学院")
	@TableField(exist = false)
	private String instituteName;

}
