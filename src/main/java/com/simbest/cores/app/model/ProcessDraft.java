package com.simbest.cores.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.simbest.cores.utils.annotations.NotNullColumn;

/**
 * 业务流程草稿
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_draft", uniqueConstraints={@UniqueConstraint(columnNames={"processTypeId", "processHeaderId", "receiptId"})})
public class ProcessDraft extends ProcessModel<ProcessDraft> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6133954955712649528L;
	
	//所有业务单据的主键必须统一类型，否则无法写入待办和审批记录
	@NotNullColumn(value="业务单据Id")
	@Column(name = "receiptId", nullable = false)
	private Long receiptId;
	
	public Long getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}
	
}