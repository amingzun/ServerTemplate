package org.xinhuamm.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.xinhuamm.demo.service.ConcurrencyService;
import org.xinhuamm.demo.vo.ConcurrencyTaskVO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 并发处理服务实现。
 * 演示如何在服务层使用统一线程池执行并发任务。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConcurrencyServiceImpl implements ConcurrencyService {

    @Qualifier("appTaskExecutor")
    private final Executor taskExecutor;

    /**
     * 并发计算平方值。
     * // AI可扩展点：可替换为批量 RPC、批量数据库查询或多服务聚合调用
     *
     * @param taskCount 任务数量
     * @return 并发结果对象
     */
    @Override
    public ConcurrencyTaskVO parallelSquare(int taskCount) {
        long start = System.currentTimeMillis();
        List<CompletableFuture<Integer>> futures = new ArrayList<>();

        for (int i = 1; i <= taskCount; i++) {
            int value = i;
            futures.add(CompletableFuture.supplyAsync(() -> {
                log.info("并发任务执行 value={} thread={}", value, Thread.currentThread().getName());
                return value * value;
            }, taskExecutor));
        }

        List<Integer> results = futures.stream().map(CompletableFuture::join).toList();
        long costMs = System.currentTimeMillis() - start;

        ConcurrencyTaskVO vo = new ConcurrencyTaskVO();
        vo.setTaskCount(taskCount);
        vo.setResults(results);
        vo.setCostMs(costMs);
        return vo;
    }
}

