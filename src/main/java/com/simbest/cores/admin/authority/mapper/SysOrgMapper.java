package com.simbest.cores.admin.authority.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.simbest.cores.admin.authority.model.SysOrg;
import com.simbest.cores.mapper.ISystemMapper;

public interface SysOrgMapper extends ISystemMapper<SysOrg,Integer>{
	
	SysOrg getRoot();
	
	List<SysOrg> getExcludeRoot();	

	List<SysOrg> getByParent(@Param("parentId") Integer parentId);
	
	Integer countByParent(@Param("parentId") Integer parentId);
}