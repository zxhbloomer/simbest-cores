package com.simbest.cores.admin.authority.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.simbest.cores.admin.authority.model.SysOrg;
import com.simbest.cores.admin.authority.service.ISysOrgAdvanceService;
import com.simbest.cores.admin.authority.service.ISysOrgService;
import com.simbest.cores.cache.IGenericCache;
import com.simbest.cores.service.impl.GenericAdvanceService;


@Service(value="sysOrgAdvanceService")
public class SysOrgAdvanceService extends GenericAdvanceService<SysOrg,Integer> implements ISysOrgAdvanceService{

	private ISysOrgService sysOrgService;
	
	@Autowired
	public SysOrgAdvanceService(
			ISysOrgService sysOrgService,
			@Qualifier(value="sysOrgCache")IGenericCache<SysOrg,Integer> sysOrgCache) {
		super(sysOrgService, sysOrgCache);
		this.sysOrgService = sysOrgService;
	}
	
	@Override
	@Cacheable
	public SysOrg getRoot() {	
		return sysOrgService.getRoot();
	}

	@Override
	@Cacheable
	public List<SysOrg> getExcludeRoot() {
		return sysOrgService.getExcludeRoot();
	}

	@Override
	@Cacheable
	public List<SysOrg> getByParent(Integer parentId) {
		return sysOrgService.getByParent(parentId);
	}

	public Integer countByParent(@Param("parentId") Integer parentId){
		return sysOrgService.countByParent(parentId);
	}
	
	@Override
	@Cacheable
	public List<SysOrg> getChildrenOrg(Integer id) {
		return sysOrgService.getChildrenOrg(id);
	}

	@Override
	@Cacheable
	public LinkedList<SysOrg> getParentByChild(Integer id) {
		return sysOrgService.getParentByChild(id);
	}

	@Override
	@Cacheable
	public SysOrg getParent(Integer id) {
		return sysOrgService.getParent(id);
	}


}
