package com.innovate.modules.profess.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
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
	private Long professAchieveId;
	/**
	 * 负责人id
	 */
	private Long professUserId;
	/**
	 * 所属二级学院
	 */
	private Long instituteId;
	/**
	 * 获奖(项目）名称
	 */
	private String awardAchieName;
	/**
	 * 负责人
	 */
	private String professAchieDirector;
	/**
	 * 团队成员
	 */
	private String teamMenber;
	/**
	 * 获得时间
	 */
	private Date awardTime;
	/**
	 * 专创成果类型（奖励、立项、教改项目、教改论文、教学成果奖、教学过程材料、教学效果）
	 */
	private String professAchieveType;
	/**
	 * 专创成果类型id
	 */
	private Long professAchieveTypeId;
	/**
	 * 是否删除
	 */
	private Integer isDel;

}
