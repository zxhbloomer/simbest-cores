package com.simbest.cores.admin.authority.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.googlecode.cqengine.resultset.ResultSet;
import com.simbest.cores.admin.authority.cache.SysUserCache;
import com.simbest.cores.admin.authority.model.*;
import com.simbest.cores.admin.authority.service.*;
import com.simbest.cores.cache.cqengine.index.SysUserIndex;
import com.simbest.cores.cache.cqengine.search.SysUserSearch;
import com.simbest.cores.service.impl.LogicAdvanceService;
import com.simbest.cores.shiro.AppUserSession;
import com.simbest.cores.utils.Constants;
import com.simbest.cores.utils.SmsUtils;
import com.simbest.cores.utils.StringUtil;
import com.simbest.cores.utils.configs.CoreConfig;
import com.simbest.cores.utils.enums.SNSLoginType;
import com.simbest.cores.web.filter.SNSAuthenticationToken;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 考虑用户数量庞大，只对默认的id和loginName进行容器启动时初始化缓存，userCode、openid、unionid则使用Spring Cache在运行期间加载
 * 
 * @author lishuyi
 *
 */
@Service(value="sysUserAdvanceService")
public class SysUserAdvanceService extends LogicAdvanceService<SysUser,Integer> implements ISysUserAdvanceService{
	
	private ISysUserService sysUserService;
	
	private SysUserCache sysUserCache;

	@Autowired
	private ISysOrgAdvanceService sysOrgAdvanceService;

	@Autowired
	private ISysRoleAdvanceService sysRoleAdvanceService;
	
	@Autowired
	private ISysPermissionAdvanceService sysPermissionAdvanceService;
	
	@Autowired
	private CoreConfig config;
	
	@Autowired
	private SmsUtils smsUtil;

	@Autowired
	private AppUserSession appUserSession;

    @Autowired
    private SysUserSearch sysUserSearch;

	@Autowired
	private RedisTemplate<String, Map<String, Object>> redisTemplate;

	private BoundHashOperations<String, Integer, Map<String, Object>> usersTreeDataHolder = null;
	private BoundHashOperations<String, String, Map<String, Object>> usersRoleTreeDataHolder = null;
	private BoundHashOperations<String, Integer, Map<String, Object>> permissionsTreeDataHolder = null;
    private BoundHashOperations<String, String, List<DynamicUserTreeNode>> choseDynamicUserTreeDataHolder = null;
    private BoundHashOperations<String, String, List<DynamicUserTreeNode>> searchDynamicUserTreeDataHolder = null;

	@Autowired
	public SysUserAdvanceService(@Qualifier(value="sysUserService")ISysUserService sysUserService, SysUserCache sysUserCache) {
		super(sysUserService, sysUserCache);
		this.sysUserService = sysUserService;		
		this.sysUserCache = sysUserCache;
	}
	
    @PostConstruct
	public void init(){	
		usersTreeDataHolder = redisTemplate.boundHashOps("usersTreeDataHolder");
		usersRoleTreeDataHolder = redisTemplate.boundHashOps("usersRoleTreeDataHolder");
		permissionsTreeDataHolder = redisTemplate.boundHashOps("permissionsTreeDataHolder");
        choseDynamicUserTreeDataHolder = redisTemplate.boundHashOps("choseDynamicUserTreeDataHolder");
        searchDynamicUserTreeDataHolder = redisTemplate.boundHashOps("searchDynamicUserTreeDataHolder");
	}

	@Override
	public SysUser getByUserCode(String userCode) {	
		return sysUserService.getByUserCode(userCode);
	}

	@Override
	public SysUser getByUnionid(String mpNum, String unionid) {
		return sysUserService.getByUnionid(mpNum, unionid);
	}

	@Override
	public SysUser getByOpenid(String openid) {
		return loadByCustom("openid", openid);		
	}
	
	@Override
	public SysUser getByAccesstoken(String accesstoken){
		return loadByCustom("accesstoken", accesstoken);
	}

	@Override
	public List<SysUser> getByRole(Integer roleId) {
		return sysUserService.getByRole(roleId);
	}

