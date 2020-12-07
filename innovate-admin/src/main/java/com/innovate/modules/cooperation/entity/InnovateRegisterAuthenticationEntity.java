package com.innovate.modules.cooperation.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 企业登记认证表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@Data
@TableName("innovate_register_authentication")
public class InnovateRegisterAuthenticationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	@ExcelIgnore
	private Long authenticationId;
	/**
	 * 企业名称
	 */
	@ExcelProperty(value = "企业名称")
	private String enterpriseName;
	/**
	 * 法人代表
	 */
	@ExcelProperty(value = "法人代表")
	private String corporateRepresentative;
	/**
	 * 统一信用代码
	 */
	@ExcelProperty(value = "统一信用代码")
	private String creditCode;
	/**
	 * 企业类型
	 */
	@ExcelProperty(value = "企业类型")
	private String enterpriseType;
	/**
	 * 是否删除
	 */
	@ExcelIgnore
	private Integer isDel;

}
