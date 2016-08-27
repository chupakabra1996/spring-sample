package ru.kpfu.itis.controller;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
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
import ru.kpfu.itis.security.registration.OnRegistrationCompleteEvent;
import ru.kpfu.itis.service.EmailVerifierService;
import ru.kpfu.itis.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(value = "/user/signup")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private EmailVerifierService emailVerifierService;




    @RequestMapping(method = RequestMethod.GET)
    public String showSignup( ModelMap model, Principal principal ) {

        logger.error("[/user/signup/ request]");

        if (principal == null) {

            logger.error("[principal is null, user not authenticated]");

            model.addAttribute("user", new UserFrom());

            return "security/signup";
        }

        logger.error("[User is already authenticated, redirect to : /home]");

        return "redirect:/home";
    }


    @RequestMapping(value = "/confirm/next", method = RequestMethod.GET)
    public String showAfterRegistration(ModelMap model) {

        logger.error("[/confirm/next request]");

        model.addAttribute("message","Confirm your account, now=)");

        return "security/afterRegistration";
    }


    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirmRegistration(
        @RequestParam(value = "token") String token,
        RedirectAttributes redirectAttributes,
        ModelMap model
    ) {
        logger.error("[/confirm request]");

        RegisterVerificationToken verificationToken = userService.getVerificationToken(token);

        if (verificationToken == null) {

            logger.error("[Token not found, it is invalid]");

            model.addAttribute("message", "Invalid token!");

            return "security/badUser";
        }

        User user = verificationToken.getUser();

        //if verification token has expired
        if ( verificationToken.getExpired().isBefore( new LocalDateTime() )) {

            logger.error("Verification token [" + verificationToken.getToken() + "] was expired");

            model.addAttribute("message", "Your auth token was expired\nResend token");

            return "security/badUser";
        }

        //activate user
        user.setEnabled(true);

        userService.updateUser(user);

        redirectAttributes.addFlashAttribute("message","You are successfully activate your account\n " +
                "now enter your email and password. Good luck=)");

        return "redirect:/login";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String registerAccount(
        @ModelAttribute("user") @Valid UserFrom user,
        BindingResult result,
        WebRequest request
    ) {

        if (result.hasErrors()) {

            logger.error("[User is not valid]");

            return "security/signup";
        }

        //verify email (api check)
        verifyEmail(user.getEmail());

        //save user
        User registered = userService.register(user);

        //activate email confirm sending
        publishOnRegistrationEvent(registered, request);

        return "redirect:/user/signup/confirm/next";
    }

    private void publishOnRegistrationEvent(User user, WebRequest request) {

        logger.error("[Publishing onRegistration event ...]");

        ApplicationEvent event = new OnRegistrationCompleteEvent(user, request.getContextPath());

        eventPublisher.publishEvent(event);
    }

    private void verifyEmail(String emailAddress) {

        logger.error("[Verifying user's email `" + emailAddress + "`]");

        if ( !emailVerifierService.check(emailAddress) ) {

            throw new UserRegistrationException("Email is not valid!");
        }
    }


    @ExceptionHandler({UserRegistrationException.class, MailException.class})
    public ModelAndView userRegistrationException(Exception ex) {

        logger.error("[Catch exception `" + ex.getClass() + "`]");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("security/signup");

        //to make available registering user/ user form binding
        modelAndView.addObject("user", new UserFrom());

        if (ex instanceof InternetAddressException || ex instanceof UserRegistrationException) {

            modelAndView.addObject("exception", ex.getMessage());

        } else {

            //occurred MailException
            modelAndView.addObject("exception", "Can't send an email! \n Please, check your email!");
        }

        return modelAndView;
    }

}
