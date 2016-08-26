package ru.kpfu.itis.controller;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.exception.InternetAddressException;
import ru.kpfu.itis.exception.UserRegistrationException;
import ru.kpfu.itis.model.entity.RegisterVerificationToken;
import ru.kpfu.itis.model.entity.User;
import ru.kpfu.itis.model.form.UserFrom;
import ru.kpfu.itis.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(value = "/user/signup")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.GET)
    public String showRegisterPage(
        ModelMap model,
        Principal principal
    ) {
        logger.error("[showRegisterPage controller]");

        if (principal == null) {
            logger.error("[principal is null, user not authenticated]");
            model.addAttribute("user", new UserFrom());
            return "signup";
        }

        logger.error("[redirect to : /home]");
        return "redirect:/home";
    }


    @RequestMapping(value = "/confirm/next")
    public String showAfterRegistrationPage(@RequestParam String status, ModelMap model) {
        model.addAttribute("message","Confirm your account, now=)");
        return "afterRegistration";
    }


    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirmRegistration(
        @RequestParam(value = "token", required = false) String token,
        RedirectAttributes redirectAttributes,
        ModelMap model
    ) {
        logger.error("[Confirm registration controller]");

        RegisterVerificationToken verificationToken = userService.getVerificationToken(token);

        if (verificationToken == null) {

            logger.error("[Token is invalid]");

            model.addAttribute("message", "Invalid token!");

            return "badUser";
        }

        User user = verificationToken.getUser();

        if ( verificationToken.getExpired().isBefore( new LocalDateTime() )) {

            logger.error("Verification token [" + verificationToken.getToken() + "] was expired!");

            model.addAttribute("message", "Your auth token was expired!");

            return "badUser";
        }

        user.setEnabled(true);

        userService.updateUser(user);

        redirectAttributes.addFlashAttribute("message","You are successfully activate your account\n " +
                "now enter your email and password. Good luck=)");

        return "redirect:/";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String registerUserAccount(
        @ModelAttribute("user") @Valid UserFrom user,
        BindingResult result,
        WebRequest request
    ) {

        if (result.hasErrors()) {
            logger.error("[Found errors in user]");
            return "signup";
        }

        userService.register(user, request);

        return "redirect:/user/signup/confirm/next?status=waiting_for_confirmation";
    }


    @ExceptionHandler({UserRegistrationException.class, MailException.class})
    public ModelAndView userRegistrationException(Exception ex) {

        logger.error("[Catch exception `" + ex.getClass() + "`]");

        ModelAndView modelAndView = new ModelAndView();

        if (ex instanceof InternetAddressException || ex instanceof UserRegistrationException) {

            modelAndView.setViewName("signup");
            modelAndView.addObject("exception", ex.getMessage());

        } else {

            modelAndView.setViewName("signup");
            modelAndView.addObject("exception", "Can't send an email! \n Please, check your email!");
        }

        return modelAndView;
    }

}
