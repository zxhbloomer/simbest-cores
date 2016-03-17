/**
 * 
 */
package com.simbest.cores.service;

import java.io.Serializable;

import com.simbest.cores.cache.IGenericCache;
import com.simbest.cores.model.GenericModel;

/**
 * 同时具备数据库CURD操作逻辑与缓存逻辑的高级抽象业务类
 * 
 * @author lishuyi
 *
 */
public interface IGenericAdvanceService<V extends GenericModel<V>, K extends Serializable>
		extends IGenericService<V, K>, IGenericCache<V, K> {

	IGenericService<V, K> getGenericService();

	IGenericCache<V, K> getCacheService();
}
