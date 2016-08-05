package com.simbest.cores.admin.authority.service.impl;

import com.simbest.cores.admin.authority.model.SysGroup;
import com.simbest.cores.admin.authority.service.ISysGroupAdvanceService;
import com.simbest.cores.admin.authority.service.ISysGroupService;
import com.simbest.cores.admin.authority.service.ISysPermissionAdvanceService;
import com.simbest.cores.cache.IGenericCache;
import com.simbest.cores.service.impl.GenericAdvanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


//@Service(value = "sysGroupAdvanceService")
public class SysGroupAdvanceService extends GenericAdvanceService<SysGroup, String> implements ISysGroupAdvanceService {

    private ISysGroupService sysGroupService;

    @Autowired
    private ISysPermissionAdvanceService sysPermissionAdvanceService;

    @Autowired
    private RedisTemplate<String, Map<String, Object>> redisTemplate;

    private BoundHashOperations<String, String, Map<String, Object>> rolePermissionsTreeDataHolder = null;

    @Autowired
    public SysGroupAdvanceService(
            ISysGroupService sysGroupService,
            @Qualifier(value = "sysGroupCache") IGenericCache<SysGroup, String> sysGroupCache) {
        super(sysGroupService, sysGroupCache);
        this.sysGroupService = sysGroupService;
    }

    @Override
    public int deleteSysUserGroupByGroupId(String roleId) {
        return sysGroupService.deleteSysUserGroupByGroupId(roleId);
    }

    @Override
    public int deleteSysUserGroupByUserId(String userId) {
        return sysGroupService.deleteSysUserGroupByUserId(userId);
    }

    @Override
    public int createSysUserGroup(String userId, String roleId) {
        return sysGroupService.createSysUserGroup(userId, roleId);
    }

    @Override
    public List<SysGroup> getByUser(String userId) {
        return sysGroupService.getByUser(userId);
    }

}
