-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS 9900proj;

-- 使用该数据库
USE 9900proj;

-- 删除表（如果存在）
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS project;

-- 创建 user 表
CREATE TABLE user (
                      id           bigint auto_increment         primary key,
                      userRole     int                                not null comment '0 - admin; 1 - tutor; 2 - client; 3 - student',
                      username     varchar(256)                       null,
                      email        varchar(512)                       not null,
                      userPassword varchar(512)                       not null,
                      zID          varchar(512)                       null,
                      groupName    varchar(256)                       null,
                      preference   varchar(1024)                      null comment 'list of project ID',
                      company      varchar(512)                       null,
                      jobTitle     varchar(512)                       null,
                      avatarUrl    varchar(1024)                      null,
                      createTime   datetime default CURRENT_TIMESTAMP null,
                      isDelete     tinyint  default 0                 not null
);

-- 创建 project 表
CREATE TABLE project (
                         projectID        bigint auto_increment         primary key,
                         projectName      varchar(512)                       null,
                         details          varchar(256)                       null,
                         projectOwner     bigint                             not null comment 'the ID of client who created it',
                         readyGroupIDList varchar(512)                       null comment 'IDList of those group who choosed this group as first reference',
                         finalGroupList   varchar(512)                       null comment 'those who are choosed finally',
                         createTime       datetime default CURRENT_TIMESTAMP null,
                         isDelete         tinyint  default 0                 not null
);

-- 插入 user 表测试数据
INSERT INTO user (userRole, username, email, userPassword, zID, groupName, preference, company, jobTitle, avatarUrl, createTime, isDelete)
VALUES
    (0, 'admin', 'admin@test.com', '123456', NULL, NULL, NULL, NULL, NULL, NULL, '2023-06-27 10:00:00', 0),
    (1, 'tutor1', 'tutor1@test.com', '123456', NULL, NULL, NULL, 'ABC University', NULL, NULL, '2023-06-27 11:00:00', 0),
    (1, 'tutor2', 'tutor2@test.com', '123456', NULL, NULL, NULL, 'DEF College', NULL, NULL, '2023-06-27 11:01:00', 0),
    (2, 'client1', 'client1@test.com', '123456', NULL, NULL, NULL, 'XYZ Company', 'Project Manager', NULL, '2023-06-27 12:00:00', 0),
    (2, 'client2', 'client2@test.com', '123456', NULL, NULL, NULL, 'WVU Corporation', 'Product Owner', NULL, '2023-06-27 12:01:00', 0),
    (3, 'student1', 'student1@test.com', '123456', 'z5555555', 'Group A', '1,2,3', NULL, NULL, NULL, '2023-06-27 13:00:00', 0),
    (3, 'student2', 'student2@test.com', '123456', 'z6666666', 'Group B', '2,3,4', NULL, NULL, NULL, '2023-06-27 13:01:00', 0),
    (3, 'student3', 'student3@test.com', '123456', 'z7777777', 'Group A', '1,3,5', NULL, NULL, NULL, '2023-06-27 13:02:00', 0),
    (3, 'student4', 'student4@test.com', '123456', 'z8888888', 'Group C', '4,5,6', NULL, NULL, NULL, '2023-06-27 13:03:00', 0);

-- 插入 project 表测试数据
INSERT INTO project (projectName, details, projectOwner, readyGroupIDList, finalGroupList, isDelete)
VALUES
    ('Project A', 'Details for Project A', 4, '1,2,3', '2', 0),
    ('Project B', 'Details for Project B', 5, '1,3,4', '4', 0),
    ('Project C', 'Details for Project C', 4, '2,4', '2,4', 0),
    ('Project D', 'Details for Project D', 5, '1,3', '3', 0),
    ('Project E', 'Details for Project E', 4, '2,3,4', '3,4', 0);