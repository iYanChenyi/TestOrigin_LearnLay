package com.LearnLah.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * User register RRequest
 *
 * @author YanChenyi
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -4157977884839010887L;

    private String username;
    private String password;
    private String checkPassword;
    private String idCode;
}
