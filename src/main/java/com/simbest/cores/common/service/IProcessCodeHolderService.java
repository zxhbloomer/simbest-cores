package com.simbest.cores.common.service;

import com.simbest.cores.common.model.ProcessCodeHolder;
import com.simbest.cores.service.IGenericService;

public interface IProcessCodeHolderService extends IGenericService<ProcessCodeHolder, Long> {

    String getNextCode(String prefix);
}
