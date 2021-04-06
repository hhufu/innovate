package com.innovate.modules.innovate.entity;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 申报流程设置
 *
 * @author HHUFU
 * @email 437213666@qq.com
 * @date 2021-02-03 11:43:20
 */
@Data
@TableName("innovate_declaration_process_setting")
public class InnovateDeclarationProcessSettingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long dpsId;
	/**
	 * 申报流程名称 1：大创申报 3 大创结题
	 */
	private Integer declareProcessName;
	/**
	 * 开始时间
	 */
	@DateTimeFormat("yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp startTime;
	/**
	 * 截止时间
	 */
	@DateTimeFormat("yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp endTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 评分细则名称
	 */
	private String attachName;
	/**
	 * 评分细则路径
	 */
	private String attachPath;

}
