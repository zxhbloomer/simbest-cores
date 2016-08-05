package com.simbest.cores.admin.authority.mapper;

import com.simbest.cores.admin.authority.model.SysGroup;
import com.simbest.cores.mapper.IGenericMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SysGroupMapper extends IGenericMapper<SysGroup, String> {

    int deleteSysUserGroupByGroupId(@Param(value = "groupId") String groupId);

    int deleteSysUserGroupByUserId(@Param(value = "userId") String userId);

    int createSysUserGroup(@Param(value = "userId") String userId, @Param(value = "groupId") String groupId);

    List<SysGroup> getByUser(@Param(value = "userId") String userId);
}
