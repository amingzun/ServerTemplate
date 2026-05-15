package org.xinhuamm.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xinhuamm.demo.common.cache.CacheService;
import org.xinhuamm.demo.common.exception.BusinessException;
import org.xinhuamm.demo.dto.UserDTO;
import org.xinhuamm.demo.entity.UserEntity;
import org.xinhuamm.demo.repository.UserMapper;
import org.xinhuamm.demo.service.UserService;
import org.xinhuamm.demo.vo.UserVO;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 用户服务实现类。
 * 负责用户核心业务逻辑编排，包括缓存读写与数据库读写。
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_CACHE_KEY_PREFIX = "user:detail:";

    private final UserMapper userMapper;
    private final CacheService cacheService;

    /**
     * 根据 ID 查询用户，优先读缓存，缓存未命中再查数据库并回填缓存。
     * // AI提示：这里是用户查询逻辑，如果新增字段需要同步修改VO
     *
     * @param id 用户 ID
     * @return 用户视图对象
     */
    @Override
    public UserVO getUserById(Long id) {
        String cacheKey = USER_CACHE_KEY_PREFIX + id;
        var cachedUser = cacheService.get(cacheKey, UserVO.class);
        if (cachedUser.isPresent()) {
            return cachedUser.get();
        }

        UserEntity entity = userMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("用户不存在");
        }

        UserVO userVO = toVO(entity);
        // AI可扩展点：此处可引入多级缓存或分布式锁防击穿
        cacheService.put(cacheKey, userVO, Duration.ofMinutes(30));
        return userVO;
    }

    /**
     * 创建用户并更新缓存。
     * // AI提示：创建完成后同步刷新缓存，避免后续读到旧数据
     *
     * @param userDTO 用户入参
     * @return 创建后的用户视图对象
     */
    @Override
    public UserVO createUser(UserDTO userDTO) {
        UserEntity entity = new UserEntity();
        entity.setUsername(userDTO.getUsername());
        entity.setEmail(userDTO.getEmail());
        entity.setAge(userDTO.getAge());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        int rows = userMapper.insert(entity);
        if (rows <= 0 || entity.getId() == null) {
            throw new BusinessException("创建用户失败");
        }

        UserVO userVO = toVO(entity);
        // AI可扩展点：可在此发送用户创建事件到消息队列
        cacheService.put(USER_CACHE_KEY_PREFIX + entity.getId(), userVO, Duration.ofMinutes(30));
        return userVO;
    }

    /**
     * 将实体对象转换为视图对象。
     *
     * @param entity 用户实体
     * @return 用户视图对象
     */
    private UserVO toVO(UserEntity entity) {
        UserVO userVO = new UserVO();
        userVO.setId(entity.getId());
        userVO.setUsername(entity.getUsername());
        userVO.setEmail(entity.getEmail());
        userVO.setAge(entity.getAge());
        userVO.setCreatedAt(entity.getCreatedAt());
        return userVO;
    }
}
