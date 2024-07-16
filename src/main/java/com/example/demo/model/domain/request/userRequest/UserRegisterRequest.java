package com.example.demo.model.domain.request.userRequest;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 5725586937659214123L;

    private String username;
    private String email;
    private String userPassword;
    private int  userRole;

    private String company;
    private String jobTitle;

    private String zID;

}
