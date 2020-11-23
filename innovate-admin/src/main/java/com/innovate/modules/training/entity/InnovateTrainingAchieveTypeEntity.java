package com.innovate.modules.training.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 实训成果类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:44:45
 */
@Data
@TableName("innovate_training_achieve_type")
public class InnovateTrainingAchieveTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long materialTypeId;
	/**
	 * 实训成果类型
	 */
	private String trainingAchieveType;

}
