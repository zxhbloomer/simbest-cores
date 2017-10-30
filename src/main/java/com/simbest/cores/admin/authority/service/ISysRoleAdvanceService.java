package com.simbest.cores.admin.authority.service;
import java.util.Map;

import com.simbest.cores.admin.authority.model.SysRole;
import com.simbest.cores.service.IGenericAdvanceService;

public interface ISysRoleAdvanceService extends IGenericAdvanceService<SysRole,Integer>, ISysRoleService{
	Map<String, Object> getPermissionsTreeData(Integer roleId);

    /**
     * 清空按条件筛选的缓存数据
     */
    public void clearCacheHolder();
}
