package ru.kpfu.itis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.kpfu.itis.exception.user.UserRegistrationException;
import ru.kpfu.itis.model.entity.User;
import ru.kpfu.itis.model.form.UserFrom;
import ru.kpfu.itis.service.EmailVerifierService;
import ru.kpfu.itis.service.MailService;
import ru.kpfu.itis.service.UserAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/user/signup")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private EmailVerifierService emailVerifierService;


    @RequestMapping(method = RequestMethod.GET)
    public String showSignup(ModelMap model, Principal principal) {

        logger.error("[/user/signup/ request]");

        if (principal == null) {

            logger.error("[principal is null, user not authenticated]");

            model.addAttribute("user", new UserFrom());

            return "security/signup";
        }

        logger.error("[User is already authenticated, redirect to : /home]");

        return "redirect:/home";
    }



    @RequestMapping(method = RequestMethod.POST)
    public String registerAccount(
            @ModelAttribute("user") @Valid UserFrom user,
            BindingResult result,
            HttpServletRequest request
    ) {

        if (result.hasErrors()) {

            logger.error("[User is not valid]");

            return "security/signup";
        }

        //verify email (api check)
        if (! emailVerifierService.isValid(user.getEmail())) {
            throw new UserRegistrationException("Email doesn't exist. Please, enter real email address!");
        }

        //generate token
        String token = UUID.randomUUID().toString();

        //send confirmation email
        Map<String, Object> params = new HashMap<>();
        params.put("user", new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()));
        params.put("link", getUrl(request) + token);

        mailService.sendConfirmAccountMessage(params);

        //save user
        User registered = userAccountService.register(user);

        //save token
        userAccountService.saveVerificationToken(registered, token);

        return "redirect:/user/account/confirm";
    }



    //TODO utility method for building path
    private String getUrl(HttpServletRequest request) {

        return "http://localhost:8080/user/account/confirm?token=";
    }



    @ExceptionHandler(UserRegistrationException.class)
    public ModelAndView userRegistrationExceptionHandler(Exception ex) {

        logger.error("[Catch exception `" + ex.getClass() + "`]");

        ModelAndView modelAndView = new ModelAndView("security/signup");

        //to make available registering user/ user form binding
        modelAndView.addObject("user", new UserFrom());

        modelAndView.addObject("exception", ex.getMessage());

        return modelAndView;
    }



}
