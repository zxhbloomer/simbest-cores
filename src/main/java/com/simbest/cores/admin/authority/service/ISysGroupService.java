package com.simbest.cores.admin.authority.service;

import com.simbest.cores.admin.authority.model.SysGroup;
import com.simbest.cores.service.IGenericService;

import java.util.List;


public interface ISysGroupService extends IGenericService<SysGroup, Integer> {

    int deleteSysUserGroupByGroupId(Integer groupId);

    int deleteSysUserGroupByUserId(Integer userId);

    int createSysUserGroup(Integer userId, Integer groupId);

    List<SysGroup> getByUser(Integer userId);

    List<String> getGroupUser(Integer groupid);
}
