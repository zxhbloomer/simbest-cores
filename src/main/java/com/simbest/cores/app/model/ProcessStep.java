package com.simbest.cores.app.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.NotNullColumn;
import com.simbest.cores.utils.annotations.Unique;
import com.simbest.cores.utils.enums.ProcessEnum;

/**
 * 业务流程明细
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_step", uniqueConstraints={@UniqueConstraint(columnNames={"stepCode", "sversion"})})
public class ProcessStep extends GenericModel<ProcessStep> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6188237835028866729L;

	@Id
	@Column(name = "stepId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer stepId;
	
	@JsonProperty("id")
	@Column(name = "stepCode", length = 20, nullable = false)
	@Unique
	private String stepCode;
	
	@NotNullColumn(value="环节类型")
	@Column(name = "stepType", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private ProcessEnum stepType;
	
	@NotNullColumn(value="环节分类")
	@Column(name = "stepClass", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private ProcessEnum stepClass;
	
	@NotNullColumn(value="环节状态")
	@Column(name = "stepDesc", nullable = false, length = 50)
	private String stepDesc;
	
	@Column(name = "passId", nullable = true) //通过环节
	private String passId; 
	
	@Transient
	private String passStep; 
	
	@Column(name = "failId", nullable = true) //驳回环节
	private String failId; 
	
	@Transient
	private String failStep; 
	
	@Column(name = "stopId", nullable = true) //终止环节
	private String stopId; 
	
	@Transient
	private String stopStep;
	
	@Column(name = "forkFromId", nullable = true) //分支来源环节
	private String forkFromId; 
	
	@NotNullColumn(value="流程头")
    @ManyToOne
	@JoinColumn(name = "headerId")
	@JsonIgnore
    private ProcessHeader header;	
    
	@NotNullColumn(value="流程类型")
	@Column(name = "typeId")
    private Integer typeId;
    
	@Column(name = "ltop", nullable=true)
    private String ltop;
	
	@Column(name = "lleft", nullable=true)
    private String lleft;
	
	@Column(name = "sversion", nullable = false)
	@JsonIgnore
    private Integer sversion;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="processStep")
	private List<ProcessAudit> audits;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="processStep")
	private List<ProcessStepConfiguration> configurations;
	
	@Transient
	private String process;
	   
	public ProcessStep() {
		super();
	}

	public ProcessStep(Integer stepId) {
		super();
		this.stepId = stepId;
	}


	/**
	 * @return the stepId
	 */
	public Integer getStepId() {
		return stepId;
	}

	/**
	 * @param stepId the stepId to set
	 */
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	public String getStepCode() {
		return stepCode;
	}

	public void setStepCode(String stepCode) {
		this.stepCode = stepCode;
	}

	/**
	 * @return the stepType
	 */
	public ProcessEnum getStepType() {
		return stepType;
	}

	/**
	 * @param stepType the stepType to set
	 */
	public void setStepType(ProcessEnum stepType) {
		this.stepType = stepType;
	}

	/**
	 * @return the stepDesc
	 */
	public String getStepDesc() {
		return stepDesc;
	}

	/**
	 * @param stepDesc the stepDesc to set
	 */
	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}

	/**
	 * @return the passId
	 */
	public String getPassId() {
		return passId;
	}

	/**
	 * @param passId the passId to set
	 */
	public void setPassId(String passId) {
		this.passId = passId;
	}

	/**
	 * @return the passStep
	 */
	public String getPassStep() {
		return passStep;
	}

	/**
	 * @param passStep the passStep to set
	 */
	public void setPassStep(String passStep) {
		this.passStep = passStep;
	}
	
	/**
	 * @return the failId
	 */
	public String getFailId() {
		return failId;
	}

	/**
	 * @param failId the failId to set
	 */
	public void setFailId(String failId) {
		this.failId = failId;
	}

	/**
	 * @return the failStep
	 */
	public String getFailStep() {
		return failStep;
	}

	/**
	 * @param failStep the failStep to set
	 */
	public void setFailStep(String failStep) {
		this.failStep = failStep;
	}

	/**
	 * @return the stopId
	 */
	public String getStopId() {
		return stopId;
	}

	/**
	 * @param stopId the stopId to set
	 */
	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	/**
	 * @return the stopStep
	 */
	public String getStopStep() {
		return stopStep;
	}

	/**
	 * @param stopStep the stopStep to set
	 */
	public void setStopStep(String stopStep) {
		this.stopStep = stopStep;
	}

	/**
	 * @return the header
	 */
	public ProcessHeader getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(ProcessHeader header) {
		this.header = header;
	}

	/**
	 * @return the typeId
	 */
	public Integer getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public List<ProcessStepConfiguration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<ProcessStepConfiguration> configurations) {
		this.configurations = configurations;
	}
	
	public List<ProcessAudit> getAudits() {
		return audits;
	}

	public void setAudits(List<ProcessAudit> audits) {
		this.audits = audits;
	}

	public String getLtop() {
		return ltop;
	}

	public void setLtop(String ltop) {
		this.ltop = ltop;
	}

	public String getLleft() {
		return lleft;
	}

	public void setLleft(String lleft) {
		this.lleft = lleft;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Integer getSversion() {
		return sversion;
	}

	public void setSversion(Integer sversion) {
		this.sversion = sversion;
	}

	public ProcessEnum getStepClass() {
		return stepClass;
	}

	public void setStepClass(ProcessEnum stepClass) {
		this.stepClass = stepClass;
	}

	/**
	 * @return the forkFromId
	 */
	public String getForkFromId() {
		return forkFromId;
	}

	/**
	 * @param forkFromId the forkFromId to set
	 */
	public void setForkFromId(String forkFromId) {
		this.forkFromId = forkFromId;
	}


	
}