	@Override
	public List<SysUser> getByOrg(Integer orgId, Integer userType) {
		return sysUserService.getByOrg(orgId, userType);
	}

	@Override
	public List<SysUser> getByIterateOrg(Integer orgId, Integer userType) {
		return sysUserService.getByIterateOrg(orgId, userType);
	}

    /**
     * 一. 穷举遍历递归部门用户树
     * 递归层层向下查询子部门，直到所有部门加载完成。查询部门时，把该部门所有用户也作为叶子节点加载
     * @param userType
     * @return
     */
	@Override
	public Map<String, Object> getUsersTreeData(Integer userType) {
		Map<String, Object> data = usersTreeDataHolder.get(userType);
		if(data == null){
			data = getUsersTree(userType);
			usersTreeDataHolder.put(userType, data);
		}
		return data;
	}
	
	private Map<String, Object> getUsersTree(Integer userType) {
		SysOrg root = sysOrgAdvanceService.getRoot();
		Map<String, Object> rootNode = Maps.newHashMap();
		rootNode.put("key", null); //选择的是用户，因此组织不需要ID
		rootNode.put("title", root.getOrgName()); // 设置根结点文本内容
		rootNode.put("nodetype", "org");
		rootNode.put("select", false); // 设置根结点选中情况
		rootNode.put("expand", true); // 设置根结点叶子结点开闭状态
		rootNode.put("isFolder", true);
		rootNode.put("cssselect", ""); 
		List<Map<String, Object>> sysUserSysOrgNodes = Lists.newArrayList();
		sysUserSysOrgNodes.addAll(querySysOrgUser(root, userType)); //获取部门下属用户
		sysUserSysOrgNodes.addAll(querySysOrgChildren(root, userType));
	    rootNode.put("children", sysUserSysOrgNodes); // 递归设置根结点子节点
		return rootNode;
	}
	
	/**
	 * 递归层层 向下 查询子部门，直到所有部门加载完成。查询部门时，把该部门所有用户也作为叶子节点加载
	 * @param parent
	 * @param roleId
	 * @return
	 */
	private List<Map<String, Object>> querySysOrgChildren(SysOrg parent, Integer userType) {
		List<Map<String, Object>> childrenNodes = Lists.newArrayList();
		List<SysOrg> children = sysOrgAdvanceService.getByParent(parent.getId());
		for (SysOrg child : children) {
			Map<String, Object> childNodeMap = Maps.newHashMap();
			Collection<Map<String, Object>> childChildren = querySysOrgChildren(child, userType); //查询下级组织
			childNodeMap.put("key", null); //选择的是用户，因此组织不需要ID
			childNodeMap.put("title", child.getOrgName());
			childNodeMap.put("nodetype", "org");
			childNodeMap.put("select", false); // 组织永远不让选中
			childNodeMap.put("expand", false); //本组织节点默认关闭closed
			childNodeMap.put("isFolder", true);
			childNodeMap.put("cssselect", ""); 
			//if (childChildren != null && childChildren.size() > 0) { //一旦加上该判断，将导致如果该部门没有儿子部门，那么该部门员工无法获取
				List<Map<String, Object>> sysUserSysOrgNodes = Lists.newLinkedList();
				List<Map<String, Object>> sysOrgUsers = querySysOrgUser(child,userType);
				sysUserSysOrgNodes.addAll(sysOrgUsers);   //加入本组织下用户
				sysUserSysOrgNodes.addAll(childChildren); //加入本组织下级组织
				childNodeMap.put("children", sysUserSysOrgNodes);
				for(Map<String, Object> checkUser : sysOrgUsers){ //如果本组织下用户拥有传入的角色，那么组织节点打开open
					if((boolean) checkUser.get("select")){
						childNodeMap.put("expand", true);
						break;
					}
				}
			//}
			childrenNodes.add(childNodeMap);
		}
		return childrenNodes;
	}
	
