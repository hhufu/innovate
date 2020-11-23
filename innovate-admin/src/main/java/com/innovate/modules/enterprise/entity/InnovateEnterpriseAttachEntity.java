package com.innovate.modules.enterprise.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
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
	 * 模糊id
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
