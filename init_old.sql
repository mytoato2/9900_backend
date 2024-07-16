-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS 9900proj;

-- 使用该数据库
USE 9900proj;

-- 删除表（如果存在）
DROP TABLE IF EXISTS user;

-- 创建表
CREATE TABLE user
(
    id           bigint auto_increment
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态 0-正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int                                null
);

-- 插入数据
INSERT INTO user (username, userAccount, avatarUrl, gender, userPassword, phone, email, userStatus, userRole)
SELECT * FROM (
                  SELECT 'root' as username, 'root' as userAccount, 'https://th.bing.com/th/id/OIP.hsnwZAr2R2xUr97ScoSjrgAAAA?rs=1&pid=ImgDetMain' as avatarUrl, 1 as gender, '123456' as userPassword, '123-456-7890' as phone, 'alice@example.com' as email, 0 as userStatus, 0 as userRole
                  UNION ALL SELECT 'student', 'tina717', 'https://th.bing.com/th/id/OIP.hsnwZAr2R2xUr97ScoSjrgAAAA?rs=1&pid=ImgDetMain', 0, '123456', '909-010-2020', 'tina@example.com', 0, 1
                  UNION ALL SELECT 'coor', 'mia010', 'https://th.bing.com/th/id/OIP.hsnwZAr2R2xUr97ScoSjrgAAAA?rs=1&pid=ImgDetMain', 0, 'password010', '202-303-4040', 'mia@example.com', 0, 2
                  UNION ALL SELECT 'tutor', 'nick111', 'https://th.bing.com/th/id/OIP.hsnwZAr2R2xUr97ScoSjrgAAAA?rs=1&pid=ImgDetMain', 1, 'password111', '303-404-5050', 'nick@example.com', 0, 3
              ) AS tmp
WHERE NOT EXISTS (
        SELECT 1 FROM user
    );