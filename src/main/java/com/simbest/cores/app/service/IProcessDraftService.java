package com.simbest.cores.app.service;

import com.simbest.cores.app.model.ProcessDraft;
import com.simbest.cores.service.ILogicService;

public interface IProcessDraftService extends ILogicService<ProcessDraft, Long> {
	ProcessDraft getOne(Integer processTypeId, Integer processHeaderId, Long receiptId);
}
