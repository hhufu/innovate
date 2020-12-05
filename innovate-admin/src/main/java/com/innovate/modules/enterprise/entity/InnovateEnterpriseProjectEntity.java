package com.innovate.modules.enterprise.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 企业项目表
 *
 * @author HHUFU
 * @email biaogejiushibiao@outlook.com
 * @date 2020-11-24 00:42:31
 */
@Data
@TableName("innovate_enterprise_project")
public class InnovateEnterpriseProjectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ExcelIgnore
    @TableId
    private Long enterpProjId;
    /**
     * 企业 id
     */
    @ExcelIgnore
    private Long enterpriseId;
    /**
     * 企业名称
     */
    @ExcelProperty(value = "企业名称")
    private String enterpriseName;
    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectName;
    /**
     * 项目开始时间
     */
    @ExcelProperty(value = "项目开始时间")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    private Date projStartTime;
    /**
     * 截止时间
     */
    @ExcelProperty(value = "截止时间")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    private Date projStopTime;
    /**
     * 项目年度
     */
    @ExcelProperty(value = "项目年度")
    private String projectYear;
    /**
     * 项目负责人
     */
    @ExcelProperty(value = "项目负责人")
    private String projectDirector;
    /**
     * 用户id
     */
    @ExcelIgnore
    private Long projectUserId;
    /**
     * 审核状态
     */
    @ExcelIgnore
    private String applyStatus;
    /**
     * 是否删除
     */
	@ExcelIgnore
    private Integer isDel;
}
