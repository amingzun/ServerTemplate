package org.xinhuamm.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户创建入参对象。
 * // AI校验入口：新增字段时请优先补充校验注解，避免脏数据进入业务层
 */
@Data
public class UserDTO {

    /**
     * 用户名。
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 用户邮箱。
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 用户年龄。
     */
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;
}

