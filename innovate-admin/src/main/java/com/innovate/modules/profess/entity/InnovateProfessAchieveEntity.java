package com.innovate.modules.profess.entity;

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
 * 专创成果
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@Data
@TableName("innovate_profess_achieve")
public class InnovateProfessAchieveEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	@ExcelIgnore
	private Long professAchieveId;
	/**
	 * 负责人id
	 */
	@ExcelIgnore
	private Long professUserId;
	/**
	 * 所属二级学院
	 */
	@ExcelIgnore
	private Long instituteId;
	/**
	 * 所属二级学院名称
	 */
	@TableField(exist = false)
	@ExcelProperty(value = "所属二级学院")
	private String instituteName;
	/**
	 * 获奖(项目）名称
	 */
	@ExcelProperty(value = "获奖名称")
	private String awardAchieName;
	/**
	 * 负责人
	 */
	@ExcelProperty(value = "负责人")
	private String professAchieDirector;
	/**
	 * 团队成员
	 */
	@ExcelProperty(value = "团队成员")
	private String teamMenber;
	/**
	 * 获得时间
	 */
	@ExcelProperty(value = "获得时间")
	@DateTimeFormat("yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date awardTime;
	/**
	 * 专创成果类型（奖励、立项、教改项目、教改论文、教学成果奖、教学过程材料、教学效果）
	 */
	@ExcelProperty(value = "专创成果类型")
	private String professAchieveType;
	/**
	 * 专创成果类型id
	 */
	@ExcelIgnore
	private Long professAchieveTypeId;
	/**
	 * 是否删除
	 */
	@ExcelIgnore
	private Integer isDel;

}
