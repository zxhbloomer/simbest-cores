package com.simbest.cores.app.model;

import javax.persistence.*;

import com.simbest.cores.utils.annotations.NotNullColumn;
import com.simbest.cores.utils.annotations.ProcessProperty;
import com.simbest.cores.utils.enums.ProcessEnum;

/**
 * 业务流程主状态
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_status", uniqueConstraints={@UniqueConstraint(columnNames={"processTypeId", "processHeaderId", "receiptId"})})
public class ProcessStatus extends ProcessModel<ProcessStatus> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5973898392541378419L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name="app_process_status_seq", sequenceName="app_process_status_seq")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="app_process_status_seq")
    @ProcessProperty
    protected Long id;

	//所有业务单据的主键必须统一类型，否则无法写入待办和审批记录
	@NotNullColumn(value="业务单据Id")
	@Column(name = "receiptId", nullable = false)
	private Long receiptId;

	//为了快速检索流程是否都已结束，因此主单据冗余存储环节类型、环节版本(当前流程版本下，是否有未完结的流程)
	@NotNullColumn(value="环节类型")
	@Column(name = "processStepType", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private ProcessEnum processStepType;
	
	@Column(name = "processStepVersion", nullable = false)
    private Integer processStepVersion;

	public Long getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}

	public ProcessEnum getProcessStepType() {
		return processStepType;
	}

	public void setProcessStepType(ProcessEnum processStepType) {
		this.processStepType = processStepType;
	}

	public Integer getProcessStepVersion() {
		return processStepVersion;
	}

	public void setProcessStepVersion(Integer processStepVersion) {
		this.processStepVersion = processStepVersion;
	}
	
	
}