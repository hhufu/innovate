package com.innovate.modules.points.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 学生积分申请附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:45:59
 */
@Data
@TableName("innovate_student_points_attach")
public class InnovateStudentPointsAttachEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long attachId;
	/**
	 * 积分申请id
	 */
	private Long pointsApplyId;
	/**
	 * 附件名称
	 */
	private String attachName;
	/**
	 * 附件路径
	 */
	private String attachPath;
	/**
	 * 上传时间
	 */
	private Date attachTime;
	/**
	 * 是否删除
	 */
	private Integer isDel;

}
