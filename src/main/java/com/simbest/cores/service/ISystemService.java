/**
 * 
 */
package com.simbest.cores.service;

import java.io.Serializable;

import com.simbest.cores.model.SystemModel;

/**
 * 业务实体通用服务层
 * 
 * 涉及业务实体的所有操作需要记录创建人信息和更新人信息
 * 
 * @author lishuyi
 *
 * @param <T>
 * @param <PK>
 */
public interface ISystemService<T extends SystemModel<T>, PK extends Serializable> extends IGenericService<T, PK> {
	
}
