package org.xinhuamm.demo.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xinhuamm.demo.common.cache.CacheService;
import org.xinhuamm.demo.common.exception.BusinessException;
import org.xinhuamm.demo.dto.UserDTO;
import org.xinhuamm.demo.entity.UserEntity;
import org.xinhuamm.demo.repository.UserMapper;
import org.xinhuamm.demo.vo.UserVO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * UserServiceImpl 单元测试。
 */
class UserServiceImplTest {

    private UserMapper userMapper;
    private CacheService cacheService;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userMapper = Mockito.mock(UserMapper.class);
        cacheService = Mockito.mock(CacheService.class);
        userService = new UserServiceImpl(userMapper, cacheService);
    }

    @Test
    void shouldReturnUserFromCacheWhenCacheHit() {
        UserVO cached = new UserVO();
        cached.setId(1L);
        cached.setUsername("cached-user");
        Mockito.when(cacheService.get("user:detail:1", UserVO.class)).thenReturn(Optional.of(cached));

        UserVO result = userService.getUserById(1L);

        Assertions.assertEquals("cached-user", result.getUsername());
        Mockito.verify(userMapper, Mockito.never()).selectById(any());
    }

    @Test
    void shouldQueryDatabaseAndBackfillCacheWhenCacheMiss() {
        Mockito.when(cacheService.get("user:detail:1", UserVO.class)).thenReturn(Optional.empty());
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUsername("db-user");
        entity.setEmail("db@example.com");
        entity.setAge(20);
        entity.setCreatedAt(LocalDateTime.now());
        Mockito.when(userMapper.selectById(1L)).thenReturn(entity);

        UserVO result = userService.getUserById(1L);

        Assertions.assertEquals("db-user", result.getUsername());
        Mockito.verify(cacheService).put(eq("user:detail:1"), any(UserVO.class), eq(Duration.ofMinutes(30)));
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserMissing() {
        Mockito.when(cacheService.get("user:detail:1", UserVO.class)).thenReturn(Optional.empty());
        Mockito.when(userMapper.selectById(1L)).thenReturn(null);

        BusinessException exception = Assertions.assertThrows(
                BusinessException.class,
                () -> userService.getUserById(1L)
        );

        Assertions.assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void shouldCreateUserAndBackfillCache() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("new-user");
        userDTO.setEmail("new@example.com");
        userDTO.setAge(18);
        Mockito.when(userMapper.insert(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity entity = invocation.getArgument(0);
            entity.setId(10L);
            return 1;
        });

        UserVO result = userService.createUser(userDTO);

        Assertions.assertEquals(10L, result.getId());
        Assertions.assertEquals("new-user", result.getUsername());
        Mockito.verify(cacheService).put(eq("user:detail:10"), any(UserVO.class), eq(Duration.ofMinutes(30)));
    }
}
