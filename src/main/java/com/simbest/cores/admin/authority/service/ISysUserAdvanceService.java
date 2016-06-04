package com.simbest.cores.admin.authority.service;
import java.util.List;
import java.util.Map;

import com.simbest.cores.admin.authority.model.DynamicUserTreeNode;
import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.service.IGenericAdvanceService;

public interface ISysUserAdvanceService extends IGenericAdvanceService<SysUser,Integer>, ISysUserService{
    String getAllParentIdString(Integer orgId);

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
     * 一. 穷举遍历递归部门用户树
     * 递归层层向下查询子部门，直到所有部门加载完成。查询部门时，把该部门所有用户也作为叶子节点加载
     * @param userType
     * @return
     */
	Map<String, Object> getUsersTreeData(Integer userType);

    /**
     * 二. 穷举遍历递归部门用户树[需要关联角色]
     *  递归层层向下查询子部门，直到所有部门加载完成。查询部门时，把该部门所有用户也作为叶子节点加载，并显示用户角色
     * @param roleId
     * @param userType
     * @return
     */
	Map<String, Object> getUsersRoleTreeData(Integer roleId, Integer userType);

    /**
     * 三. 穷举遍历用户权限树，用于对用户直接授权
     * @param userId
     * @return
     */
	Map<String, Object> getUserPermissionsTreeData(Integer userId);

    /**
     * 四. 根据所选组织，动态自定义构建组织成员及下级组织的树形菜单（含首节点）
     * @param orgId
     * @param userType
     * @return
     */
    List<DynamicUserTreeNode> getFirstDynamicUserTree(Integer orgId, Integer userType);

    /**
     * 四. 根据所选组织，动态自定义构建组织成员及下级组织的树形菜单
     * @param orgId
     * @param userType
     * @return
     */
    List<DynamicUserTreeNode> getChoseDynamicUserTree(Integer orgId, Integer userType);
}
