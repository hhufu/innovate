package com.innovate.modules.cooperation.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.innovate.modules.sys.entity.SysUserEntity;
import lombok.Data;

/**
 * 企业协议管理
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-26 11:24:20
 */
@Data
@TableName("innovate_cooperation_agreement")
public class InnovateCooperationAgreementEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	@ExcelProperty(value = {"自增主键"})
	private Long enterpriseId;
	/**
	 * 企业名称
	 */
	@ExcelProperty(value = {"企业名称"})
	private String enterpriseName;
	/**
	 * 二级学院 innovate_sys_institute主键
	 */
	@ExcelProperty(value = {"二级学院ID"})
	private Long instituteId;
	/**
	 * 年度
	 */
	@ExcelProperty(value = {"年度"})
	private String agreementYear;
	/**
	 * 协议时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	@ExcelProperty(value = {"协议时间"})
	private Date agreementTime;
	/**
	 * 协议材料
	 */
	@ExcelProperty(value = {"协议材料"})
	private String agreementMaterials;
	/**
	 * 企业记录
	 */
	@ExcelProperty(value = {"企业记录"})
	private String enterpriseRecords;
	/**
	 * 创建者
	 */
	@ExcelProperty(value = {"创建者"})
	private Long userId;
	/**
	 * 是否删除
	 */
	@ExcelIgnore
	private Integer isDel;
	/**
	 * 用户信息
	 */
	@TableField(exist = false)
	private SysUserEntity userEntity;

}
