package com.simbest.cores.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/action/redirect")
public class RedirectController {

	/**
	 * 用户跳转JSP页面
	 * 
	 * 此方法不考虑权限控制
	 * 
	 * @param pageName JSP名称(不加后缀)
	 * @return 指定JSP页面
	 */
	@RequestMapping("/{pageName}")
	public String redirectView(@PathVariable String pageName) {
		return pageName;
	}
	
	/**
	 * 用户跳转JSP页面
	 * 
	 * 此方法不考虑权限控制
	 * 
	 * @param folder 路径
	 * @param pageName JSP名称(不加后缀)
	 * @return 指定JSP页面
	 * 
	 * 例如用法：webapp/portal/index.jsp 中调用连接  ${ctx}/redirect/portal/about'
	 */
	@RequestMapping("/{folder}/{pageName}")
	public String redirectJsp(@PathVariable String folder, @PathVariable String pageName) {		
		return folder + "/" + pageName;
	}
}
