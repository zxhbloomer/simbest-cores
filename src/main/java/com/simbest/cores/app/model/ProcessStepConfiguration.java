/**
 * 
 */
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.NotNullColumn;
import com.simbest.cores.utils.enums.ProcessEnum;

/**
 * 流程环节跃迁附加配置
 * 
 * 当满足一组条件conditions，返回对应的审批配置Id
 * 
 * 流程环节可以附加配置多个ProcessStepConfiguration，
 * 但是只能有一个ProcessStepConfiguration的ProcessStepConfiguration条件配置为true
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_step_configuration")
public class ProcessStepConfiguration extends GenericModel<ProcessStepConfiguration> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4000341203442306902L;

	public enum logical {
		And, Or
	}

	@Id
	@Column(name = "configurationId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Integer configurationId;

	@Column(name = "logic", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private logical logic;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "configuration")
	private List<ProcessStepCondition> conditions;

	@NotNullColumn(value="审批类型 ") //audit_role("基于角色"), audit_user("基于用户")
	@Column(name = "subjectType", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private ProcessEnum subjectType;
	
	@NotNullColumn(value="审批对象")  //支持保存多个SysRoleId 或者 多个SysUserId
	@Column(name = "subjects", nullable = false)
	private String subjects;

	@ManyToOne
	@JoinColumn(name = "processStepId", nullable = false)
	@JsonIgnore
	private ProcessStep processStep;
	
	@Column(name = "cversion", nullable = false)
	@JsonIgnore
    private Integer cversion;
	
	public ProcessStepConfiguration() {
		super();
	}

	public ProcessStepConfiguration(List<ProcessStepCondition> conditions,
			logical logic, ProcessEnum subjectType, String subjects) {
		super();
		this.conditions = conditions;
		this.logic = logic;
		this.subjectType = subjectType;
		this.subjects = subjects;
	}

	/**
	 * @return the configurationId
	 */
	public Integer getConfigurationId() {
		return configurationId;
	}

	/**
	 * @param configurationId the configurationId to set
	 */
	public void setConfigurationId(Integer configurationId) {
		this.configurationId = configurationId;
	}

	public logical getLogic() {
		return logic;
	}

	public void setLogic(logical logic) {
		this.logic = logic;
	}

	public List<ProcessStepCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<ProcessStepCondition> conditions) {
		this.conditions = conditions;
	}

	public ProcessEnum getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(ProcessEnum subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public ProcessStep getProcessStep() {
		return processStep;
	}

	public void setProcessStep(ProcessStep processStep) {
		this.processStep = processStep;
	}

	public Integer getCversion() {
		return cversion;
	}

	public void setCversion(Integer cversion) {
		this.cversion = cversion;
	}


}
