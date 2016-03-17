package com.simbest.cores.app.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simbest.cores.utils.enums.ProcessEnum;

/**
 * Json数据提交模型
 * 
 * @author lishuyi
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT) 
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessJsonData<T extends ProcessModel<?>, PK extends Serializable> {
	private ProcessEnum result;
	private String opinion;
	private T businessData;

	public ProcessJsonData() {
		super();
	}

	public ProcessJsonData(ProcessEnum result, T businessData) {
		super();
		this.result = result;
		this.businessData = businessData;
	}

	public ProcessJsonData(ProcessEnum result, String opinion, T businessData) {
		super();
		this.result = result;
		this.opinion = opinion;
		this.businessData = businessData;
	}

	/**
	 * @return the result
	 */
	public ProcessEnum getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ProcessEnum result) {
		this.result = result;
	}

	/**
	 * @return the opinion
	 */
	public String getOpinion() {
		return opinion;
	}

	/**
	 * @param opinion
	 *            the opinion to set
	 */
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	/**
	 * @return the businessData
	 */
	public T getBusinessData() {
		return businessData;
	}

	/**
	 * @param businessData
	 *            the businessData to set
	 */
	public void setBusinessData(T businessData) {
		this.businessData = businessData;
	}

}