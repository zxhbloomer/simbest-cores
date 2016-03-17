package com.simbest.cores.app.service;
import java.util.List;

import com.simbest.cores.app.model.ProcessHeader;
import com.simbest.cores.app.model.ProcessStep;
import com.simbest.cores.service.IGenericAdvanceService;

public interface IProcessHeaderAdvanceService extends IGenericAdvanceService<ProcessHeader,Integer>{
	
	/**
	 * 获取流程运行版本所有环节
	 */
	List<ProcessStep> getSteps(Integer headerId);

}
