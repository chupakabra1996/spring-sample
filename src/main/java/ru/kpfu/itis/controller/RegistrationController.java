package ru.kpfu.itis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/signup")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String showRegister(Principal principal) {
        if (principal == null) return "signup";
        return "redirect:/homepage";
    }
}