	/**
	 * 获取部门下属用户
	 * @param sysOrg
	 * @param roleId
	 * @return
	 */
	private List<Map<String, Object>> querySysOrgUser(SysOrg sysOrg, Integer userType){
		List<Map<String, Object>> sysUserNodes = Lists.newArrayList();
		List<SysUser> children = sysUserService.getByOrg(sysOrg.getId(), userType);
		for (SysUser child : children) {
			Map<String, Object> childNodeMap = Maps.newHashMap();
			childNodeMap.put("key", child.getId());
			childNodeMap.put("title", child.getUsername());
			childNodeMap.put("nodetype", "user");
			childNodeMap.put("expand", true); //用户节点默认打开open
			childNodeMap.put("select", false);
			childNodeMap.put("isFolder", false);
			childNodeMap.put("cssselect", ""); 
			sysUserNodes.add(childNodeMap);
		}
		return sysUserNodes;
	}

    /**
     * 二. 穷举遍历递归部门用户树[需要关联角色]
     *  递归层层向下查询子部门，直到所有部门加载完成。查询部门时，把该部门所有用户也作为叶子节点加载，并显示用户角色
     * @param roleId
     * @param userType
     * @return
     */
	@Override
	public Map<String, Object> getUsersRoleTreeData(Integer roleId, Integer userType) {
		Map<String, Object> data = usersRoleTreeDataHolder.get(roleId+Constants.UNDERLINE+userType);
		if(data == null){
			data = getUsersRoleTree(roleId,userType);
			usersRoleTreeDataHolder.put(roleId+Constants.UNDERLINE+userType, data);
		}
		return data;
	}
	
	private Map<String, Object> getUsersRoleTree(Integer roleId, Integer userType) {
		SysOrg root = sysOrgAdvanceService.getRoot();
		Map<String, Object> rootNode = Maps.newHashMap();
		rootNode.put("key", null); //选择的是用户，因此组织不需要ID
		rootNode.put("title", root.getOrgName()); // 设置根结点文本内容
		rootNode.put("nodetype", "org");
		rootNode.put("select", false); // 设置根结点选中情况
		rootNode.put("expand", true); // 设置根结点叶子结点开闭状态
		rootNode.put("isFolder", true);
		rootNode.put("cssselect", ""); 
		List<Map<String, Object>> sysUserSysOrgNodes = Lists.newArrayList();
		sysUserSysOrgNodes.addAll(querySysOrgUserByRole(root, roleId, userType)); //获取部门下属用户
		sysUserSysOrgNodes.addAll(querySysOrgChildren(root,  roleId, userType));
	    rootNode.put("children", sysUserSysOrgNodes); // 递归设置根结点子节点
		return rootNode;
	}
	
	/**
	 * 递归层层向下查询子部门，直到所有部门加载完成。查询部门时，把该部门所有用户也作为叶子节点加载
	 * @param parent
	 * @param roleId
	 * @return
	 */
	private List<Map<String, Object>> querySysOrgChildren(SysOrg parent, Integer roleId, Integer userType) {
		List<Map<String, Object>> childrenNodes = Lists.newArrayList();
		List<SysOrg> children = sysOrgAdvanceService.getByParent(parent.getId());
		for (SysOrg child : children) {
			Map<String, Object> childNodeMap = Maps.newHashMap();
			Collection<Map<String, Object>> childChildren = querySysOrgChildren(child, roleId, userType); //查询下级组织
			childNodeMap.put("key", null); //选择的是用户，因此组织不需要ID
			childNodeMap.put("title", child.getOrgName());
			childNodeMap.put("nodetype", "org");
			childNodeMap.put("select", false); // 组织永远不让选中
			childNodeMap.put("expand", false); //本组织节点默认关闭closed
			childNodeMap.put("isFolder", true);
			childNodeMap.put("cssselect", ""); 
			//if (childChildren != null && childChildren.size() > 0) { //一旦加上该判断，将导致如果该部门没有儿子部门，那么该部门员工无法获取
				List<Map<String, Object>> sysUserSysOrgNodes = Lists.newArrayList();
				List<Map<String, Object>> sysOrgUsers = querySysOrgUserByRole(child,roleId,userType);
				sysUserSysOrgNodes.addAll(sysOrgUsers);   //加入本组织下用户
				sysUserSysOrgNodes.addAll(childChildren); //加入本组织下级组织
				childNodeMap.put("children", sysUserSysOrgNodes);
				for(Map<String, Object> checkUser : sysOrgUsers){ //如果本组织下用户拥有传入的角色，那么组织节点打开open
					if((boolean) checkUser.get("select")){
						childNodeMap.put("expand", true);
						break;
					}
				}
			//}
			childrenNodes.add(childNodeMap);
		}
		return childrenNodes;
	}

