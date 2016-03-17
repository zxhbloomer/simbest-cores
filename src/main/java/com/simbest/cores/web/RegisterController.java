package com.simbest.cores.web;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.admin.authority.service.ISysUserService;

@Controller
@RequestMapping(value = "/action/register")
public class RegisterController {

	@Resource(name = "sysUserService")
	private ISysUserService sysUserService;

	@RequestMapping(method = RequestMethod.GET)
	public String registerForm() {
		return "account/register";
	}

	@RequestMapping(method = { RequestMethod.POST })
	public RedirectView register(@Valid SysUser sysUser, RedirectAttributes redirectAttributes) {
		sysUserService.create(sysUser);
		redirectAttributes.addFlashAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, sysUser.getLoginName());
		return new RedirectView("/login", true, false, false);
		//return "redirect:/login";
	}

	/**
	 * Ajax请求校验loginName是否唯一。
	 */
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam("loginName") String loginName) {
		if (sysUserService.getByUnique(loginName) == null) {
			return "true";
		} else {
			return "false";
		}
	}
}