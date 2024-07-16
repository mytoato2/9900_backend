package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.BaseResponse;
import com.example.demo.common.ErrorCode;
import com.example.demo.common.ResultUtils;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.example.demo.constant.UserConstant.USER_LOGIN_STATE;


/**
* @author mytoato
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-06-26 19:49:11
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
implements UserService{
    @Resource
    private UserMapper userMapper;


    @Override
    public BaseResponse<Long> userRegister(String email, String userPassword, int role,String company,String jobTitle,String username) {
        //TODO:进来的都是不是3的，所以有可能有错误数据。controller层只管传递，不做校验
        //1.校验
        if(StringUtils.isAnyBlank(email,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "email or password is empty");
        }
        if(StringUtils.isAnyBlank(username)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username is empty");
        }
        if(StringUtils.isAnyBlank(role+"")){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "userRole is empty");
        }
        //userRole只能是1或2,0也不行
        if(role!=1 && role!=2){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"userRole invalid");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        long count = this.count(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.DUPLICATE_KEY,"this email has been registered");
        }
        //TODO:是否要密码加密

        User user = new User();
        user.setEmail(email);
        user.setUserPassword(userPassword);
        user.setUserRole(role);
        user.setCompany(company);
        user.setJobTitle(jobTitle);
        user.setUsername(username);
        boolean res = this.save(user);
        if(!res){
            throw new BusinessException(ErrorCode.NULL_ERROR,"register failed");
        }

        return ResultUtils.success(user.getId());
    }

    @Override
    public BaseResponse<Long> studentRegister(String email, String userPassword,String username, String zID) {

        //1.校验不能空的选项
        if(StringUtils.isAnyBlank(email)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "");
        }
        if(StringUtils.isAnyBlank(username)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username is empty");
        }
        if(StringUtils.isAnyBlank(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "password is empty");
        }
        if(StringUtils.isAnyBlank(zID)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "zid is empty");
        }

        //账户不能重复,email唯一或zid也唯一
        //分开判断，报错信息更精确
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("email",email)
//                .or()
//                .eq("zID",zID);
        queryWrapper.eq("email",email);
        long count = this.count(queryWrapper);
        //
        if(count>0){
            throw new BusinessException(ErrorCode.DUPLICATE_KEY,"this email has been registered");
        }
        //再判断zID
        queryWrapper.clear();
        queryWrapper.eq("zID",zID);
        count = this.count(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.DUPLICATE_KEY,"this zid has been registered");
        }

        User user = new User();
        user.setUserPassword(userPassword);
        user.setUserRole(3);
        user.setUsername(username);
        user.setZID(zID);
        boolean res = this.save(user);
        if(!res){
            throw new BusinessException(ErrorCode.NULL_ERROR,"register failed");
        }

        return ResultUtils.success(user.getId());
    }

    @Override
    public BaseResponse<User> userLogin(String email, String userPassword, HttpServletRequest request) {

        //查询用户账户密码是否匹配
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
//        queryWrapper.eq("userPassword",userPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if(user == null){
            log.info("user doesn't exist");
            throw new BusinessException(ErrorCode.QUERY_FAILED,"user doesn't exist");
        }
        String realPassword = user.getUserPassword();
        if(!userPassword.equals(realPassword)){
            log.info("password doesn't match");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"password doesn't match");
        }
//        queryWrapper.eq("userPassword",userPassword);
//        user = userMapper.selectOne(queryWrapper);
//        if(user == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"acount cannot match password");
        //用户脱敏
        User safetyUser =  getSafetyUser(user);
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);

        return ResultUtils.success(safetyUser);
    }
    @Override
    //分类获取安全用户信息
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }

        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setCreateTime(originUser.getCreateTime());

        int userRole = originUser.getUserRole();
        switch (userRole) {
            case 0: // admin
                break;
            case 1: // tutor
                safetyUser.setCompany(originUser.getCompany());
                break;
            case 2: // client
                safetyUser.setCompany(originUser.getCompany());
                safetyUser.setJobTitle(originUser.getJobTitle());
                break;
            case 3: // student
                safetyUser.setZID(originUser.getZID());
                safetyUser.setGroupName(originUser.getGroupName());
                safetyUser.setPreference(originUser.getPreference());
                break;
            default:
                //throw new BusinessException( "Invalid user role: " + userRole);
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid user role: " + userRole);
        }

        return safetyUser;
    }
    @Override
    public Long getLoginUserId(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return currentUser.getId();
    }

}
