package org.xinhuamm.demo.common.util;

import org.springframework.beans.BeanUtils;

/**
 * Bean 复制工具类。
 * 主要用于 Entity/DTO/VO 之间的属性拷贝，提升代码可读性。
 */
public final class BeanCopyUtil {

    private BeanCopyUtil() {
    }

    /**
     * 将源对象复制到目标类型对象。
     *
     * @param source 源对象
     * @param clazz  目标类型
     * @param <S>    源类型
     * @param <T>    目标类型
     * @return 复制后的目标对象
     */
    public static <S, T> T copy(S source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        try {
            T target = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new IllegalStateException("对象复制失败", e);
        }
    }
}

