package com.simbest.cores.admin.authority.service;
import java.util.Map;

import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.service.IGenericAdvanceService;

public interface ISysUserAdvanceService extends IGenericAdvanceService<SysUser,Integer>, ISysUserService{
	/**
	 * 前端用户知道openid，不知道phone
	 * @param phone
	 * @param expectCode
	 * @return
	 */
	Map<String, Object> updateBindFrontendUser(String phone, String expectCode);
	
	/**
	 * 后端端用户知道phone，不知道openid(不自动写入DB)
	 * @param weChatUser
	 * @param phone
	 * @param expectCode
	 * @return
	 */
	Map<String, Object> updateBindBackendUser(SysUser weChatUser, String phone, String expectCode);
	
	/**
	 * 递归层层 向下 查询子部门，直到所有部门加载完成。查询部门时，把该部门所有用户也作为叶子节点加载
	 * @return
	 */
	Map<String, Object> getUsersTreeData(Integer userType);
	
	/**
	 * 递归层层 向下 查询子部门，直到所有部门加载完成。查询部门时，把该部门所有用户也作为叶子节点加载，并显示用户角色
	 * @param roleId
	 * @return
	 */
	Map<String, Object> getUsersRoleTreeData(Integer roleId, Integer userType);
	
	/**
	 * 递归层层 向下 查询用户权限
	 * @param userId
	 * @return
	 */
	Map<String, Object> getUserPermissionsTreeData(Integer userId);
}
