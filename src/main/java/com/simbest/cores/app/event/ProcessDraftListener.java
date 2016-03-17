/**
 * 
 */
package com.simbest.cores.app.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.simbest.cores.app.model.ProcessModel;
import com.simbest.cores.app.service.IProcessDraftService;
import com.simbest.cores.app.service.IProcessStatusService;

/**
 * @author lishuyi
 *
 */
@Component
public class ProcessDraftListener implements ApplicationListener<ProcessDraftEvent>{
	private static transient final Log log = LogFactory.getLog(ProcessDraftListener.class);
	
	@Autowired
	private ProcessTaskListener<?,?> processTaskListener;

	@Autowired
	private IProcessStatusService processStatusService;
	
	@Autowired
	private IProcessDraftService processDraftService;
	
	@Override
	public void onApplicationEvent(ProcessDraftEvent event) {
		ProcessModel<?> process = event.getProcess();		
		int ret = processDraftService.create(processTaskListener.getProcessDraft(process));
		log.debug(ret);
	}

	
}
