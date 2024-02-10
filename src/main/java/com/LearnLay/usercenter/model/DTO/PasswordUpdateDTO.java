package com.LearnLay.usercenter.model.DTO;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author YanChenyi
 */
@Data
public class PasswordUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5996345129538944393L;

    /**
     * Original Password
     */
    private String userPassword;

    /**
     * New Password
     */
    private String newPassword;
}
