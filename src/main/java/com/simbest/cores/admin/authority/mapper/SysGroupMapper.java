package com.simbest.cores.admin.authority.mapper;

import com.simbest.cores.admin.authority.model.SysGroup;
import com.simbest.cores.mapper.IGenericMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SysGroupMapper extends IGenericMapper<SysGroup, Integer> {

    int deleteSysUserGroupByGroupId(@Param(value = "groupId") Integer groupId);

    int deleteSysUserGroupByUserId(@Param(value = "userId") Integer userId);

    int createSysUserGroup(@Param(value = "userId") Integer userId, @Param(value = "groupId") Integer groupId);

    List<SysGroup> getByUser(@Param(value = "userId") Integer userId);

    List<String> getGroupUser(@Param(value = "groupid") Integer groupid);
}
