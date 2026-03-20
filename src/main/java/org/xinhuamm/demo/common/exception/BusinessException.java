package org.xinhuamm.demo.common.exception;

/**
 * 业务异常。
 * 用于 Service 层表达明确的业务错误。
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}