	/**
	 * 获取部门下属用户(显示该用户是否拥有制定角色)
	 * @param sysOrg
	 * @param roleId
	 * @return
	 */
	private List<Map<String, Object>> querySysOrgUserByRole(SysOrg sysOrg, Integer roleId, Integer userType){
		List<Integer> checkUserIds = Lists.newArrayList();
		List<SysUser> roleUsers = sysUserService.getByRole(roleId);
		for(SysUser u : roleUsers){
			checkUserIds.add(u.getId());
		}
		List<Map<String, Object>> sysUserNodes = Lists.newArrayList();
		List<SysUser> children = sysUserService.getByOrg(sysOrg.getId(), userType);
		for (SysUser child : children) {
			Map<String, Object> childNodeMap = Maps.newHashMap();
			childNodeMap.put("key", child.getId());
			childNodeMap.put("title", child.getUsername());
			childNodeMap.put("nodetype", "user");
			childNodeMap.put("expand", true); //用户节点默认打开open
			childNodeMap.put("select", checkUserIds.contains(child.getId()));
			childNodeMap.put("isFolder", false);
			childNodeMap.put("cssselect", checkUserIds.contains(child.getId())?"select":""); 
			sysUserNodes.add(childNodeMap);
		}
		return sysUserNodes;
	}

    /**
     * 三. 穷举遍历用户权限树，用于对用户直接授权
     * @param userId
     * @return
     */
	@Override
	public Map<String, Object> getUserPermissionsTreeData(Integer userId) {
		Map<String, Object> data = permissionsTreeDataHolder.get(userId);
		if(data == null){
			data = getPermissionsTree(userId);
			permissionsTreeDataHolder.put(userId, data);
		}
		return data;
	}
	
	/**
	 * 递归层层向下查询用户权限
	 * @param userId
	 * @return
	 */
	private Map<String, Object> getPermissionsTree(Integer userId) {		
		SysPermission root = sysPermissionAdvanceService.getRoot();
		List<SysPermission> checkedSysPermissions = sysPermissionAdvanceService.getSysUserPermission(userId);
		Map<String, Object> rootNode = Maps.newHashMap();
		rootNode.put("key", root.getId()); // 设置根结点id
		rootNode.put("title", root.getDescription()); // 设置根结点文本内容
		rootNode.put("select", true); // 设置根结点选中情况
		rootNode.put("expand", true); // 设置根结点叶子结点开闭状态
		rootNode.put("isFolder", true);
		rootNode.put("cssselect", "select"); 
	    rootNode.put("children", queryChildren(root, checkedSysPermissions)); // 递归设置根结点子节点
		return rootNode;
	}
	
	private List<Map<String, Object>> queryChildren(SysPermission parent, List<SysPermission> checkedSysPermissions) {
		List<Integer> checkPermissionIds = Lists.newArrayList();
		for(SysPermission p : checkedSysPermissions){
			checkPermissionIds.add(p.getId());	
		}		
		List<Map<String, Object>> childrenNodes = Lists.newLinkedList();
		List<SysPermission> children = sysPermissionAdvanceService.getByParent(parent.getId());
		for (SysPermission child : children) {
			Map<String, Object> childNodeMap = Maps.newHashMap();
			Boolean isChecked = false;
			if (checkPermissionIds != null && checkPermissionIds.contains(child.getId())) {
				isChecked = true;
			}
			List<Map<String, Object>> childChildren = queryChildren(child, checkedSysPermissions);
			childNodeMap.put("key", child.getId());
			childNodeMap.put("title", child.getDescription());
			if (childChildren != null && childChildren.size() > 0) {
				childNodeMap.put("children", childChildren);
			}
			childNodeMap.put("select", isChecked);
			childNodeMap.put("cssselect", isChecked?"select":""); 
			childNodeMap.put("expand", isChecked ? true:false);
			childNodeMap.put("isFolder", child.getType().equals("button") ? false:true);
			childrenNodes.add(childNodeMap);
		}
		return childrenNodes;
	}


