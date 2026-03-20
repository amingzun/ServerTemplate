package org.xinhuamm.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xinhuamm.demo.entity.UserEntity;

/**
 * 用户 Mapper。
 * 负责用户表的数据访问。
 */
@Mapper
public interface UserMapper {

    /**
     * 根据主键查询用户。
     *
     * @param id 用户 ID
     * @return 用户实体
     */
    UserEntity selectById(@Param("id") Long id);

    /**
     * 插入用户。
     *
     * @param userEntity 用户实体
     * @return 影响行数
     */
    int insert(UserEntity userEntity);
}

