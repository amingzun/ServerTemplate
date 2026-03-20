package org.xinhuamm.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类。
 * 映射数据库表 t_user。
 */
@Data
public class UserEntity {

    /**
     * 主键 ID。
     */
    private Long id;

    /**
     * 用户名。
     */
    private String username;

    /**
     * 邮箱。
     */
    private String email;

    /**
     * 年龄。
     */
    private Integer age;

    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间。
     */
    private LocalDateTime updatedAt;
}

