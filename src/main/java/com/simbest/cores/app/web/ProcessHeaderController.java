package com.simbest.cores.app.web;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.simbest.cores.app.model.ProcessHeader;
import com.simbest.cores.app.service.IProcessHeaderAdvanceService;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.service.IGenericService;
import com.simbest.cores.web.BaseController;

@Controller
@RequestMapping(value = {"/action/sso/admin/paras/process/header", //SSO跳转，Shrio不拦截
"/action/admin/paras/process/header"}) //后台管理跳转，Shrio拦截校验权限
public class ProcessHeaderController extends BaseController<ProcessHeader, Integer>{

	public final Log log = LogFactory.getLog(ProcessHeaderController.class);

	@Autowired
	private IProcessHeaderAdvanceService processHeaderAdvanceService;
	
	@Autowired
	@Qualifier(value="processHeaderService")
	private IGenericService<ProcessHeader, Integer> service;
	
	public ProcessHeaderController() {
		super(ProcessHeader.class, null, null);	
	}

	@PostConstruct
	private void initService() {
		setService(service);
	}
	
	@Override
	public Map<String, Object> query(ProcessHeader o) throws Exception {
		Map<String, Object> result = Maps.newHashMap();
		Collection<ProcessHeader> list = processHeaderAdvanceService.getValues();
		result.put("iTotalRecords", list.size());
		result.put("iTotalDisplayRecords", list.size());
		result.put("aaData", list);
		return result;
	}
	
	@Override
	public Map<String, Object> get(Integer id) throws Exception{
		Map<String, Object> map = Maps.newHashMap();
		ProcessHeader o = processHeaderAdvanceService.loadByKey(id);
		map.put("message", o != null ? "":"操作失败!");
		map.put("responseid", o != null ? 1:0);
		map.put("data", o != null ? o:null);
		return map;
	}
	
	/**
	 * 启用或者禁用业务流程
	 * @param enabled
	 * @param processHeaderCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateEnable/{processHeaderCode}/{enabled}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateEnable(@PathVariable("enabled")Boolean enabled, @PathVariable("processHeaderCode")String processHeaderCode) throws Exception {
		Map<String, Object> map = Maps.newHashMap();
		try {			 
			ProcessHeader o = processHeaderAdvanceService.loadByUnique(processHeaderCode);
			o.setEnabled(enabled);
			int ret = service.update(o);
			if(ret>0){
				processHeaderAdvanceService.saveOrUpdate(o.getHeaderId(), o);
			}
			map.put("responseid", ret > 0 ? 1:ret);
		}catch (Exception e) {
			map.put("responseid", 0);			
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return map;
	}

}
