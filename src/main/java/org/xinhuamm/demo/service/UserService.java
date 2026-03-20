package org.xinhuamm.demo.service;

import org.xinhuamm.demo.dto.UserDTO;
import org.xinhuamm.demo.vo.UserVO;

/**
 * 用户服务接口。
 * 定义用户业务能力，供控制层调用。
 */
public interface UserService {

    /**
     * 根据用户 ID 查询用户。
     *
     * @param id 用户 ID
     * @return 用户视图对象
     */
    UserVO getUserById(Long id);

    /**
     * 创建用户。
     *
     * @param userDTO 用户入参
     * @return 创建后的用户视图对象
     */
    UserVO createUser(UserDTO userDTO);
}

