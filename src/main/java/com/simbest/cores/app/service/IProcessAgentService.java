package com.simbest.cores.app.service;

import java.util.Collection;

import com.simbest.cores.app.model.ProcessAgent;
import com.simbest.cores.service.IGenericService;

public interface IProcessAgentService extends IGenericService<ProcessAgent, Integer> {

	Collection<ProcessAgent> getExpiresAgent();
	
}
