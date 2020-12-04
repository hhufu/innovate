package com.innovate.modules.enterprise.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 入驻企业表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@Data
@TableName("innovate_enterprise_info")
public class InnovateEnterpriseInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键（入驻企业id）
	 */
	@TableId
	@ExcelIgnore
	private Long settledEnterpId;
	/**
	 * 企业名称
	 */
	@ExcelProperty(value = "企业名称")
	private String enterpriseName;
	/**
	 * 用户ID
	 */
	@ExcelIgnore
	private Long enterpriseUserId;
	/**
	 * 企业负责人姓名
	 */
	@ExcelProperty(value = "企业负责人姓名")
	private String enterpriseDirector;

	/**
	 * 负责人所在学院ID
	 */
	@ExcelIgnore
	private String instituteId;
	/**
	 * 负责人所在学院
	 */
	@ExcelProperty(value = "负责人所在学院")
	private String departmentDirector;
	/**
	 * 入驻时间
	 */
	@ExcelProperty(value = "入驻时间")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	private Date settledTime;
	/**
	 * 企业类型
	 */
	@ExcelProperty(value = "企业类型")
	private String enterpriseType;
	/**
	 * 主要经营范围
	 */
	@ExcelProperty(value = "主要经营范围")
	private String businessScope;
	/**
	 * 审核状态
	 */
	@ExcelIgnore
	private String applyStatus;
	/**
	 * 是否删除
	 */
	@ExcelIgnore
	private Integer isDel;

}
