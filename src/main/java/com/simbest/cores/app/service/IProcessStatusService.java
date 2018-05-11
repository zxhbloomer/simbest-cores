package com.simbest.cores.app.service;

import com.simbest.cores.app.model.ProcessStatus;
import com.simbest.cores.service.ILogicService;

import java.util.List;

public interface IProcessStatusService extends ILogicService<ProcessStatus, Long> {

	ProcessStatus getOne(Integer processTypeId, Integer processHeaderId, Long receiptId);

    Integer getJoinCount(Integer createUserId);

    List<ProcessStatus> getJoin();
	
	boolean checkProcessRunning(Integer processTypeId, Integer processHeaderId, Integer processStepVersion);

    int updateOnCompleted(ProcessStatus status);
}
