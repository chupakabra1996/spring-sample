package ru.kpfu.itis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.exception.UserRegistrationException;
import ru.kpfu.itis.model.form.UserFrom;
import ru.kpfu.itis.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/signup")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    private MessageSourceAccessor msa;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        msa = new MessageSourceAccessor(messageSource);
    }


    @RequestMapping(method = RequestMethod.GET)
    public String showRegisterPage(
            ModelMap model, Principal principal
    ) {

        if (principal == null) {
            model.addAttribute("user", new UserFrom());
            return "signup";
        }

        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("HC#showHomePage").build();
    }


    @RequestMapping(method = RequestMethod.POST)
    public String registerUserAccount(
            RedirectAttributes redirectAttributes,
            @ModelAttribute("user") @Valid UserFrom user,
            BindingResult result
    ) {

        if (!result.hasErrors()) {
            registerNewUserAccount(user, result);
        }

        if (result.hasErrors()) {
            return "signup";
        }

        redirectAttributes.addFlashAttribute("successRegister", "User " + user.getEmail() + " was successfully registered!");
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("LC#showLoginPage").build();
    }


    private void registerNewUserAccount(UserFrom userFrom, BindingResult result) {

        try {
            userService.register(userFrom);
        } catch (UserRegistrationException e) {
            result.rejectValue("email", "user.email.exists", "Warning: E-Mail Address is already registered!" +
                    "Please login at the login page.");
        }
    }

}
