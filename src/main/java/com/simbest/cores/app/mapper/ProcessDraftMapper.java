package com.simbest.cores.app.mapper;

import org.apache.ibatis.annotations.Param;

import com.simbest.cores.app.model.ProcessDraft;
import com.simbest.cores.mapper.ILogicMapper;

public interface ProcessDraftMapper extends ILogicMapper<ProcessDraft,Long> {
	ProcessDraft getOne(@Param(value = "processTypeId") Integer processTypeId,
			@Param(value = "processHeaderId") Integer processHeaderId,
			@Param(value = "receiptId") Long receiptId);
}