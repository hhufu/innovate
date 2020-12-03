package com.innovate.modules.training.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.innovate.modules.sys.entity.SysUserEntity;
import lombok.Data;

/**
 * 实训基地成果表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@Data
@TableName("innovate_training_base_achieve")
public class InnovateTrainingBaseAchieveEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	@ExcelIgnore
	private Long trainingAchieveId;
	/**
	 * 基地名称
	 */
	@ExcelProperty(value = "基地名称")
	private String trainingBaseName;
	/**
	 * 材料年度
	 */
	@ExcelProperty(value = "材料年度")
	private String materialYear;
	/**
	 * 材料类型
	 */
	@ExcelProperty(value = "材料类型")
	private String materialType;
	/**
	 * 材料类型id
	 */
	@ExcelIgnore
	private Long materialTypeId;
	/**
	 * 实训基地id
	 */
	@ExcelIgnore
	private Long trainingBaseId;
	/**
	 * 所属二级学院名称
	 */
	@TableField(exist = false)
	@ExcelProperty(value = "所属二级学院名称")
	private String instituteName;
	/**
	 * 所属二级学院
	 */
	@ExcelIgnore
	private Long instituteId;
	/**
	 * 是否删除
	 */
	@ExcelIgnore
	private Integer isDel;
	/**
	 * 用户信息
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private SysUserEntity userEntity;
}
