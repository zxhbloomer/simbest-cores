package com.simbest.cores.admin.authority.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {
	private static final long serialVersionUID = -1373760761780840081L;
	public String loginName;
	public String userName;
	public String userCode;
	public Integer userId;
	public Integer orgId;
	public String orgName;
	public String headUrl;
	public String accesstoken;
	public String openid;
	public String unionid;
	public String phone;
    private String mpNum;
	public List<Integer> roleIds;

	public ShiroUser(String loginName, String userName, String userCode,
			Integer userId, Integer orgId, String orgName,
			List<Integer> roleIds, String headUrl, String accesstoken,
			String openid, String unionid, String phone, String mpNum) {
		super();
		this.loginName = loginName;
		this.userName = userName;
		this.userCode = userCode;
		this.userId = userId;
		this.orgId = orgId;
		this.orgName = orgName;
		this.roleIds = roleIds;
		this.headUrl = headUrl;
		this.accesstoken = accesstoken;
		this.openid = openid;
		this.unionid = unionid;
		this.phone = phone;
        this.mpNum = mpNum;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return userName;
	}
	
	public String toFullString(){
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @return the orgId
	 */
	public Integer getOrgId() {
		return orgId;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @return the roleIds
	 */
	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	/**
	 * @return the accesstoken
	 */
	public String getAccesstoken() {
		return accesstoken;
	}

	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}

	/**
	 * @return the unionid
	 */
	public String getUnionid() {
		return unionid;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param headUrl the headUrl to set
	 */
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

    public String getMpNum() {
        return mpNum;
    }

    /**
	 * 重载equals,只计算loginName;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ShiroUser other = (ShiroUser) obj;
		if (loginName == null) {
			if (other.loginName != null) {
				return false;
			}
		} else if (!loginName.equals(other.loginName)) {
			return false;
		}
		return true;
	}
}
