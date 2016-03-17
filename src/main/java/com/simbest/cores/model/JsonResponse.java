/**
 * 
 */
package com.simbest.cores.model;

/**
 * @author Li
 *
 */
public class JsonResponse {
	private Integer responseid;
	private String message;
	private Object data;

	public Integer getResponseid() {
		return responseid;
	}

	public void setResponseid(Integer responseid) {
		this.responseid = responseid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
