package com.LearnLah.usercenter.service.impl;

import com.LearnLah.usercenter.common.ErrorCode;
import com.LearnLah.usercenter.constant.UserConstant;
import com.LearnLah.usercenter.exception.BusinessException;
import com.LearnLah.usercenter.mapper.UserMapper;
import com.LearnLah.usercenter.model.DTO.PasswordUpdateDTO;
import com.LearnLah.usercenter.model.domain.User;
import com.LearnLah.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
* @author YanChenyi
* @description Service to implement user's database options
* @createDate 2024-2-02 20:34:19
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    private static final String SALT = "Yan";

    @Override
    public long userRegister(String username, String password, String checkPassword, String idCode) {
        // Length validation for account, password, and confirmation password
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "Data is NULL");
        }
        if (idCode.length() > 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account too long");
        }
        if (username.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account too short");
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password too short");
        }
        // Account name cannot contain special characters
        if (!username.matches("^[0-9a-zA-Z]{4,}$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account contains special characters");
        }
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Passwords entered do not match");
        }
        // Account duplication verification
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account duplicate");
        }
        // ID duplication verification
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_code", idCode);
        count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ID duplicate");
        }
        // Password encryption
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // Store Data
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword);
        user.setIdCode(idCode);
        // The default username for backend settings is the account name
        user.setUsername(username);
        user.setAvatarUrl("https://pic1.zhimg.com/80/v2-c8586e136574ebcbdc8ac5464cb36ff4_720w.webp");
        boolean savResult = this.save(user);
        if (!savResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Storage Error");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String username, String password, HttpServletRequest httpServletRequest) {
        // Length validation for account, password, and confirmation password
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Storage Error");
        }
        if (username.length() < 4 || password.length() < 8) {
            return null;
        }
        // Account cannot contain special characters
        if (!username.matches("^[0-9a-zA-Z]{4,}$")) {
            return null;
        }
        // Password encryption
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // Account duplication verification
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // User doesn't exist
        if (user == null) {
            log.info("user login failed, username cannot match password!");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username cannot match password");
        }
        
        User safetyUser = getSafetyUser(user);
        // Store login status
        httpServletRequest.getSession().setAttribute(UserConstant.USER_LOGIN_STATUS, safetyUser);
        // Return user information
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setIdCode(originUser.getIdCode());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATUS);
        return 1;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // Already login or not
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATUS);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "Haven't login");
        }
        // Search from database
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "Haven't login");
        }
        return currentUser;
    }

    @Override
    public QueryWrapper<User> searchUsers(User userQuery) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (userQuery != null) {
            if (userQuery.getId() != null) {
                queryWrapper.eq("id", userQuery.getId());
            }
            if (userQuery.getUserRole() != null) {
                queryWrapper.eq("user_role", userQuery.getUserRole());
            }
            if (userQuery.getGender() != null) {
                queryWrapper.eq("gender", userQuery.getGender());
            }
            if (userQuery.getUserStatus() != null) {
                queryWrapper.eq("user_status", userQuery.getUserStatus());
            }
            if (StringUtils.isNotBlank(userQuery.getUsername())) {
                queryWrapper.like("username", userQuery.getUsername());
            }

            if (StringUtils.isNotBlank(userQuery.getEmail())) {
                queryWrapper.like("email", userQuery.getEmail());
            }
            if (StringUtils.isNotBlank(userQuery.getIdCode())) {
                queryWrapper.eq("id_code", userQuery.getIdCode());
            }
            if (StringUtils.isNotBlank(userQuery.getPhone())) {
                queryWrapper.like("phone", userQuery.getPhone());
            }
        }
        return queryWrapper;
    }

    @Override
    public boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO, HttpServletRequest request) {
        if (passwordUpdateDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Front-end did'n pass the password");
        }
        User loginUser = getLoginUser(request);
        Integer userId = loginUser.getId();
        if (userId == null || userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "User doesn't exist");
        }
        String encryptedOldPassword = DigestUtils.md5DigestAsHex((SALT + passwordUpdateDTO.getPassword()).getBytes());
        if (!loginUser.getPassword().equals(encryptedOldPassword)) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD_ERROR, "Original password error");
        }

        User user = new User();
        BeanUtils.copyProperties(passwordUpdateDTO, user);
        user.setId(loginUser.getId());

        // Encrypt the new password using MD5
        String encryptedNewPassword = DigestUtils.md5DigestAsHex((SALT + passwordUpdateDTO.getNewPassword()).getBytes());
        user.setPassword(encryptedNewPassword);
        if (encryptedNewPassword.equals(passwordUpdateDTO.getPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password modification cannot be the same");
        }
        return updateById(user);
    }
}




