package org.xinhuamm.demo.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xinhuamm.demo.common.response.Result;
import org.xinhuamm.demo.service.ConcurrencyService;
import org.xinhuamm.demo.vo.ConcurrencyTaskVO;

/**
 * 并发示例控制器。
 * 演示标准线程池与异步计算的使用方式。
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concurrency")
public class ConcurrencyController {

    private final ConcurrencyService concurrencyService;

    /**
     * 并发计算平方值示例。
     *
     * @param taskCount 任务数量
     * @return 并发计算结果
     */
    @GetMapping("/square")
    public Result<ConcurrencyTaskVO> parallelSquare(
            @RequestParam(defaultValue = "5")
            @Min(value = 1, message = "任务数必须大于0")
            @Max(value = 50, message = "任务数不能超过50") int taskCount) {
        return Result.success(concurrencyService.parallelSquare(taskCount));
    }
}