    /**
     * 四. 根据所选组织，动态自定义构建组织成员及下级组织的树形菜单（含首节点）
     * @param orgId
     * @param userType
     * @return
     */
    @Override
    public List<DynamicUserTreeNode> getFirstDynamicUserTree(Integer orgId, Integer userType){
        List<DynamicUserTreeNode> resultList = Lists.newArrayList();
        SysOrg root = sysOrgAdvanceService.loadByKey(orgId);
        DynamicUserTreeNode rootNode = new DynamicUserTreeNode();
        rootNode.setType("org");
        rootNode.setChild(true);
        rootNode.setId(root.getId());
        rootNode.setPid(root.getParent()==null?null:root.getParent().getId());
        rootNode.setTitle(root.getOrgName());
        resultList.add(rootNode);
        return loadUserAndOrg(resultList, orgId, userType);
    }

    /**
     * 四. 根据所选组织，动态自定义构建组织成员及下级组织的树形菜单
     * @param orgId
     * @param userType
     * @return
     */
    @Override
    public List<DynamicUserTreeNode> getChoseDynamicUserTree(Integer orgId, Integer userType) {
        List<DynamicUserTreeNode> data = choseDynamicUserTreeDataHolder.get(orgId+Constants.UNDERLINE+userType);
        if(data == null){
            data = loadUserAndOrg(Lists.<DynamicUserTreeNode>newArrayList(), orgId, userType);
            choseDynamicUserTreeDataHolder.put(orgId+Constants.UNDERLINE+userType, data);
        }
        return data;
    }

    private List<DynamicUserTreeNode> loadUserAndOrg(List<DynamicUserTreeNode> resultList, Integer orgId, Integer userType){
        List<SysUser> userList = getByOrg(orgId,userType);
        for(SysUser user : userList){
            DynamicUserTreeNode node = new DynamicUserTreeNode();
            node.setType("user");
            node.setChild(false);
            node.setId(user.getId());
            node.setPid(orgId);
            node.setTitle(user.getUsername());
            resultList.add(node);
        }
        List<SysOrg> orgList = sysOrgAdvanceService.getByParent(orgId);
        for(SysOrg org : orgList){
            DynamicUserTreeNode node = new DynamicUserTreeNode();
            Integer children = sysOrgAdvanceService.countByParent(org.getId());
            if(children != null && children>0)
                node.setChild(true);
            else
                node.setChild(false);
            node.setType("org");
            node.setId(org.getId());
            node.setPid(orgId);
            node.setTitle(org.getOrgName());
            resultList.add(node);
        }
        return resultList;
    }

    /**
     * 五.根据CQEngine所建立的索引字段搜索，并构建树
     * @param loginName
     * @param mpNum
     * @param position
     * @return
     */
    @Override
    public List<DynamicUserTreeNode> searchDynamicUserTree(String loginName, Integer ownerOrgId, String position) {
        List<DynamicUserTreeNode> data = searchDynamicUserTreeDataHolder.get(loginName+Constants.UNDERLINE+ownerOrgId+Constants.UNDERLINE+position);
        if(data == null){
            if(Boolean.valueOf(config.getValue("app.enable.cqengine"))) {
                data = loadDynamicUserTreeByCQEngine(loginName, ownerOrgId, position);
            }else{
                data = loadDynamicUserTreeByDatabase(loginName, ownerOrgId, position);
            }
            searchDynamicUserTreeDataHolder.put(loginName+Constants.UNDERLINE+ownerOrgId+Constants.UNDERLINE+position, data);
        }
        return data;
    }

