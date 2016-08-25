package ru.kpfu.itis.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.security.Principal;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping(method = RequestMethod.GET)
    public String showLoginPage(Principal principal) {
        if (principal == null) return "login";
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("HC#showHomePage").build();
    }




}
