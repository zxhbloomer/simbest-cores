/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.cores.cache.cqengine.index;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.simbest.cores.admin.authority.model.SysOrg;
import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.model.BaseObject;

import java.util.List;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-06-03  17:57
 *
 * 参考：
 * http://vishnu667.github.io/using-cqengine/
 * https://github.com/npgall/cqengine
 *
 */
public class SysUserIndex extends BaseObject<SysUserIndex> {
    private String loginName;
    private Integer ownerOrgId;
    private String position;
    private SysUser sysUser;
    private List<SysOrg> hierarchyOrgs;

    public SysUserIndex(String loginName, Integer ownerOrgId, String position, SysUser sysUser, List<SysOrg> hierarchyOrgs) {
        this.loginName = loginName;
        this.ownerOrgId = ownerOrgId;
        this.position = position;
        this.sysUser = sysUser;
        this.hierarchyOrgs = hierarchyOrgs;
    }

    public static final SimpleAttribute<SysUserIndex, String> LOGIN_NAME = new SimpleAttribute<SysUserIndex, String>("loginName") {
        public String getValue(SysUserIndex user, QueryOptions queryOptions) { return user.loginName; }
    };
    
    public static final SimpleAttribute<SysUserIndex, Integer> OWNER_ORG = new SimpleAttribute<SysUserIndex, Integer>("ownerOrgId") {
        public Integer getValue(SysUserIndex user, QueryOptions queryOptions) { return user.ownerOrgId; }
    };

    public static final SimpleAttribute<SysUserIndex, String> POSITION = new SimpleAttribute<SysUserIndex, String>("position") {
        public String getValue(SysUserIndex user, QueryOptions queryOptions) { return user.position; }
    };

    public String getLoginName() {
        return loginName;
    }

    public Integer getOwnerOrgId() {
        return ownerOrgId;
    }

    public String getPosition() {
        return position;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public List<SysOrg> getHierarchyOrgs() {
        return hierarchyOrgs;
    }
}
