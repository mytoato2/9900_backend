package com.example.demo.model.domain.request.projectRequest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class ProjectCreateRequest {

//        private Long projectID;

        private String projectName;

        private String details;

        private Long projectOwner;

        private String readyGroupIDList;

        private String finalGroupList;


}

