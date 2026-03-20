package org.xinhuamm.demo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xinhuamm.demo.common.response.Result;
import org.xinhuamm.demo.dto.UserDTO;
import org.xinhuamm.demo.service.UserService;
import org.xinhuamm.demo.vo.UserVO;

/**
 * 用户控制器。
 * 负责处理用户相关请求，只做参数接收与响应组装，不承载业务逻辑。
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    /**
     * 根据用户 ID 查询用户信息。
     *
     * @param id 用户 ID
     * @return 用户视图对象
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable @Min(value = 1, message = "用户ID必须大于0") Long id) {
        return Result.success(userService.getUserById(id));
    }

    /**
     * 新增用户。
     *
     * @param userDTO 用户入参
     * @return 新建后的用户视图对象
     */
    @PostMapping
    public Result<UserVO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return Result.success(userService.createUser(userDTO));
    }
}

