package com.simbest.cores.admin.authority.service;

import com.simbest.cores.admin.authority.model.SysGroup;
import com.simbest.cores.service.IGenericService;

import java.util.List;


public interface ISysGroupService extends IGenericService<SysGroup, String> {

    int deleteSysUserGroupByGroupId(String groupId);

    int deleteSysUserGroupByUserId(String userId);

    int createSysUserGroup(String userId, String groupId);

    List<SysGroup> getByUser(String userId);

    List<String> getGroupUser(String groupid);
}
