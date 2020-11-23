package com.innovate.modules.enterprise.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 企业项目表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@Data
@TableName("innovate_enterprise_project")
public class InnovateEnterpriseProjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long enterpProjId;
	/**
	 * 企业 id
	 */
	private Long enterpriseId;
	/**
	 * 企业名称
	 */
	private String enterpriseName;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 项目开始时间
	 */
	private Date projStartTime;
	/**
	 * 截止时间
	 */
	private Date projStopTime;
	/**
	 * 项目年度
	 */
	private String projectYear;
	/**
	 * 项目负责人
	 */
	private String projectDirector;
	/**
	 * 用户id
	 */
	private Long projectUserId;
	/**
	 * 是否删除
	 */
	private Integer isDel;

}
