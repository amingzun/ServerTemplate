package org.xinhuamm.demo.common.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * TraceId 过滤器。
 * 为每个请求生成或继承 traceId，并记录统一访问日志。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    /**
     * 日志上下文中的 traceId 键名。
     */
    public static final String TRACE_ID_KEY = "traceId";

    /**
     * 请求头 traceId 键名。
     */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (!StringUtils.hasText(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }

        long start = System.currentTimeMillis();
        MDC.put(TRACE_ID_KEY, traceId);
        response.setHeader(TRACE_ID_HEADER, traceId);
        try {
            log.info("请求开始 method={} uri={} ip={}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
            filterChain.doFilter(request, response);
        } finally {
            long cost = System.currentTimeMillis() - start;
            log.info("请求结束 method={} uri={} status={} costMs={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    cost);
            MDC.remove(TRACE_ID_KEY);
        }
    }
}

