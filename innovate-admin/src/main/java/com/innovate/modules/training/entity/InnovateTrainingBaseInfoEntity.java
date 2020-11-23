package com.innovate.modules.training.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
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
	private Long trainingBaseId;
	/**
	 * 实习实训基地名称
	 */
	private String trainingBaseName;
	/**
	 * 所属二级学院
	 */
	private String instituteId;
	/**
	 * 建立时间
	 */
	private Date baseCreateTime;
	/**
	 * 是否删除
	 */
	private Integer isDel;

}
