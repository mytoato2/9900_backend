package com.example.demo.service;

import com.example.demo.common.BaseResponse;
import com.example.demo.model.domain.User;
//import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author mytoato
* @description 针对表【user】的数据库操作Service
* @createDate 2024-06-26 19:49:11
*/
public interface UserService extends IService<User> {
    /**
     *用户注册
     * @param email
     * @param userPassword 用户密码
     * @return -1/id
     */
    BaseResponse<Long> userRegister(String email, String userPassword, int userRole,String comany,String jobTitle,String username);


    BaseResponse<Long> studentRegister(String email, String userPassword,String username, String zID);

    /**
     * 用户登录
     * @param email
     * @param userPassword
     * @return
     */
    BaseResponse<User> userLogin(String email, String userPassword, HttpServletRequest request);

    User getSafetyUser(User user);

    Long getLoginUserId(HttpServletRequest request);
}
