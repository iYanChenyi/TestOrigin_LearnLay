package com.LearnLay.usercenter.model.domain.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * User Login request
 *
 * @author YanChenyi
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 5904429575465599378L;

    private String userAccount;
    private String userPassword;
}
