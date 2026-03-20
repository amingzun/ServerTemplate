package org.xinhuamm.demo.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结构。
 *
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /**
     * 业务状态码，0 表示成功。
     */
    private int code;

    /**
     * 响应消息。
     */
    private String message;

    /**
     * 响应数据体。
     */
    private T data;

    /**
     * 生成成功响应。
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 统一成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "success", data);
    }

    /**
     * 生成成功响应（无数据）。
     *
     * @return 统一成功响应
     */
    public static Result<Void> success() {
        return new Result<>(0, "success", null);
    }

    /**
     * 生成失败响应。
     *
     * @param message 错误信息
     * @return 统一失败响应
     */
    public static Result<Void> error(String message) {
        return new Result<>(-1, message, null);
    }

    /**
     * 生成失败响应（可自定义错误码）。
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 统一失败响应
     */
    public static Result<Void> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}

