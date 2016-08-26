package ru.kpfu.itis.controller;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.exception.UserRegistrationException;
import ru.kpfu.itis.model.entity.RegisterVerificationToken;
import ru.kpfu.itis.model.entity.User;
import ru.kpfu.itis.model.form.UserFrom;
import ru.kpfu.itis.security.registration.OnRegistrationCompleteEvent;
import ru.kpfu.itis.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Locale;

@Controller
@RequestMapping
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    private MessageSourceAccessor msa;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        msa = new MessageSourceAccessor(messageSource);
    }

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @RequestMapping(name = "/signup", method = RequestMethod.GET)
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
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("HC#showHomePage").build();
    }

    @RequestMapping(value = "/signup/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(
            WebRequest request,
            ModelMap model,
            @RequestParam("token") String token
    ) {

        logger.error("[Confirm registration controller]");

        Locale locale = request.getLocale();

        RegisterVerificationToken verificationToken = userService.getVerificationToken(token);

        logger.error("verification token found? : " + (verificationToken != null));

        if (verificationToken == null) {
            logger.error("[token is null]");
            String message = msa.getMessage("auth.message.invalidToken", "Invalid token", locale);
            model.addAttribute("message", message);
            return "badUser";
        }

        if (verificationToken.isVerified()) {
            logger.error("[redirect to /signup?verified=true]");
            return "redirect:/login?verified=true";
        }

        User user = verificationToken.getUser();

        LocalDateTime now = new LocalDateTime();

        if (!verificationToken.getExpired().isAfter(now)) {
            logger.error("Verification token [" + verificationToken.getToken() + "] was expired!");
            String messageValue = msa.getMessage("auth.message.expired", "", locale);
            model.addAttribute("message", messageValue);
            return "badUser";
        }

        user.setEnabled(true);
        verificationToken.setVerified(true);

        userService.saveRegisteredUser(user);

        return "redirect:/login?lang=" + request.getLocale().getLanguage();
    }


    @RequestMapping(method = RequestMethod.POST)
    public String registerUserAccount(
            RedirectAttributes redirectAttributes,
            @ModelAttribute("user") @Valid UserFrom user,
            BindingResult result,
            WebRequest request
    ) {
        User registered;

        if (!result.hasErrors()) {
            logger.error("[try to register new user account ...]");
            registered = registerNewUserAccount(user, result);
        } else {
            logger.error("[Found errors in user]");
            return "signup";
        }

        try {
            sendConfirmationMessage(request, registered);
        } catch (Exception e) {
            logger.error("Can't send email", e);
            redirectAttributes.addFlashAttribute("user", user);
            String redirectUrl = MvcUriComponentsBuilder.fromMappingName("RC#showRegisterError").arg(0, "error").build();
            return "redirect:" + redirectUrl.substring(0, redirectUrl.lastIndexOf('/'));
        }

        redirectAttributes.addFlashAttribute("successRegister", "User " + user.getEmail() + " was successfully registered!");

        String redirectUrl = MvcUriComponentsBuilder.fromMappingName("LC#showLoginPage").build();

        return "redirect:" + redirectUrl.substring(0, redirectUrl.lastIndexOf('/'));
    }

    private void sendConfirmationMessage(WebRequest request, User user) throws Exception {
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(request.getContextPath(), user, request.getLocale()));
    }


    private User registerNewUserAccount(UserFrom userFrom, BindingResult result) {

        try {
            return userService.register(userFrom);
        } catch (UserRegistrationException e) {
            result.rejectValue("email", "user.email.exists", "Warning: E-Mail Address is already registered!" +
                    "Please login at the login page.");
        }

        return null;
    }
}
