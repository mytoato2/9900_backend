package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.BaseResponse;
import com.example.demo.common.ErrorCode;
import com.example.demo.common.ResultUtils;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.domain.User;
import com.example.demo.model.domain.request.userRequest.UserCreateRequest;
import com.example.demo.model.domain.request.userRequest.UserLoginRequest;
import com.example.demo.model.domain.request.userRequest.UserRegisterRequest;
import com.example.demo.model.domain.request.userRequest.UserUpdateProfileRequest;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.constant.UserConstant.ADMIN_ROLE;
import static com.example.demo.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Tag(name= "User")
public class UserController {

    @Resource
    private UserService userService;
    @PostMapping("/register")
    @Operation(
            summary = "User registration",
            description = "Register a new user. The registration process differs based on the user role (student or non-student)."
    )
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"parameter is empty");

        int userRole = userRegisterRequest.getUserRole();
        String email = userRegisterRequest.getEmail();
        String userPassword = userRegisterRequest.getUserPassword();
        String company = userRegisterRequest.getCompany();
        String jobTitle = userRegisterRequest.getJobTitle();
        String zID =  userRegisterRequest.getZID();
        String username = userRegisterRequest.getUsername();

        BaseResponse<Long> res =userRole==3
                ? userService.studentRegister(email, userPassword,username,zID)
                : userService.userRegister(email, userPassword, userRole,company,jobTitle,username);
        return res;
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "User login with email and password. If the email and password match, the user information will be returned and the login state will be recorded in the session."
    )
    @Parameters({
            @Parameter(
                    name = "email",
                    description = "User's email address",
                    required = true,
                    example = "admin"
            ),
            @Parameter(
                    name = "userPassword",
                    description = "User's password",
                    required = true,
                    example = "123456"
            )
    })
    public BaseResponse<User>userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null) throw new BusinessException(ErrorCode.NULL_ERROR);
        String userAccount = userLoginRequest.getEmail();
        String userPassword = userLoginRequest.getUserPassword();
        BaseResponse<User> res = userService.userLogin(userAccount, userPassword,request);
        return res;
    }


    @GetMapping("/search")
    @Operation(summary = "Get users", description = "Get user with optional username")
    @Parameters({
            @Parameter(
                    name = "username",
                    description = "Filter user by name (optional). If not provided or empty, all users will be returned.",
                    example = "John"
            )
    })
    public BaseResponse<List<User>> searchUsers(@RequestParam(required = false) String username,HttpServletRequest request){
        //应该写在service层，但是太简单就直接放controller层了
        if(!isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> res = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        if(res.isEmpty()) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        return ResultUtils.success(res);
    }


    @PostMapping("/delete")
    @Operation(
            summary = "Delete user",
            description = "Delete a user by ID. Only admin users are allowed to perform this operation. The user is not actually deleted from the database, but instead marked as deleted by updating the user's status."
    )
    @Parameters({
            @Parameter(
                    name = "id",
                    description = "ID of the user to be deleted",
                    required = true,
                    example = "123"
            )
    })
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id is invalid");
        }
        //batisplus的removeById方法配置后不会真删除，只是更新状态
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/create")
    @Operation(
            summary = "Create a new user",
            description = "Create a new user with the provided information. Only admin users are allowed to perform this operation."
    )
    public BaseResponse<Long> createUser(@RequestBody UserCreateRequest userCreateRequest, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (userCreateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"parameter is empty");
        }
        return userRegister(userCreateRequest);
    }

    @PostMapping("/update")
    @Operation(
            summary = "Update user information",
            description = "Update a user's personal information. Users can only update their own information, while admin users can update any user's information. Admin users cannot change other admin users' roles."
    )
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateProfileRequest userUpdateProfileRequest, HttpServletRequest request) {
        Long userId = userUpdateProfileRequest.getId();
        // 非管理员只能修改自己的信息,管理员可以修改任何人的信息
        if (!isAdmin(request) && !userId.equals(userService.getLoginUserId(request))) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User existingUser = userService.getById(userId);
        if (existingUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found");
        }

        // 检查当前登录用户是否为管理员
        boolean isAdmin = isAdmin(request);

        // 如果当前登录用户是管理员,且要修改的用户也是管理员,则不允许修改角色
        if (isAdmin && existingUser.getUserRole() == ADMIN_ROLE && userUpdateProfileRequest.getUserRole() != null) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Cannot change admin's role");
        }

        // 如果当前登录用户不是管理员,则不允许将角色修改为管理员
        if (!isAdmin && userUpdateProfileRequest.getUserRole() != null && userUpdateProfileRequest.getUserRole() == ADMIN_ROLE) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Cannot change role to admin");
        }

        User user = new User();
        BeanUtils.copyProperties(userUpdateProfileRequest, user);
        boolean updated = userService.updateById(user);

        return ResultUtils.success(updated);
    }
    private boolean isAdmin(HttpServletRequest request){
        //仅管理员可查询
        Object userobj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userobj;
        return user != null && user.getUserRole()==ADMIN_ROLE;
    }


}
