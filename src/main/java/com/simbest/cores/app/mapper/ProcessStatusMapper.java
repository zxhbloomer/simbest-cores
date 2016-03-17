package com.simbest.cores.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.simbest.cores.app.model.ProcessStatus;
import com.simbest.cores.mapper.ILogicMapper;

public interface ProcessStatusMapper extends ILogicMapper<ProcessStatus,Long> {
	
	ProcessStatus getOne(@Param(value = "processTypeId") Integer processTypeId,
			@Param(value = "processHeaderId") Integer processHeaderId,
			@Param(value = "receiptId") Long receiptId);

    Integer getJoinCount(@Param(value = "createUserId") Integer createUserId);

	List<ProcessStatus> getJoin(@Param(value = "createUserId") Integer createUserId);
	
	Integer checkProcessRunning(@Param(value = "processTypeId") Integer processTypeId,
			@Param(value = "processHeaderId") Integer processHeaderId,
			@Param(value = "processStepVersion") Integer processStepVersion);
}