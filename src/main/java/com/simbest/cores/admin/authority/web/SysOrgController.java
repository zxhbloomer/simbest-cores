package com.simbest.cores.admin.authority.web;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.simbest.cores.admin.authority.model.SysOrg;
import com.simbest.cores.admin.authority.service.ISysOrgAdvanceService;
import com.simbest.cores.utils.annotations.LogAudit;
import com.simbest.cores.utils.editors.SysOrgEditor;
import com.simbest.cores.web.BaseController;
//import com.wordnik.swagger.annotations.ApiOperation;
//import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = {"/action/sso/admin/authority/sysorg", //SSO跳转，Shrio不拦截
"/action/admin/authority/sysorg"}) //后台管理跳转，Shrio拦截校验权限
public class SysOrgController extends BaseController<SysOrg, Integer>{	

	public final Log log = LogFactory.getLog(SysOrgController.class);

	@Autowired
	private ISysOrgAdvanceService sysOrgAdvanceService;
	
	public SysOrgController() {
		super(SysOrg.class, "/action/admin/authority/sysorg/sysOrgList", "/action/admin/authority/sysorg/sysOrgForm");
	}
	
	@PostConstruct
	private void initService() {
		setService(sysOrgAdvanceService);
	}
	
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		super.initBinder(binder);
		binder.registerCustomEditor(SysOrg.class, new SysOrgEditor(sysOrgAdvanceService));
	}
	
	@RequiresPermissions("admin:authority:sysorg:query")
	@RequestMapping(value = "/sysOrgList", method = RequestMethod.GET)
	public ModelAndView openListView(Date ssDate, Date eeDate) throws Exception {
		return super.openListView(ssDate, eeDate);
	}

	@RequiresPermissions(value={"admin:authority:sysorg:create","admin:authority:sysorg:update"},logical=Logical.OR)
	@RequestMapping(value = "/sysOrgForm", method = RequestMethod.GET)
	public ModelAndView openFormView(Date ssDate, Date eeDate) throws Exception {
		return super.openFormView(ssDate, eeDate);
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
//	@ApiOperation(value = "获取组织", httpMethod = "POST", response = SysOrg.class, notes = "获取单个组织")
	@ResponseBody
	@LogAudit
//	public Map<String, Object> get(@ApiParam(required=true, name="o", value="组织json数据") @RequestBody SysOrg o) throws Exception {
	public Map<String, Object> get(@RequestBody SysOrg o) throws Exception {	
		return super.get(o.getId());
	}
	
	@RequestMapping(value = "/query", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
//	@ApiOperation(value = "查询组织", httpMethod = "POST", response = Map.class, notes = "获取多个组织")
	@ResponseBody
	@LogAudit
	@Override
//	public Map<String, Object> query(@ApiParam(required=true, name="o", value="组织json数据")@RequestBody SysOrg o) throws Exception {			
	public Map<String, Object> query(@RequestBody SysOrg o) throws Exception {
		Map<String, Object> result = Maps.newHashMap();
		Collection<SysOrg> list = sysOrgAdvanceService.getAll(o);
		Map<String, Object> dataMap = super.wrapQueryResult((List<SysOrg>) list);
		result.put("data", dataMap);
		result.put("message", "");
		result.put("responseid", 1);
		return result;
	}

	/**
	 * 面试使用
	 */
	@RequestMapping(value = "/queryEmpty", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> queryEmpty() throws Exception {			
		Map<String, Object> result = Maps.newHashMap();
		Collection<SysOrg> list = sysOrgAdvanceService.getAll();
		Map<String, Object> dataMap = super.wrapQueryResult((List<SysOrg>) list);
		result.put("data", dataMap);
		result.put("message", "");
		result.put("responseid", 1);
		return result;
	}

	
	@RequiresPermissions("admin:authority:sysorg:create")
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	@LogAudit
	@Override
	public Map<String, Object> create(@RequestBody SysOrg o) throws Exception {
		if(o.getParentId() != null){
			o.setParent(new SysOrg(o.getParentId()));
		}
		Map<String, Object> map = super.create(o);
		map.put("data", sysOrgAdvanceService.getById(o.getId()));		
		return map;
	}

	@RequiresPermissions("admin:authority:sysorg:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	@LogAudit
	@Override
	public Map<String, Object> update(@RequestBody SysOrg o) throws Exception {
		Map<String, Object> map = Maps.newHashMap();
		if(o.getId().equals(o.getParentId())){
			map.put("message", "操作失败: 上级部门不允许是自身！");
			map.put("responseid", 0);
		}else{
			if(o.getParentId() != null){
				o.setParent(new SysOrg(o.getParentId()));
			}
			map = super.update(o);
			map.put("data", sysOrgAdvanceService.getById(o.getId()));
		}
		return map;
	}

	@RequiresPermissions("admin:authority:sysorg:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	@LogAudit
	public Map<String, Object> delete(@RequestBody SysOrg o) throws Exception {
		return super.delete(o.getId());
	}
	
	@RequiresPermissions("admin:authority:sysorg:delete")
	@RequestMapping(value = "/deletes", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	@LogAudit
	@Override
	public Map<String, Object> deletes(@RequestBody Integer[] ids) throws Exception {
		return super.deletes(ids);
	}
	
	/**
	 * 获取组织下拉框
	 * @param o
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSysOrgMap", method = RequestMethod.GET)
	@ResponseBody
	public Map<Integer, String> getSysOrgMap(SysOrg o) throws Exception {
		Map<Integer, String> map = Maps.newLinkedHashMap();
		Collection<SysOrg> list = sysOrgAdvanceService.getAll(o);
		for(SysOrg sysOrg : list){
			map.put(sysOrg.getId(), sysOrg.getOrgName());
		}
		return map;
	}
	
	/**
	 * 获取组织下拉框
	 * @param o
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFullSysOrgMap", method = RequestMethod.GET)
	@ResponseBody
	public Map<Integer, String> getFullSysOrgMap(SysOrg o) throws Exception {	
		Map<Integer, String> map = Maps.newLinkedHashMap();
		SysOrg root = sysOrgAdvanceService.getRoot();
		map.put(root.getId(), root.getOrgName());	
		map.putAll(getSysOrgMap(o));
		return map;
	}

}
