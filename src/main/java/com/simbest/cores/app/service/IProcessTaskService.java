package com.simbest.cores.app.service;

import com.simbest.cores.app.model.ProcessTask;
import com.simbest.cores.service.IGenericService;

public interface IProcessTaskService extends IGenericService<ProcessTask, Long> {

	ProcessTask getCurrentUserTask(Integer typeId, Integer headerId,
			Long receiptId, Integer currentUserId);

    int deleteById(Long id);

}
