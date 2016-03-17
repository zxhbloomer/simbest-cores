/**
 * 
 */
package com.simbest.cores.service;

import java.io.Serializable;

import com.simbest.cores.model.LogicModel;

/**
 * 同时具备数据库CURD操作逻辑与缓存逻辑的高级抽象业务类
 * 
 * @author lishuyi
 *
 */
public interface ILogicAdvanceService<V extends LogicModel<V>, K extends Serializable>
		extends ISystemAdvanceService<V, K>, ILogicService<V, K> {
}
