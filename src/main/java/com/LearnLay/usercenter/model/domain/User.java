package com.LearnLay.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * User
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * User name
     */
    private String username;

    /**
     * User profile picture 
     */
    private String avatarUrl;

    /**
     * User gender
     */
    private Integer gender;

    /**
     * User Account
     */
    private String userAccount;

    /**
     * User Password
     */
    private String userPassword;

    /**
     * User Phone Number
     */
    private String phone;

    /**
     * User Email
     */
    private String email;

    /**
     * User Status
     */
    private Integer userStatus;

    /**
     * Account Create Time
     */
    private Date createTime;

    /**
     * Update Time
     */
    private Date updateTime;

    /**
     * is Delete
     */
    @TableLogic
    private Integer isDelete;

    /**
     * Role 0-General User，1-Admin
     */
    private Integer userRole;

    /**
     * User Code
     */
    private String idCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}