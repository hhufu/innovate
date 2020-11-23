package com.innovate.modules.training.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 实训基地附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@Data
@TableName("innovate_training_base_attach")
public class InnovateTrainingBaseAttachEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 附件id
	 */
	@TableId
	private Long attachId;
	/**
	 * 实训基地成果id
	 */
	private Long trainingAchieveId;
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
