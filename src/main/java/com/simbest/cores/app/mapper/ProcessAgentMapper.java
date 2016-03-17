package com.simbest.cores.app.mapper;

import java.util.Collection;

import com.simbest.cores.app.model.ProcessAgent;
import com.simbest.cores.mapper.IGenericMapper;

public interface ProcessAgentMapper extends IGenericMapper<ProcessAgent,Integer> {
	
	Collection<ProcessAgent> getExpiresAgent();
	
}