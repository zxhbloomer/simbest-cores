/**
 * 
 */
package com.simbest.cores.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simbest.cores.model.GenericModel;

/**
 * 流程环节跃迁附加条件
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_step_condition")
public class ProcessStepCondition extends GenericModel<ProcessStepCondition>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 965846198094369857L;

	public enum operation {
		gt, ge, lt, le, eq, Null
	}
	
	@Id
	@Column(name = "conditionId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Integer conditionId;
	
	@Column(name = "name", nullable = false, length = 20)
	private String name;
	
	@Column(name = "opt", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private operation opt;
	
	private Object value;
	
	@ManyToOne
    @JoinColumn(name = "configurationId", nullable=false)
	@JsonIgnore
	private ProcessStepConfiguration configuration;
	 
	public ProcessStepCondition() {
		super();
	}

	public ProcessStepCondition(String name, operation opt, Object value) {
		super();
		this.name = name;
		this.opt = opt;
		this.value = value;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the opt
	 */
	public operation getOpt() {
		return opt;
	}
	/**
	 * @param opt the opt to set
	 */
	public void setOpt(operation opt) {
		this.opt = opt;
	}
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the conditionId
	 */
	public Integer getConditionId() {
		return conditionId;
	}

	/**
	 * @param conditionId the conditionId to set
	 */
	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	public ProcessStepConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ProcessStepConfiguration configuration) {
		this.configuration = configuration;
	}
	
	
}
