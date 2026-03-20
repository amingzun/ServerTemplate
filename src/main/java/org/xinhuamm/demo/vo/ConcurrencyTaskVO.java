package org.xinhuamm.demo.vo;

import lombok.Data;

import java.util.List;

/**
 * 并发任务结果对象。
 */
@Data
public class ConcurrencyTaskVO {

    /**
     * 提交的任务数。
     */
    private int taskCount;

    /**
     * 结果列表（每个数字的平方）。
     */
    private List<Integer> results;

    /**
     * 总耗时（毫秒）。
     */
    private long costMs;
}

