package com.simbest.cores.app.mapper;

import org.apache.ibatis.annotations.Param;

import com.simbest.cores.app.model.ProcessTask;
import com.simbest.cores.mapper.IGenericMapper;

public interface ProcessTaskMapper extends IGenericMapper<ProcessTask, Long> {
	ProcessTask getCurrentUserTask(@Param("typeId") Integer typeId,
			@Param("headerId") Integer headerId,
			@Param("receiptId") Long receiptId,
			@Param("currentUserId") Integer currentUserId);

    int deleteById(@Param("id") Long id);
}
