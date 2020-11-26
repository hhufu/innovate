package com.innovate.modules.cooperation.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 企业协议管理
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@Data
@TableName("innovate_cooperation_agreement")
public class InnovateCooperationAgreementEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long enterpriseId;
	/**
	 * 企业名称
	 */
	private String enterpriseName;
	/**
	 * 二级学院
	 */
	private String secondaryCollege;
	/**
	 * 年度
	 */
	private String agreementYear;
	/**
	 * 
	 */
	private Date agreementTime;
	/**
	 * 协议材料
	 */
	private String agreementMaterials;
	/**
	 * 企业记录
	 */
	private String enterpriseRecords;

}