    private List<DynamicUserTreeNode> loadDynamicUserTreeByDatabase(String loginName, Integer ownerOrgId, String position){
        List<DynamicUserTreeNode> resultList = Lists.newArrayList();
        Set<SysOrg> unduplicatedOrgSet = Sets.newHashSet();
        SysUser params = new SysUser();
        params.setLoginName(loginName);
        params.setOwnerOrgId(ownerOrgId);
        params.setPosition(position);
        Collection<SysUser> list = getAll(params);
        for (SysUser user : list) {
            DynamicUserTreeNode userNode = new DynamicUserTreeNode();
            userNode.setType("user");
            userNode.setChild(false);
            userNode.setId(user.getId());
            userNode.setPid(user.getSysOrg().getId());
            userNode.setTitle(user.getUsername());
            unduplicatedOrgSet.addAll(sysOrgAdvanceService.getHierarchyOrgs(user.getSysOrg().getId()));
            resultList.add(userNode);
        }
        for(SysOrg org : unduplicatedOrgSet){
            DynamicUserTreeNode orgNode = new DynamicUserTreeNode();
            Integer children = sysOrgAdvanceService.countByParent(org.getId());
            if(children != null && children>0)
                orgNode.setChild(true);
            else
                orgNode.setChild(false);
            orgNode.setType("org");
            orgNode.setId(org.getId());
            if(null != org.getParent())
                orgNode.setPid(org.getParent().getId());
            orgNode.setTitle(org.getOrgName());
            resultList.add(orgNode);
        }
        return resultList;
    }

    private List<DynamicUserTreeNode> loadDynamicUserTreeByCQEngine(String loginName, Integer ownerOrgId, String position){
        List<DynamicUserTreeNode> resultList = Lists.newArrayList();
        Set<SysOrg> unduplicatedOrgSet = Sets.newHashSet();
        ResultSet<SysUserIndex> list = sysUserSearch.searchQuery(loginName,ownerOrgId,position);
        for (SysUserIndex user : list) {
            DynamicUserTreeNode userNode = new DynamicUserTreeNode();
            userNode.setType("user");
            userNode.setChild(false);
            userNode.setId(user.getSysUser().getId());
            userNode.setPid(user.getSysUser().getSysOrg().getId());
            userNode.setTitle(user.getSysUser().getUsername());
            unduplicatedOrgSet.addAll(user.getHierarchyOrgs());
            resultList.add(userNode);
        }
        for(SysOrg org : unduplicatedOrgSet){
            DynamicUserTreeNode orgNode = new DynamicUserTreeNode();
            Integer children = sysOrgAdvanceService.countByParent(org.getId());
            if(children != null && children>0)
                orgNode.setChild(true);
            else
                orgNode.setChild(false);
            orgNode.setType("org");
            orgNode.setId(org.getId());
            if(null != org.getParent())
                orgNode.setPid(org.getParent().getId());
            orgNode.setTitle(org.getOrgName());
            resultList.add(orgNode);
        }
        return resultList;
    }

	@Override
	public int updatePassword(SysUser u) {
		return sysUserService.updatePassword(u);
	}	

	@Override
	public int updateGroupRemark(Integer id, Integer groupid, String remark) {
		return sysUserService.updateGroupRemark(id, groupid, remark);
	}

	@Override
	public int createOrUpdateViaAdmin(SysUser u) {
		int ret = sysUserService.createOrUpdateViaAdmin(u);
		saveOrUpdate(u);
		log.debug(ret);
		return ret;
	}
	
	@Override
	public int createViaAdmin(SysUser u) {
		int ret = sysUserService.createViaAdmin(u);
		saveOrUpdate(u);
		log.debug(ret);
		return ret;
	}

	@Override
	public int updateViaAdmin(SysUser u){
		int ret = sysUserService.updateViaAdmin(u);
		saveOrUpdate(u);
		log.debug(ret);
		return ret;
	}
	
	@Override
	public int deleteUserByAdmin(SysUser u) {
		int ret = sysUserService.deleteUserByAdmin(u);
		removeValue(u.getId());
		return ret;
	}
	
	@Override
	public int forceDelete(Integer userId) {
		int ret = sysUserService.forceDelete(userId);
		log.debug("forceDelete userId:"+userId);
		removeValue(userId);
		return ret;
	}

