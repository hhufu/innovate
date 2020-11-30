package com.innovate.modules.enterprise.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 企业附件表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@Data
@TableName("innovate_enterprise_attach")
public class InnovateEnterpriseAttachEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 附件id
	 */
	@TableId
	private Long attachId;
	/**
	 * 模糊id  企业成果材料：A-企业成果表id  企业入驻材料：E-入驻企业表id
	 */
	private Long functionId;
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
	@JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
	private Date attachTime;
	/**
	 * 附件类型（1、入驻材料 2、项目材料 3、企业成果材料）
	 */
	private Integer attachType;
	/**
	 * 是否删除
	 */
	private Integer isDel;

}
