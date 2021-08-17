package com.innovate.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author luo
 * @email 1275986528@qq.com
 * @date 2021-07-13 11:01:39
 */
@Data
@TableName("sys_mail_code")
public class SysMailCodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 *
	 */
	private Long userId;
	/**
	 *
	 */
	private String mailCode;
	/**
	 *
	 */
	private Date times;
	/**
	 *
	 */
	private Integer invalid;

	public SysMailCodeEntity(){
	}
	public SysMailCodeEntity(Long userId) {
		this.userId = userId;
	}
}