	/**
	 * 前端用户知道openid，不知道phone
	 */
	@Override
	public Map<String, Object> updateBindFrontendUser(String phone, String expectCode) {
		Map<String, Object> map = Maps.newHashMap();	
		SysUser sysUser = null;
		boolean check = smsUtil.checkUserBindCode(phone, expectCode);
		if(check){
			int ret = 0;
			Map<String,Object> params = Maps.newHashMap();
			params.put("phone", phone);
			params.put("userType", Integer.valueOf(config.getValue("app.usertype.frontend")));
			Collection<SysUser> users = sysUserService.getAll(params);
			ShiroUser shiroUser = appUserSession.getCurrentUser();
			if(users.isEmpty()){ //用户没有用App注册过				
				SysUser weChatUser = sysUserService.getByOpenid(shiroUser.getOpenid());
				weChatUser.setPhone(phone);
				weChatUser.setRemoved(false);
				weChatUser.setEnabled(true);		
				sysUser = weChatUser;
			}else {//用户曾使用App注册过(以原App用户信息为准)
				SysUser appUser = users.iterator().next();
				log.debug(appUser);
				SysUser weChatUser = getByOpenid(shiroUser.getOpenid());
				log.debug(weChatUser);
				if(weChatUser != null){
					BeanUtils.copyProperties(weChatUser, appUser, new String[]{"id", "phone", "accesstoken", "groupid", "sysOrg"});
					ret = forceDelete(weChatUser.getId()); //物理删除微信用户，否则openid不能唯一
					log.debug(ret);
				}
				sysUser = appUser;
			}
			sysUser.setUsername(StringUtil.filterEmoji(sysUser.getUsername()));
			sysUser.setNickname(StringUtil.filterEmoji(sysUser.getNickname()));
			sysUser.setSignature(StringUtil.filterEmoji(sysUser.getSignature()));
			ret = updateViaAdmin(sysUser);
			log.debug(ret);
			SNSAuthenticationToken snsToken = new SNSAuthenticationToken(sysUser.getOpenid(), SNSLoginType.weixin);
			SecurityUtils.getSubject().login(snsToken);	
			map.put("message", "绑定成功!");
			map.put("responseid", 1);			
		}else{
			map.put("message", "短信验证错误!");
			map.put("responseid", 0);
		}
		return map;
	}

	/**
	 * 后端端用户知道phone，不知道openid(不自动写入DB)
	 */
	@Override
	public Map<String, Object> updateBindBackendUser(SysUser weChatUser, String phone, String expectCode) {
		Map<String, Object> map = Maps.newHashMap();	
		boolean check = smsUtil.checkUserBindCode(phone, expectCode);
		if(check){
			int ret = 0;
			Map<String,Object> params = Maps.newHashMap();
			params.put("phone", phone);
			params.put("userType", Integer.valueOf(config.getValue("app.usertype.backend")));
			Collection<SysUser> users = sysUserService.queryAnyway(params);
			if(users.isEmpty()){ //用户没有关联过俱乐部
				map.put("message", "手机号未与俱乐部关联!");
				map.put("responseid", 0);
			}else{
				SysUser backendUser = users.iterator().next();
				BeanUtils.copyProperties(weChatUser, backendUser, new String[]{"id", "phone", "accesstoken", "groupid", "sysOrg"});
				backendUser.setPhone(phone);
				backendUser.setRemoved(false);
				backendUser.setEnabled(true);
				backendUser.setUsername(StringUtil.filterEmoji(backendUser.getUsername()));
				backendUser.setNickname(StringUtil.filterEmoji(backendUser.getNickname()));
				backendUser.setSignature(StringUtil.filterEmoji(backendUser.getSignature()));
				ret = updateViaAdmin(backendUser);				
				log.debug(ret);
				if(ret > 0){
					SNSAuthenticationToken snsToken = new SNSAuthenticationToken(backendUser.getOpenid(), SNSLoginType.weixin);
					SecurityUtils.getSubject().login(snsToken);	
					map.put("message", "绑定成功!");
					map.put("responseid", 1);
				}else{
					map.put("message", "更新后台账号失败!");
					map.put("responseid", 0);
				}
			}
		}else{
			map.put("message", "短信验证错误!");
			map.put("responseid", 0);
		}
		return map;
	}



	
}
