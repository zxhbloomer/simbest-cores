/**
 * 
 */
package com.simbest.cores.admin.authority.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.admin.authority.service.ISysUserService;
import com.simbest.cores.cache.impl.GenericCache;

/**
 * @author Li
 *
 */
@Component(value="sysUserCache")
public class SysUserCache extends GenericCache<SysUser,Integer>{

	@Autowired
	public SysUserCache(@Qualifier(value="sysUserService")ISysUserService sysUserService) {
		super(sysUserService);
		super.setClazz(SysUser.class);
		this.registerCustomkey("openid");
		this.registerCustomkey("accesstoken");
	}

}
