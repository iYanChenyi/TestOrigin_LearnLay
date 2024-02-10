package com.LearnLay.usercenter.model.DTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class UserUpdateDTO {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * User name
     */
    private String username;

    /**
     * User Profile Picture
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
     * User phone number
     */
    private String phone;

    /**
     * User email
     */
    private String email;

    /**
     * User Status
     */
    private Integer userStatus;

    /**
     * Role 0-General User，1-Admin
     */
    private Integer userRole;

    /**
     * User Code
     */
    private String idCode;
}
