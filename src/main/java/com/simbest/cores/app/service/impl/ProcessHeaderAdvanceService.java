package com.simbest.cores.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.simbest.cores.app.model.ProcessHeader;
import com.simbest.cores.app.model.ProcessStep;
import com.simbest.cores.app.service.IProcessHeaderAdvanceService;
import com.simbest.cores.cache.IGenericCache;
import com.simbest.cores.exceptions.UnExpectedStepException;
import com.simbest.cores.service.IGenericService;
import com.simbest.cores.service.impl.GenericAdvanceService;


@Service(value="processHeaderAdvanceService")
public class ProcessHeaderAdvanceService extends GenericAdvanceService<ProcessHeader,Integer> implements IProcessHeaderAdvanceService{	
	
	@Autowired
	public ProcessHeaderAdvanceService(
			@Qualifier(value="processHeaderService")IGenericService<ProcessHeader, Integer> processHeaderService,
			@Qualifier(value="processHeaderCache")IGenericCache<ProcessHeader,Integer> processHeaderCache) {
		super(processHeaderService, processHeaderCache);
	}

	@Override
	public List<ProcessStep> getSteps(Integer headerId) {
		ProcessHeader header = getKeyHashOps().get(headerId);
		if(header == null){
			log.error(String.format("10005 Not found processHeader: %s", headerId ));
			throw new UnExpectedStepException("10005", String.format("10005 Not found processHeader: %s", headerId ));
		}
		return header.getSteps();
	}	
	
}
