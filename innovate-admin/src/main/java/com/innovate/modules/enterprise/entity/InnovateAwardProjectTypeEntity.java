package com.innovate.modules.enterprise.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 企业获奖项目类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@Data
@TableName("innovate_award_project_type")
public class InnovateAwardProjectTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long awardProjectjTypeId;
	/**
	 * 获奖项目类型
	 */
	private String awardProjectjType;

}
