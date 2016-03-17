package com.simbest.cores.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.simbest.cores.app.model.ProcessType;
import com.simbest.cores.app.service.IProcessTypeAdvanceService;
import com.simbest.cores.cache.IGenericCache;
import com.simbest.cores.service.IGenericService;
import com.simbest.cores.service.impl.GenericAdvanceService;


@Service(value="processTypeAdvanceService")
public class ProcessTypeAdvanceService extends GenericAdvanceService<ProcessType,Integer> implements IProcessTypeAdvanceService{	
	
	@Autowired
	public ProcessTypeAdvanceService(
			@Qualifier(value="processTypeService")IGenericService<ProcessType, Integer> processTypeService,
			@Qualifier(value="processTypeCache")IGenericCache<ProcessType,Integer> processTypeCache) {
		super(processTypeService, processTypeCache);
	}
	
}
