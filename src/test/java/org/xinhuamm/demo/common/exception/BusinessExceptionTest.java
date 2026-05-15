package org.xinhuamm.demo.common.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * BusinessException 单元测试。
 */
class BusinessExceptionTest {

    @Test
    void shouldUseDefaultBusinessCode() {
        BusinessException exception = new BusinessException("用户不存在");

        Assertions.assertEquals(4001, exception.getCode());
        Assertions.assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void shouldUseCustomBusinessCode() {
        BusinessException exception = new BusinessException(4002, "用户已存在");

        Assertions.assertEquals(4002, exception.getCode());
        Assertions.assertEquals("用户已存在", exception.getMessage());
    }
}
