package com.example.mybatis.mapper;

import com.example.mybatis.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * mybatis_user 表对应数据库 SQL 语句映射接口
 * 接口只规定方法、参数和返回值。
 * mapper.xml 中编写具体 SQL 语句
 */
@Mapper
public interface SysUserMapper {

    /**
     * 根据 ID 查询用户信息
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    SysUser selectUserById(Long id);

    /**
     * 插入用户信息
     *
     * @param user 用户信息
     * @return 插入数量
     */
    int insertUser(SysUser user);

    /**
     * 查询所有用户信息
     *
     * @return 所有用户信息
     */
    List<SysUser> selectUserList();

    /**
     * 查询用户数量
     *
     * @return 用户数量
     */
    int selectUserCount();

    /**
     * 根据 ID 查询用户信息
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    Map<String, Object> selectUserMapById(Long id);

    /**
     * 根据条件查询用户信息
     *
     * @param map 查询条件
     * @return 用户信息
     */
    List<SysUser> selectUserByCondition(Map<String, Object> map);

    /**
     * 查询最近登录用户信息和首个用户首次登录时间
     *
     * @return 包含最近登录用户名、最近登录时间和首个用户首次登录时间的Map
     */
    Map<String, Object> selectLatestLoginUserInfo();
}
