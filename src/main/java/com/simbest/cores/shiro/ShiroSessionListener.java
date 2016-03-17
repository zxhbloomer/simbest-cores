/**
 * 
 */
package com.simbest.cores.shiro;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.dao.DataAccessException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simbest.cores.admin.syslog.model.SysLoginInfo;
import com.simbest.cores.admin.syslog.model.SysLoginInfoExample;
import com.simbest.cores.admin.syslog.model.SysLoginInfoExample.Criteria;
import com.simbest.cores.admin.syslog.service.ISysLoginInfoService;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.utils.configs.CoreConfig;

/**
 * Session 到期退出，记录用户登出信息
 * 
 * @author lishuyi
 * 
 */
@Component
public class ShiroSessionListener extends SessionListenerAdapter {
	public final Log log = LogFactory.getLog(ShiroSessionListener.class);
	
	@Resource(name = "sysLoginInfoService")
	private ISysLoginInfoService sysLoginInfoService;
	
	private Boolean record;
	
	@Autowired
	private void initRecord(CoreConfig coreConfig){
		record = Boolean.valueOf(coreConfig.getValue("app.record.log"));
	}
	
	@Override
	public void onExpiration(Session session) {
		if(record){
			SysLoginInfoExample ex = new SysLoginInfoExample();
			Criteria c = ex.createCriteria();
			c.andSessionidEqualTo(session.getId().toString());			
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
	}

}
