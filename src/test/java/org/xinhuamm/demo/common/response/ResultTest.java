package org.xinhuamm.demo.common.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Result 单元测试。
 * 验证统一返回结构工具方法行为。
 */
class ResultTest {

    /**
     * 验证 success 方法。
     */
    @Test
    void shouldReturnSuccessResult() {
        Result<String> result = Result.success("ok");
        Assertions.assertEquals(0, result.getCode());
        Assertions.assertEquals("success", result.getMessage());
        Assertions.assertEquals("ok", result.getData());
    }

    /**
     * 验证 error 方法。
     */
    @Test
    void shouldReturnErrorResult() {
        Result<Void> result = Result.error("bad request");
        Assertions.assertEquals(-1, result.getCode());
        Assertions.assertEquals("bad request", result.getMessage());
        Assertions.assertNull(result.getData());
    }
}

