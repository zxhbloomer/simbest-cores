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
    private String mpNum;
    private String position;
    private SysUser sysUser;
    private SysOrg sysOrg;
    private List<SysOrg> parents;

    public SysUserIndex(String loginName, String mpNum, String position, SysUser sysUser, SysOrg sysOrg, List<SysOrg> parents) {
        this.loginName = loginName;
        this.mpNum = mpNum;
        this.position = position;
        this.sysUser = sysUser;
        this.sysOrg = sysOrg;
        this.parents = parents;
    }

    public static final SimpleAttribute<SysUserIndex, String> LOGIN_NAME = new SimpleAttribute<SysUserIndex, String>("loginName") {
        public String getValue(SysUserIndex user, QueryOptions queryOptions) { return user.loginName; }
    };
    
    public static final SimpleAttribute<SysUserIndex, String> MP_NAME = new SimpleAttribute<SysUserIndex, String>("mpNum") {
        public String getValue(SysUserIndex user, QueryOptions queryOptions) { return user.mpNum; }
    };

    public static final SimpleAttribute<SysUserIndex, String> POSITION = new SimpleAttribute<SysUserIndex, String>("position") {
        public String getValue(SysUserIndex user, QueryOptions queryOptions) { return user.position; }
    };

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMpNum() {
        return mpNum;
    }

    public void setMpNum(String mpNum) {
        this.mpNum = mpNum;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public SysOrg getSysOrg() {
        return sysOrg;
    }

    public void setSysOrg(SysOrg sysOrg) {
        this.sysOrg = sysOrg;
    }

    public List<SysOrg> getParents() {
        return parents;
    }

    public void setParents(List<SysOrg> parents) {
        this.parents = parents;
    }
}
