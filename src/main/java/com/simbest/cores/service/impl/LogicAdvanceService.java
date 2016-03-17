/**
 * 
 */
package com.simbest.cores.service.impl;

import java.io.Serializable;
import java.util.Collection;

import com.simbest.cores.cache.IGenericCache;
import com.simbest.cores.model.LogicModel;
import com.simbest.cores.service.ILogicAdvanceService;
import com.simbest.cores.service.ILogicService;

/**
 * 业务实体通用服务层与缓存的集成高级服务层
 * @author lishuyi
 *
 * @param <K>
 * @param <V>
 */
public class LogicAdvanceService<V extends LogicModel<V>, K extends Serializable> 
	extends SystemAdvanceService<V, K> implements ILogicAdvanceService<V, K>{

	private ILogicService<V, K> logicService;
	
	public LogicAdvanceService(ILogicService<V, K> logicService,
			IGenericCache<V, K> cacheService) {
		super(logicService, cacheService);
		this.logicService = logicService;
	}

	@Override
	public int updateEnable(boolean enabled, Collection<K> ids) {		
		log.debug("@Logic Advance Service make enable objects : "+enabled+" by "+ ids);
		int ret = logicService.updateEnable(enabled, ids);
		if(ret > 0){
			for(K id:ids){
				V o = logicService.getById(id);
				saveOrUpdate(id, o);
			}
		}
		return ret;
	}

}
