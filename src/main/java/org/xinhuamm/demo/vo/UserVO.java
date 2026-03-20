package org.xinhuamm.demo.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户返回对象。
 * 对外暴露时仅包含必要字段，避免泄露敏感信息。
 */
@Data
public class UserVO implements Serializable {

    /**
     * 序列化版本号。
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID。
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
}

