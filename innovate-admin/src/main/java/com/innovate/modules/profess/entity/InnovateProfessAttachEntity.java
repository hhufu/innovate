package com.innovate.modules.profess.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 专创附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:40:46
 */
@Data
@TableName("innovate_profess_attach")
public class InnovateProfessAttachEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 附件id
	 */
	@TableId
	private Long attachId;
	/**
	 * 专创结合成果id
	 */
	private Long professAchieveId;
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
