/**
 * 
 */
package com.simbest.cores.service;

import java.io.Serializable;

import com.simbest.cores.model.SystemModel;

/**
 * 同时具备数据库CURD操作逻辑与缓存逻辑的高级抽象业务类
 * 
 * @author lishuyi
 *
 */
public interface ISystemAdvanceService<V extends SystemModel<V>,K extends Serializable>
		extends IGenericAdvanceService<V, K>, ISystemService<V, K> {
}
