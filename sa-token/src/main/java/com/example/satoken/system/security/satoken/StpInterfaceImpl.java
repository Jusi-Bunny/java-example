package com.example.satoken.system.security.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.example.satoken.mapper.SysPermissionMapper;
import com.example.satoken.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final SysRoleMapper sysRoleMapper;

    private final SysPermissionMapper sysPermissionMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = loginId instanceof Number number
                ? number.longValue()
                : Long.parseLong(loginId.toString());
        return sysPermissionMapper.selectPermissionCodeListByUserId(userId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = loginId instanceof Number number
                ? number.longValue()
                : Long.parseLong(loginId.toString());
        return sysRoleMapper.selectRoleCodeListByUserId(userId);
    }
}
