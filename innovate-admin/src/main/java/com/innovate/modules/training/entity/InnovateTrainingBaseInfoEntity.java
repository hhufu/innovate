package com.innovate.modules.training.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 实训基地信息
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@Data
@TableName("innovate_training_base_info")
public class InnovateTrainingBaseInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	@ExcelIgnore
	private Long trainingBaseId;
	/**
	 * 实习实训基地名称
	 */
	@ExcelProperty(value = "实习实训基地名称")
	private String trainingBaseName;
	/**
	 * 所属二级学院id
	 */
	private Long instituteId;
	/**
	 * 所属二级学院名称
	 */
	@TableField(exist = false)
	@ExcelProperty(value = "所属二级学院")
	private String instituteName;
	/**
	 * 建立时间
	 */
	@DateTimeFormat("yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date baseCreateTime;
	/**
	 * 是否删除
	 */
	@ExcelIgnore
	private Integer isDel;

}
