package com.simbest.cores.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simbest.cores.admin.authority.model.SysOrg;
import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.NotNullColumn;

import javax.persistence.*;
import java.util.Date;

/**
 * 业务代办回调日志记录
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_task_callback_log")
public class ProcessTaskCallbackLog extends GenericModel<ProcessTaskCallbackLog> {


    private static final long serialVersionUID = 5167156979941299089L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name="app_callback_log_seq", sequenceName="app_callback_log_seq")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="app_callback_log_seq")
    private Integer id;

    private Long taskId;

    @Column(name = "callbackType", nullable = false)
	private String callbackType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "callbackStartDate", nullable = false)
	private Date callbackStartDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "callbackEndDate", nullable = false)
    private Date callbackEndDate;

    @Column(name = "callbackDuration", nullable = false)
    private Long callbackDuration;

    @Column(name = "callbackResult", nullable = false, columnDefinition = "int default 1")
    protected Boolean callbackResult;

    @Column(name = "callbackError", nullable = true, length = 2000)
    private String callbackError;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getCallbackType() {
        return callbackType;
    }

    public void setCallbackType(String callbackType) {
        this.callbackType = callbackType;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getCallbackStartDate() {
        return callbackStartDate;
    }

    public void setCallbackStartDate(Date callbackStartDate) {
        this.callbackStartDate = callbackStartDate;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getCallbackEndDate() {
        return callbackEndDate;
    }

    public void setCallbackEndDate(Date callbackEndDate) {
        this.callbackEndDate = callbackEndDate;
    }

    public Long getCallbackDuration() {
        return callbackDuration;
    }

    public void setCallbackDuration(Long callbackDuration) {
        this.callbackDuration = callbackDuration;
    }

    public Boolean getCallbackResult() {
        return callbackResult;
    }

    public void setCallbackResult(Boolean callbackResult) {
        this.callbackResult = callbackResult;
    }

    public String getCallbackError() {
        return callbackError;
    }

    public void setCallbackError(String callbackError) {
        this.callbackError = callbackError;
    }
}