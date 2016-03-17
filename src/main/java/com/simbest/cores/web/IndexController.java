package com.simbest.cores.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = {"/action/sso", //SSO跳转，Shrio不拦截
	"/action"}) //后台管理跳转，Shrio拦截校验权限
public class IndexController {

    @RequestMapping("/")
    public ModelAndView index() {
    	ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/welcome")
    public ModelAndView welcome() {
    	ModelAndView mav = new ModelAndView("welcome");
        return mav;
    }
}
