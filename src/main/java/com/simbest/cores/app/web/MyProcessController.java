package com.simbest.cores.app.web;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.simbest.cores.admin.authority.model.ShiroUser;
import com.simbest.cores.app.model.ProcessAuditLog;
import com.simbest.cores.app.model.ProcessDraft;
import com.simbest.cores.app.model.ProcessStatus;
import com.simbest.cores.app.model.ProcessTask;
import com.simbest.cores.app.service.IProcessDraftService;
import com.simbest.cores.app.service.IProcessStatusService;
import com.simbest.cores.service.IGenericService;
import com.simbest.cores.shiro.AppUserSession;
import com.simbest.cores.utils.decorators.BeanDecoratorExecutor;
import com.simbest.cores.utils.decorators.ProcessSubjectsDescDecorator;
import com.simbest.cores.web.BaseController;

@Controller
@RequestMapping(value = {"/action/sso/myProcess", //SSO跳转，Shrio不拦截
	"/action/myProcess"}) //后台管理跳转，Shrio拦截校验权限
public class MyProcessController extends BaseController<ProcessAuditLog, Long>{

	public final Log log = LogFactory.getLog(MyProcessController.class);
	
	@Autowired
	@Qualifier("processAuditLogService")
	private IGenericService<ProcessAuditLog, Long> auditLogService;
	
	@Autowired
	@Qualifier("processTaskService")
	private IGenericService<ProcessTask, Long> taskService;
	
	@Autowired
	private IProcessDraftService processDraftService;
	
	@Autowired
	private AppUserSession appUserSession;
	
	@Autowired
	private IProcessStatusService statusService;

	@Autowired
	private ProcessSubjectsDescDecorator decorator;
	
	public MyProcessController() {
		super(ProcessAuditLog.class, null, null);		
	}
	
	@RequestMapping(value = "/openMyTaskList", method = RequestMethod.POST)
	public String openMyTaskList() throws Exception {	
		return "redirect:/html/app/process/processTask.html";
	}	
	
	@RequestMapping(value = "/queryMyTaskCount", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public Integer queryMyTaskCount() throws Exception {	
		ProcessTask o = new ProcessTask();
		o.setCurrentUserId(appUserSession.getCurrentUser().getUserId());
		return taskService.getCount(o);
	}	
	
	//我的待办
	@RequestMapping(value = "/queryMyTask", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryMyTask(ProcessTask task) throws Exception {		
		ShiroUser user = appUserSession.getCurrentUser();	
		task.setCurrentUserId(user.getUserId());
		Collection<ProcessTask> list = taskService.getAll(task);	
		Map<String, Object> dataMap = super.wrapQueryResult((List<ProcessTask>) list);
		dataMap.put("Datas1", user);
		Map<String, Object> result = Maps.newHashMap();
		result.put("data", dataMap);
		result.put("message", "");
		result.put("responseid", 1);
		return result;
	}

    @RequestMapping(value = "/queryMyApplyCount", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Integer queryMyApplyCount() throws Exception {
        ProcessStatus o = new ProcessStatus();
        o.setCreateUserId(appUserSession.getCurrentUser().getUserId());
        return statusService.getCount(o);
    }

    //我的申请
	@RequestMapping(value = "/queryMyApply", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryMyApply(ProcessStatus status) throws Exception {
		ShiroUser user = appUserSession.getCurrentUser();
		status.setCreateUserId(user.getUserId());
		status.setOrderByClause("i.createDate DESC");
		Collection<ProcessStatus> list = statusService.getAll(status);
		Map<String, Object> dataMap = super.wrapQueryResult((List<ProcessStatus>) list);
		if(list.size() > 0){
			List<Object[]> decorators = Lists.newArrayList();
			decorators.add(new Object[]{decorator, "subjects", "subjectsDesc"});
			BeanDecoratorExecutor.populates(list, decorators);
		}

		dataMap.put("Datas1", user);
		Map<String, Object> result = Maps.newHashMap();
		result.put("data", dataMap);
		result.put("message", "");
		result.put("responseid", 1);
		return result;
	}

    @RequestMapping(value = "/getJoinCount", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Integer getJoinCount() throws Exception {
        return statusService.getJoinCount(appUserSession.getCurrentUser().getUserId());
    }

	//我的已办
	@RequestMapping(value = "/queryMyJoin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryMyJoin(ProcessStatus status) throws Exception {
		ShiroUser user = appUserSession.getCurrentUser();
		List<ProcessStatus> list = statusService.getJoin();
		Map<String, Object> dataMap = super.wrapQueryResult((List<ProcessStatus>) list);
		if(list.size() > 0){
			List<Object[]> decorators = Lists.newArrayList();
			decorators.add(new Object[]{decorator, "subjects", "subjectsDesc"});
			BeanDecoratorExecutor.populates(list, decorators);
		}
		
		dataMap.put("Datas1", user);
		Map<String, Object> result = Maps.newHashMap();
		result.put("data", dataMap);
		result.put("message", "");
		result.put("responseid", 1);
		return result;
	}

    @RequestMapping(value = "/queryMyDraftCount", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Integer queryMyDraftCount() throws Exception {
        ProcessDraft o = new ProcessDraft();
        o.setCreateUserId(appUserSession.getCurrentUser().getUserId());
        return processDraftService.getCount(o);
    }

    //我的草稿
	@RequestMapping(value = "/queryMyDraft", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryMyDraft(ProcessDraft o) throws Exception {
		ShiroUser user = appUserSession.getCurrentUser();
		o.setCreateUserId(user.getUserId());
		Collection<ProcessDraft> list = processDraftService.getAll(o);
		Map<String, Object> dataMap = super.wrapQueryResult((List<ProcessDraft>) list);
		dataMap.put("Datas1", user);
		Map<String, Object> result = Maps.newHashMap();
		result.put("data", dataMap);
		result.put("message", "");
		result.put("responseid", 1);
		return result;
	}
}
