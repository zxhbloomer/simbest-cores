/**
 * 
 */
package com.simbest.cores.service.impl;

import java.io.Serializable;

import com.simbest.cores.cache.IGenericCache;
import com.simbest.cores.model.SystemModel;
import com.simbest.cores.service.ISystemAdvanceService;
import com.simbest.cores.service.ISystemService;

/**
 * 系统实体通用服务层与缓存的集成高级服务层
 * 
 * @author lishuyi
 *
 * @param <K>
 * @param <V>
 */
public class SystemAdvanceService<V extends SystemModel<V>, K extends Serializable>
		extends GenericAdvanceService<V, K> implements ISystemAdvanceService<V, K> {

	private ISystemService<V, K> systemService;
	
	private IGenericCache<V, K> cacheService;
	
	public SystemAdvanceService(ISystemService<V, K> systemService,
			IGenericCache<V, K> cacheService) {
		super(systemService, cacheService);
		this.systemService = systemService;
		this.cacheService = cacheService;
	}


}
