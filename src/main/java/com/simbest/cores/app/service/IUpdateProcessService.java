/**
 * 
 */
package com.simbest.cores.app.service;

import java.util.Collection;
import java.util.List;

import com.simbest.cores.app.model.ProcessStep;
import com.simbest.cores.service.IGenericService;

/**
 * 修改业务流程
 * 
 * @author lishuyi
 *
 */
public interface IUpdateProcessService extends IGenericService<ProcessStep, Integer> {
	int saveSteps(List<ProcessStep> stepList) throws IllegalArgumentException, IllegalAccessException;
	
	Collection<ProcessStep> loadStepsByHeader(String headerCode);
}
