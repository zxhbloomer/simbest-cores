package com.simbest.cores.app.service;

import com.simbest.cores.app.model.ProcessAuditLog;
import com.simbest.cores.service.IGenericService;

public interface IProcessAuditLogService extends IGenericService<ProcessAuditLog, Long> {

    /**
     * 删除第一条起草日志
     *
     * @param o
     * @return
     */
    int deleteCreationLog(ProcessAuditLog o);

    /**
     * 修改日志审批意见
     *
     * @param o
     * @return
     */
    int updateLogOpnion(ProcessAuditLog o);
}
