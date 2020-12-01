package com.innovate.modules.profess.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 专创成果类型
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@Data
@TableName("innovate_profess_achieve_type")
public class InnovateProfessAchieveTypeEntity implements Serializable {
	/**
	 * 自增主键
	 */
	@TableId
	private Long professAchieveTypeId;
	/**
	 * 实训成果类型
	 */
	private String professAchieveType;

}
