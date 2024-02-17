package com.LearnLah.usercenter.service;
import java.util.Date;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.LearnLah.usercenter.model.domain.User;
import com.LearnLah.usercenter.service.UserService;

@SpringBootTest
class UserServiceTest {

    @Resource
    public UserService userService;

    @Test
    public void testSelect() {
        User user = new User();

        user.setUsername("Yan");
        user.setAvatarUrl(" ");
        user.setGender(0);
        user.setPassword("12345678");
        user.setPhone("123456789123");
        user.setEmail("31654@awg54.asd");
        user.setUserStatus(0);
        user.setIsDelete(0);

        System.out.println(userService.save(user));
        System.out.println(user.getId());

        Assertions.assertTrue(true);
    }

    @Test
    void userRegister() {
        String username = "Yan1";
        String password = "12345678";
        String checkPassword = "12345678";
        String idCode = "123";
        long result = userService.userRegister(username, password, checkPassword, idCode);
        Assertions.assertEquals(-1, result);
    }
}