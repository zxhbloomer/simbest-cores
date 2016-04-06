package com.simbest.cores.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {

    @RequestMapping("action/welcome")
    public ModelAndView welcome() {
    	ModelAndView mav = new ModelAndView("welcome");
        return mav;
    }
}
