package com.simbest.cores.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.simbest.cores.app.model.ProcessAudit;
import com.simbest.cores.mapper.IGenericMapper;

public interface ProcessAuditMapper extends IGenericMapper<ProcessAudit,Integer> {
	
	List<ProcessAudit> getByStep(@Param("processStepId") Integer processStepId);
	
}