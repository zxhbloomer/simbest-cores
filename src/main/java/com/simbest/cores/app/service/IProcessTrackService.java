package com.simbest.cores.app.service;

import com.simbest.cores.app.model.ProcessTrack;
import com.simbest.cores.service.IGenericService;

public interface IProcessTrackService extends IGenericService<ProcessTrack, Long> {

	boolean checkAllParallelToJoin(ProcessTrack track, String joinId);
}
