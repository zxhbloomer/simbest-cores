package com.simbest.cores.admin.authority.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.simbest.cores.admin.authority.model.SysOrg;
import com.simbest.cores.service.IGenericService;

public interface ISysOrgService extends IGenericService<SysOrg,Integer>{
	
	SysOrg getRoot();
	
	List<SysOrg> getExcludeRoot();	

	/**
	 * 通过父亲部门获取部门
	 * @param parentId
	 * @return
	 */
	List<SysOrg> getByParent(Integer parentId);

	/**
	 * 获取下属部门（不包括传入部门）
	 * @param id
	 * @return
	 */
	List<SysOrg> getChildrenOrg(Integer id);
	
	LinkedList<SysOrg> getParentByChild(Integer id);
	
	SysOrg getParent(Integer id);
	
	Integer countByParent(@Param("parentId") Integer parentId);
}
