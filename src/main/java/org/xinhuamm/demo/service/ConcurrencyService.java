package org.xinhuamm.demo.service;

import org.xinhuamm.demo.vo.ConcurrencyTaskVO;

/**
 * 并发处理服务接口。
 * 提供标准并发任务编排示例。
 */
public interface ConcurrencyService {

    /**
     * 并发计算 1..taskCount 的平方值。
     *
     * @param taskCount 任务数量
     * @return 并发任务结果
     */
    ConcurrencyTaskVO parallelSquare(int taskCount);
}

