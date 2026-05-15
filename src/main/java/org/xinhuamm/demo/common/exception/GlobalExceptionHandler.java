package org.xinhuamm.demo.common.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xinhuamm.demo.common.response.Result;

import java.util.stream.Collectors;

/**
 * 全局异常处理器。
 * 统一拦截接口异常并转换为标准 Result 结构。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理请求体参数校验异常（@Valid + @RequestBody）。
     *
     * @param ex 异常对象
     * @return 统一失败响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return Result.error(400, message);
    }

    /**
     * 处理表单参数校验异常。
     *
     * @param ex 异常对象
     * @return 统一失败响应
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return Result.error(400, message);
    }

    /**
     * 处理路径参数、查询参数约束异常。
     *
     * @param ex 异常对象
     * @return 统一失败响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException ex) {
        return Result.error(400, ex.getMessage());
    }

    /**
     * 处理业务异常。
     *
     * @param ex 异常对象
     * @return 统一失败响应
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex) {
        return Result.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理运行时异常。
     *
     * @param ex 异常对象
     * @return 统一失败响应
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException ex) {
        log.error("运行时异常", ex);
        return Result.error(500, "系统内部错误，请联系管理员");
    }
}
