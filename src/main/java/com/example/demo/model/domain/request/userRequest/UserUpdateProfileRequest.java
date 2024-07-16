package com.example.demo.model.domain.request.userRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserUpdateProfileRequest{
    private Long id;

    private Integer userRole;

    private String username;

    private String email;

    private String userPassword;

    private String zID;

    private String groupName;

    private String preference;

    private String company;

    private String jobTitle;

    private String avatarUrl;
}
