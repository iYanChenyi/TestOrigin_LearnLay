package com.LearnLay.usercenter.service;

import com.LearnLay.usercenter.model.DTO.PasswordUpdateDTO;
import com.LearnLay.usercenter.model.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author YanChenyi
* @description Service to implement user's database options
* @createDate 2024-2-02 20:40:39
*/
public interface UserService extends IService<User> {

    /**
     * User registration validation
     * @param userAccount User enters account name
     * @param userPassword User enters password
     * @param checkPassword User checks password
     * @param idCode User ID
     * @return Return ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String idCode);

    /**
     * User login validation 
     *
     * @param userAccount        User enters account name
     * @param userPassword       User enters password
     * @param httpServletRequest Make request
     * @return Return user information
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    /**
     * User information desensitization
     * @param originUser Original user information
     * @return Return desensitized information
     */
    User getSafetyUser(User originUser);

    /**
     * Delete User
     *
     * @param request Make Request
     * @return Return an int value
     */
    int userLogout(HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    QueryWrapper<User> searchUsers(User userQuery);

    boolean updateUserPassword(PasswordUpdateDTO passwordUpdateDTO, HttpServletRequest request);
}