package ru.kpfu.itis.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping
public class HomeController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String showHomePage(Principal principal, ModelMap model) {

        model.addAttribute("user", principal.getName());

        return "home";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showIndex() {
        return "index";
    }
}
