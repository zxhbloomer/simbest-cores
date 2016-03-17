/**
 * 
 */
package com.simbest.cores.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.simbest.cores.exceptions.AppException;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.exceptions.ExceptionsMsgAlert;
import com.simbest.cores.exceptions.TransactionRollbackException;
import com.simbest.cores.model.LogicModel;
import com.simbest.cores.service.ILogicService;
import com.simbest.cores.shiro.AppUserSession;
import com.simbest.cores.utils.annotations.LogAudit;

/**
 * 逻辑实体控制层
 * 
 * @author lishuyi
 *
 */
public class LogicController<T extends LogicModel<T>, PK extends Serializable>
		extends BaseController<T, PK> {


	@Autowired
	private AppUserSession appUserSession;
	
	/**
	 * 
	 * @param persistentClass 持久化对象
	 * @param listPage 列表页面
	 * @param formPage 表单页面
	 */
	public LogicController(Class<T> persistentClass, String listPage, String formPage) {
		super(persistentClass, listPage, formPage);
	}

	@RequestMapping(value = "/updateEnable", method = RequestMethod.POST)
	@ResponseBody
	@LogAudit
	public Map<String, Object> updateEnable(boolean enabled,@RequestParam("ids") List<PK> ids) throws Exception {
		Map<String, Object> map = Maps.newHashMap();
		try {			 
			int ret = ((ILogicService<?, PK>)getService()).updateEnable(enabled, ids);
			map.put("message", ret > 0  ? "操作成功!":"操作失败!"); //返回值兼容批量更新
			map.put("responseid", ret > 0 ? 1:ret);
		}catch (NullPointerException e) {
			map.put("responseid", 0);
			map.put("message", "唯一性字段不能为空或重复:"+ExceptionsMsgAlert.alertDuplicateKeyException(persistentClass, Exceptions.getStackTraceAsString(e)));
			log.error("LogicController catch NullPointerException during updateEnable: "+ids);
			log.error(Exceptions.getStackTraceAsString(e));
		}catch (DataIntegrityViolationException e) {
			map.put("responseid", 0);
			map.put("message", "请检查约束条件:"+ExceptionsMsgAlert.alertDataIntegrityViolationException(persistentClass, Exceptions.getStackTraceAsString(e)));
			log.error("LogicController catch DataIntegrityViolationException during updateEnable: "+ids);
			log.error(Exceptions.getStackTraceAsString(e));
		}catch (TransactionRollbackException e) {
			map.put("responseid", 0);
			map.put("message", "提交事务异常!");
			log.error("LogicController catch TransactionRollbackException during create: "+ids);
			log.error(Exceptions.getStackTraceAsString(e));
		}catch (AppException e) {
			map.put("responseid", 0);
			map.put("message", "数据异常，请确认后重新尝试!");
			log.error("LogicController catch AppException during updateEnable: "+ids);
			log.error(Exceptions.getStackTraceAsString(e));
		}catch (Exception e) {
			map.put("responseid", 0);
			map.put("message", "数据异常，请确认后重新尝试!");
			log.error("LogicController catch Exception during updateEnable: "+ids);
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return map;
	}
	
	@Override
	public Map<String, Object> update(T o) throws Exception {
		if(!o.validate()){
			Map<String, Object> map = Maps.newHashMap();
			map.put("responseid", 0);
			map.put("message", "请先启用该数据，再进行修改!");
			return map;
		}
		else{
			return super.update(o);
		}
	}
}
