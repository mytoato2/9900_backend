package com.example.demo.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 
 * @TableName project
 */
@TableName(value ="project")
@Data
public class Project implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long projectID;

    /**
     * 
     */
    private String projectName;

    /**
     * 
     */
    private String details;

    /**
     * the ID of client who created it
     */
    private Long projectOwner;

    /**
     * IDList of those group who choosed this group as first reference
     */
    private String readyGroupIDList;

    /**
     * those who are choosed finally
     */
    private String finalGroupList;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    @JsonIgnore
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}