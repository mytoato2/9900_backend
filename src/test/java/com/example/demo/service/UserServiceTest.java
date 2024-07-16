//package com.example.demo.service;
//import java.util.Date;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//
//
///**
// * 用户服务测试
// */
//@SpringBootTest
//class UserServiceTest {
//
//    @Resource
//    private UserService userService;
//    @Test
//    public void testAddUser(){
//        User user = new User();
//        user.setUsername("admin");
//        user.setUserAccount("admin");
//        user.setAvatarUrl("");
//        user.setGender(0);
//        user.setUserPassword("123456");
//        user.setPhone("123");
//        user.setEmail("456");
//        user.setUserStatus(0);
//        user.setCreateTime(new Date());
//        user.setUpdateTime(new Date());
//        user.setIsDelete(0);
//
//        boolean res = userService.save(user);
//        System.out.println(user.getId());
//        Assertions.assertTrue(res);
//    }
//
//    @Test
//    void userRegister() {
////        String userAccount = "Chris";
////        String userPassword = "";
////        int userStatus = 1;
////        long res = userService.userRegister(userAccount,userPassword,userStatus);
////        Assertions.assertEquals(-1,res);
////        userAccount = "c";
////        res = userService.userRegister(userAccount,userPassword,userStatus);
////        Assertions.assertEquals(-1,res);
////        userAccount = "admin";
////        userPassword = "123456";
////        res = userService.userRegister(userAccount,userPassword,userStatus);
////        Assertions.assertEquals(-1,res);
////        userAccount = "Chris";
////        userPassword = "123456";
////        res = userService.userRegister(userAccount,userPassword,userStatus);
////        Assertions.assertTrue(res<0);
//
//
//    }
//}