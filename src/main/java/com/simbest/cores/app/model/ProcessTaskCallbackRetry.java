package com.simbest.cores.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.NotNullColumn;

import javax.persistence.*;
import java.util.Date;

/**
 * 业务代办回调任务
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_task_callback_retry")
public class ProcessTaskCallbackRetry extends GenericModel<ProcessTaskCallbackRetry>{


    private static final long serialVersionUID = 406815278844587818L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "processServiceClass", nullable = false)
	private String processServiceClass;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastExecuteDate", nullable = false)
    private Date lastExecuteDate;

    @Column(name = "executeTimes", nullable = false)
    private Integer executeTimes;

    @Column(name = "callbackType", nullable = false)
    private String callbackType;

    private Long taskId;

    @NotNullColumn(value="业务类型")
    @Column(name = "typeId", nullable = false)
    private Integer typeId;

    @NotNullColumn(value="业务单据")
    @Column(name = "headerId", nullable = false)
    private Integer headerId;

    @NotNullColumn(value="业务单据Id")
    @Column(name = "receiptId", nullable = false)
    private Long receiptId;

    @NotNullColumn(value="业务当前环节")
    @Column(name = "stepId", nullable = false)
    private Integer stepId;

    @NotNullColumn(value="受理人")
    @Column(name = "currentUserId", nullable = true, length = 50)
    private Integer currentUserId;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcessServiceClass() {
        return processServiceClass;
    }

    public void setProcessServiceClass(String processServiceClass) {
        this.processServiceClass = processServiceClass;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getLastExecuteDate() {
        return lastExecuteDate;
    }

    public void setLastExecuteDate(Date lastExecuteDate) {
        this.lastExecuteDate = lastExecuteDate;
    }

    public Integer getExecuteTimes() {
        return executeTimes;
    }

    public void setExecuteTimes(Integer executeTimes) {
        this.executeTimes = executeTimes;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Integer headerId) {
        this.headerId = headerId;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCallbackType() {
        return callbackType;
    }

    public void setCallbackType(String callbackType) {
        this.callbackType = callbackType;
    }
}