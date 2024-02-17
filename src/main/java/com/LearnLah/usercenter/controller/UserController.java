package com.LearnLah.usercenter.controller;


import com.LearnLah.usercenter.common.BaseResponse;
import com.LearnLah.usercenter.common.ErrorCode;
import com.LearnLah.usercenter.exception.BusinessException;
import com.LearnLah.usercenter.model.DTO.PasswordUpdateDTO;
import com.LearnLah.usercenter.model.DTO.UserDeleteDTO;
import com.LearnLah.usercenter.model.DTO.UserSearchDTO;
import com.LearnLah.usercenter.model.DTO.UserUpdateDTO;
import com.LearnLah.usercenter.model.domain.User;
import com.LearnLah.usercenter.model.domain.request.UserLoginRequest;
import com.LearnLah.usercenter.model.domain.request.UserRegisterRequest;
import com.LearnLah.usercenter.utils.ResultUtil;
import com.LearnLah.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import static com.LearnLah.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.LearnLah.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

import java.util.List;
import java.util.stream.Collectors;


/**
 * api for user
 *
 * @author YanChenyi
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    public UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> UserRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String idCode = userRegisterRequest.getIdCode();
        if (StringUtils.isAnyBlank(username, password, checkPassword, idCode)) {
            return ResultUtil.error(ErrorCode.PARAMS_NULL_ERROR);
        }
        long result = userService.userRegister(username, password, checkPassword, idCode);
        return ResultUtil.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User curUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (curUser == null) {
            return ResultUtil.error(ErrorCode.PARAMS_NULL_ERROR);
        }
        long userId = curUser.getId();
        User user = userService.getById(userId);
        User result = userService.getSafetyUser(user);
        return ResultUtil.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> UserLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            return ResultUtil.error(ErrorCode.PARAMS_NULL_ERROR);
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            return ResultUtil.error(ErrorCode.PARAMS_NULL_ERROR);
        }
        User result = userService.userLogin(username, password, httpServletRequest);
        return ResultUtil.success(result);
    }


    @PostMapping("/logout")
    public BaseResponse<Integer> UserLogout(HttpServletRequest request) {
        if (request == null) {
            return ResultUtil.error(ErrorCode.PARAMS_NULL_ERROR);
        }

        int result = userService.userLogout(request);
        return ResultUtil.success(result);
    }

    /**
     * Search User
     *
     * @param userSearchDTO Ask for user's information
     * @param request       
     * @return return user's information
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(UserSearchDTO userSearchDTO, HttpServletRequest request) {
        //isAdmin
        if (!isAdmin(request)) {
            return ResultUtil.error(ErrorCode.NOT_AUTH);
        }
        User userQuery = new User();
        if (userSearchDTO != null) {
            BeanUtils.copyProperties(userSearchDTO, userQuery);
        }
        //Search
        QueryWrapper<User> queryWrapper = userService.searchUsers(userQuery);
        List<User> userList = userService.list(queryWrapper);
        List<User> result = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtil.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteDTO userDeleteDTO, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResultUtil.error(ErrorCode.PARAMS_NULL_ERROR);
        }
        boolean result = userService.removeById(userDeleteDTO.getId());
        return ResultUtil.success(result);
    }

    /**
     * Update user's information
     *
     * @param request
     * @return
     */
    @PostMapping("/updateUser")
    public BaseResponse<User> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, HttpServletRequest request) {
        if (userUpdateDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtil.success(loginUser);
    }

    /**
     * Admin updates user's information
     *
     * @param request
     * @return
     */
    @PostMapping("/adminUpdateUser")
    public BaseResponse<Boolean> adminUpdateUser(@RequestBody UserUpdateDTO userUpdateDTO, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH, "No right to access");
        }
        if (userUpdateDTO == null || userUpdateDTO.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        return ResultUtil.success(userService.updateById(user));
    }

    /**
     * Update Password
     *
     * @param passwordUpdateDTO
     * @param request
     * @return
     */
    @PostMapping("/updatePassword")
    public BaseResponse<Boolean> updatePassword(@RequestBody PasswordUpdateDTO passwordUpdateDTO,
                                                    HttpServletRequest request) {
        boolean updatePassword = userService.updatePassword(passwordUpdateDTO, request);
        if (updatePassword) {
            return ResultUtil.success(true);
        }
        return ResultUtil.error(ErrorCode.INVALID_PASSWORD_ERROR);
    }

    /**
     * IsAdmin
     *
     * @param request Ask for front-end's response
     * @return return is admin or not
     */
    private boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
