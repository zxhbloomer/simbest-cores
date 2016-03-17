package com.simbest.cores.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.dao.DataAccessException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.simbest.cores.admin.authority.model.ShiroUser;
import com.simbest.cores.admin.syslog.model.SysLoginInfo;
import com.simbest.cores.admin.syslog.model.SysLoginInfoExample;
import com.simbest.cores.admin.syslog.model.SysLoginInfoExample.Criteria;
import com.simbest.cores.admin.syslog.service.ISysLoginInfoService;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.utils.configs.CoreConfig;

/**
 * 记录用户登出信息
 * 
 * @author lishuyi
 */
@Controller
@RequestMapping(value = "/action/logout")
public class LogoutController {
	public final Log log = LogFactory.getLog(getClass());

	@Resource(name = "sysLoginInfoService")
	private ISysLoginInfoService sysLoginInfoService;
	
	private Boolean record;
	
	@Autowired
	private void initRecord(CoreConfig coreConfig){
		record = Boolean.valueOf(coreConfig.getValue("app.record.log"));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String doLogout() {
		Subject currentUser = SecurityUtils.getSubject();
		ShiroUser principal = (ShiroUser)currentUser.getPrincipal();
		if(principal != null){
			if(record){
				SysLoginInfoExample ex = new SysLoginInfoExample();
				Criteria c = ex.createCriteria();
				c.andSessionidEqualTo(currentUser.getSession().getId().toString());			
				List<SysLoginInfo> list = sysLoginInfoService.selectByExample(ex);
				if(list != null && list.size()==1){
					SysLoginInfo info = list.get(0);
					info.setLogouttime(new Date());
					try{
						sysLoginInfoService.updateByPrimaryKeySelective(info);
					}catch(DataAccessException e){
		    			log.error(Exceptions.getStackTraceAsString(e));
		    		}
				}
			}
			currentUser.logout();
		}
		return "redirect:/action/login";  
	}
}