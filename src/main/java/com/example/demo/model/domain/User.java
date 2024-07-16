package com.example.demo.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
//    @JsonIgnore
    private Long id;

    /**
     * 0 - admin; 1 - tutor; 2 - client; 3 - student
     */
    private Integer userRole;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    @JsonIgnore
    private String userPassword;

    /**
     * 
     */
    private String zID;

    /**
     * 
     */
    private String groupName;

    /**
     * list of project ID
     */
    private String preference;

    /**
     * 
     */
    private String company;

    /**
     * 
     */
    private String jobTitle;

    /**
     * 
     */
    private String avatarUrl;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    @TableLogic
    @JsonIgnore
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}