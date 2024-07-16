package com.example.demo.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.domain.Project;
import com.example.demo.service.ProjectService;
import com.example.demo.mapper.ProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author mytoato
* @description 针对表【project】的数据库操作Service实现
* @createDate 2024-06-27 05:08:35
*/
@Service
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project>
implements ProjectService{
    @Resource
    private ProjectMapper projectMapper;
}
