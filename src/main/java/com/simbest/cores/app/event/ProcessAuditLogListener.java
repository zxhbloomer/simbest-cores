/**
 * 
 */
package com.simbest.cores.app.event;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.simbest.cores.admin.authority.model.ShiroUser;
import com.simbest.cores.app.model.ProcessAuditLog;
import com.simbest.cores.app.model.ProcessJsonData;
import com.simbest.cores.app.model.ProcessModel;
import com.simbest.cores.app.service.IProcessHeaderAdvanceService;
import com.simbest.cores.app.service.IProcessStepAdvanceService;
import com.simbest.cores.app.service.IProcessTypeAdvanceService;
import com.simbest.cores.service.IGenericService;
import com.simbest.cores.shiro.AppUserSession;
import com.simbest.cores.utils.DateUtil;

/**
 * @author lishuyi
 *
 */
@Component
public class ProcessAuditLogListener<T extends ProcessModel<?>, PK extends Serializable> implements ApplicationListener<ProcessAuditLogEvent<T,PK>> {

	@Autowired
	private AppUserSession appUserSession;
	
	@Autowired
	@Qualifier("processAuditLogService")
	private IGenericService<ProcessAuditLog, Long> auditLogService;
	
	@Autowired
	private IProcessStepAdvanceService processStepCache;
	
	@Autowired
	private IProcessHeaderAdvanceService processHeaderAdvanceService;
	
	@Autowired
	private IProcessTypeAdvanceService processTypeAdvanceService;
	
	@Override
	public void onApplicationEvent(ProcessAuditLogEvent<T, PK> event) {
		ProcessAuditLog auditLog = new ProcessAuditLog();		
		ProcessJsonData<T, PK> processData = event.getProcessData();		
		auditLog.setTypeId(processData.getBusinessData().getProcessTypeId()); //流程类型
		auditLog.setTypeDesc(processTypeAdvanceService.loadByKey(processData.getBusinessData().getProcessTypeId()).getTypeDesc());
		auditLog.setHeaderId(processData.getBusinessData().getProcessHeaderId()); //流程头
		auditLog.setHeaderDesc(processHeaderAdvanceService.loadByKey(processData.getBusinessData().getProcessHeaderId()).getHeaderDesc());
		auditLog.setReceiptId(processData.getBusinessData().getId());
		auditLog.setResult(processData.getResult());
		auditLog.setResultDesc(processData.getResult().getValue());
		auditLog.setOpinion(processData.getOpinion());	
		auditLog.setStepId(processData.getBusinessData().getProcessStepId());//待处理环节
		auditLog.setStepDesc(processStepCache.loadByKey(processData.getBusinessData().getProcessStepId()).getStepDesc());//待处理环节
		auditLog.setPreviousStepId(event.getPreviousStepId());//已处理环节
		auditLog.setPreviousStepDesc(processStepCache.loadByKey(event.getPreviousStepId()).getStepDesc());	
		ShiroUser user = appUserSession.getCurrentUser();
		//处理人信息
		auditLog.setOrgId(user.getOrgId()); 
		auditLog.setOrgName(user.getOrgName());
		auditLog.setCreateUserId(user.getUserId());
		auditLog.setCreateUserCode(user.getUserCode());
		auditLog.setCreateUserName(user.getUserName());
		auditLog.setCreateDate(DateUtil.getCurrent());
		auditLogService.create(auditLog);
	}


}
