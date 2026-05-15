package org.xinhuamm.demo.common.exception;

/**
 * 业务异常。
 * 用于 Service 层表达明确的业务错误。
 */
public class BusinessException extends RuntimeException {

    /**
     * 默认业务错误码。
     */
    public static final int DEFAULT_CODE = 4001;

    private final int code;

    public BusinessException(String message) {
        this(DEFAULT_CODE, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
