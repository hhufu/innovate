package com.innovate.modules.cooperation.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 校政企合作附件
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-25 21:54:33
 */
@Data
@TableName("innovate_cooperation_materials")
public class InnovateCooperationMaterialsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long materialsId;
	/**
	 * 模糊id P-企业项目表id  A-企业合作表id
	 */
	private String functionId;
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
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date attachTime;
	/**
	 * 是否删除
	 */
	private Integer isDel;

}
