package com.simbest.cores.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.simbest.cores.utils.annotations.ProcessProperty;

@MappedSuperclass
public abstract class LogicModel<T> extends SystemModel<T>{
	
	@Column(name = "enabled", nullable = false, columnDefinition = "TINYINT default 1")
	@ProcessProperty
	protected Boolean enabled;
	
	@Column(name = "removed", nullable = false, columnDefinition = "TINYINT default 0")
	@ProcessProperty
	protected Boolean removed;
	
	@Column(name = "createUserId", nullable = false)
	@ProcessProperty
	protected Integer createUserId;	
	
	@Column(name = "createUserCode", nullable = true, length = 50)
	@ProcessProperty
	protected String createUserCode;	
	
	@Column(name = "createUserName", nullable = false, length = 50)
	@ProcessProperty
	protected String createUserName; 

	@Column(name = "updateUserId")
	@ProcessProperty
	protected Integer updateUserId;	
	
	@Column(name = "updateUserCode", length = 50)
	@ProcessProperty
	protected String updateUserCode;	
	
	@Column(name = "updateUserName", length = 50)
	@ProcessProperty
	protected String updateUserName; 

	/**
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return the removed
	 */
	public Boolean getRemoved() {
		return removed;
	}
	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}
	/**
	 * @return the createUserCode
	 */
	public String getCreateUserCode() {
		return createUserCode;
	}
	/**
	 * @param createUserCode the createUserCode to set
	 */
	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}
	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}
	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * @return the updateUserCode
	 */
	public String getUpdateUserCode() {
		return updateUserCode;
	}
	/**
	 * @param updateUserCode the updateUserCode to set
	 */
	public void setUpdateUserCode(String updateUserCode) {
		this.updateUserCode = updateUserCode;
	}
	/**
	 * @return the updateUserName
	 */
	public String getUpdateUserName() {
		return updateUserName;
	}
	/**
	 * @param updateUserName the updateUserName to set
	 */
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	/**
	 * @return the createUserId
	 */
	public Integer getCreateUserId() {
		return createUserId;
	}
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * @return the updateUserId
	 */
	public Integer getUpdateUserId() {
		return updateUserId;
	}
	/**
	 * @param updateUserId the updateUserId to set
	 */
	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	/**
	 * 必须确保enabled和removed有值
	 * @return
	 */
	public boolean validate(){
		return this!=null && enabled!=null && enabled && removed!=null && !removed;
	}
}
