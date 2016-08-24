package ru.kpfu.itis.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = {"/", "/homepage"}, method = RequestMethod.GET)
    public String showWelcome(Principal principal, ModelMap model) {
        if (principal != null) model.addAttribute("user", principal.getName());
        else model.addAttribute("user", "Guest");

        return "index";
    }


}
