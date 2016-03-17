package com.simbest.cores.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.NotNullColumn;
import com.simbest.cores.utils.annotations.Unique;

/**
 * 业务流程分类
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "app_process_type")
@XmlRootElement
public class ProcessType extends GenericModel<ProcessType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -13798030722354856L;

	@Id
	@Column(name = "typeId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer typeId;

	@NotNullColumn(value="流程类型编码")
	@Column(name = "typeCode", unique=true, nullable = false, length = 20)
	@Unique
	private String typeCode;

	@NotNullColumn(value="流程类型描述")
	@Column(name = "typeDesc", nullable = false, length = 200)
	private String typeDesc;

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

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
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
	
}