package com.innovate.modules.match.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 双创赛事签署意见
 *
 * @author 
 * @email 
 * @date 2021-04-29 15:58:05
 */
@Data
@TableName("innovate_match_signing_opinion")
public class MatchSigningOpinionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 签署意见id
	 */
	@TableId
	private Long signingOpinionsId;
	/**
	 * 双创申请id
	 */
	private Long matchId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 签署意见
	 */
	private String signingOpinion;
	/**
	 * 签署意见时间
	 */
	private Date signingOpinionTime;
	/**
	 * 
	 */
	private Integer isDel;
	/**
	 * 签署类别1是指导老师2是二级学院
	 */
	private Long signType;

}
