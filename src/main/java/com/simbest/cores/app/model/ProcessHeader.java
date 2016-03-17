package com.simbest.cores.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.NotNullColumn;
import com.simbest.cores.utils.annotations.Unique;

/**
 * 业务流程头
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_header")
@XmlRootElement
public class ProcessHeader extends GenericModel<ProcessHeader> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8775430057069370476L;

	@Id
	@Column(name = "headerId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer headerId;

	@NotNullColumn(value="流程编码")
	@Column(name = "headerCode", unique=true, nullable = false, length = 50)
	@Unique
	private String headerCode;

	@NotNullColumn(value="流程描述")
	@Column(name = "headerDesc", nullable = false, length = 200)
	private String headerDesc;
    
	//流程分类
	@NotNullColumn(value="流程类型")
	@Column(name = "typeId", nullable = false)
    private Integer typeId;
	
	@OneToMany(mappedBy = "header")
	private List<ProcessStep> steps = new ArrayList<ProcessStep>();
	
	@Column(name = "enabled", nullable = false, columnDefinition = "TINYINT default 1")
	private Boolean enabled;
	
	@Column(name = "hversion", nullable = false)
    private Integer hversion;
	
    @Transient
    private String typeDesc; 
    
    public ProcessHeader() {
		super();
    }
    
	public ProcessHeader(Integer headerId) {
		super();
		this.headerId = headerId;
	}
	
	/**
	 * @return the headerId
	 */
	public Integer getHeaderId() {
		return headerId;
	}

	/**
	 * @param headerId the headerId to set
	 */
	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
	}

	/**
	 * @return the headerCode
	 */
	public String getHeaderCode() {
		return headerCode;
	}

	/**
	 * @param headerCode the headerCode to set
	 */
	public void setHeaderCode(String headerCode) {
		this.headerCode = headerCode;
	}

	/**
	 * @return the headerDesc
	 */
	public String getHeaderDesc() {
		return headerDesc;
	}

	/**
	 * @param headerDesc the headerDesc to set
	 */
	public void setHeaderDesc(String headerDesc) {
		this.headerDesc = headerDesc;
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

	/**
	 * @return the typeDesc
	 */
	public String getTypeDesc() {
		return typeDesc;
	}

	/**
	 * @param typeDesc the typeDesc to set
	 */
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	/**
	 * @return the steps
	 */
	public List<ProcessStep> getSteps() {
		return steps;
	}

	/**
	 * @param steps the steps to set
	 */
	public void setSteps(List<ProcessStep> steps) {
		this.steps = steps;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getHversion() {
		return hversion;
	}

	public void setHversion(Integer hversion) {
		this.hversion = hversion;
	}
	
}