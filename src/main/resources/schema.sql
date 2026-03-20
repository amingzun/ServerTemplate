CREATE TABLE IF NOT EXISTS t_user
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username   VARCHAR(64)  NOT NULL COMMENT '用户名',
    email      VARCHAR(128) NOT NULL COMMENT '邮箱',
    age        INT          NOT NULL COMMENT '年龄',
    created_at DATETIME     NOT NULL COMMENT '创建时间',
    updated_at DATETIME     NOT NULL COMMENT '更新时间'
) COMMENT='用户表';

