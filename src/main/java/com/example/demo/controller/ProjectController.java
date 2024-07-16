package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.BaseResponse;
import com.example.demo.common.ErrorCode;
import com.example.demo.common.ResultUtils;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.domain.Project;
import com.example.demo.model.domain.User;
import com.example.demo.model.domain.request.projectRequest.ProjectCreateRequest;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
@Tag(name= "Project")
public class ProjectController {
    @Resource
    private ProjectService projectService;
    @Resource
    private UserService userService;


    @GetMapping("/search")
    @Operation(summary = "Get All Project", description = "Retrieve all projects. This endpoint is accessible to all authenticated users")
    public BaseResponse<List<Project>> searchUsers(HttpServletRequest request){
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();

        List<Project> projectList = projectService.list(queryWrapper);
//        List<Project> res = projectList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(projectList);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new project",
            description = "Create a new project by an admin or client. " +
            "The projectName and projectOwner cannot be empty，others are optional" +
            "Only users with userRole=0 (admin) or userRole=2 (client) are allowed to create projects."
    )
    public BaseResponse<Long> createProject(@RequestBody ProjectCreateRequest projectCreateRequest, HttpServletRequest request) {
        Long loginUserId = userService.getLoginUserId(request);

        User user = userService.getById(loginUserId);
        //判断为空已经在service层判断了,拿到的user不可能为空
        int userRole = user.getUserRole();
        if(userRole != 0 && userRole != 2){
            throw new BusinessException(ErrorCode.NO_AUTH, "Only admins and clients can create projects");
        }
        if(StringUtils.isAnyBlank(projectCreateRequest.getProjectName())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Project name can't be empty");
        }

        // 创建 Project 对象并设置属性
        Project project = new Project();
        project.setProjectName(projectCreateRequest.getProjectName());
        project.setDetails(projectCreateRequest.getDetails());
        project.setProjectOwner(loginUserId);
//        project.setReadyGroupIDList(null); // 初始化为 null,后续学生小组选择项目时更新
//        project.setFinalGroupList(null); // 初始化为 null,后续确定最终选择的小组时更新


        // 调用 projectService 的方法保存项目
        boolean success = projectService.save(project);

        if (success) {
            return ResultUtils.success(project.getProjectID());
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to create project");
        }
    }
}
