package ru.kpfu.itis.controller;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.model.entity.RegisterVerificationToken;
import ru.kpfu.itis.model.entity.User;
import ru.kpfu.itis.model.form.ResendForm;
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
@RequestMapping("/user/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private EmailVerifierService emailVerifierService;

    @Autowired
    private MailService mailService;


    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirmRegistration(
            @RequestParam(required = false) String token,
            RedirectAttributes redirectAttributes,
            Principal principal,
            ModelMap model
    ) {

        logger.error("[/confirm request]");

        //check if user authenticated
        if (principal != null) return "redirect:/home";


        if (token == null) {

            model.addAttribute("message", "We've sent ypu an email. Confirm your account, now=)");

            return "security/confirmAccount";
        }


        //get the token
        RegisterVerificationToken verificationToken = userAccountService.getVerificationToken(token);

        if (verificationToken == null) {

            logger.error("[Token not found]");

            return "redirect:/login";
        }

        //get the token's owner
        User user = verificationToken.getUser();

        //if verification token has expired
        if (verificationToken.getExpired().isBefore(new LocalDateTime())) {

            logger.error("Verification token [" + verificationToken.getToken() + "] was expired");

            redirectAttributes.addAttribute("message", "Your token has expired!");

            return "redirect:/user/account/resend";
        }

        //activate user
        user.setEnabled(true);
        userAccountService.updateUser(user);

        //delete token
        userAccountService.deleteVerifiedToken(verificationToken);

        redirectAttributes.addFlashAttribute("message", "You are successfully activate your account\n " +
                "now enter your email and password. Good luck=)");

        return "redirect:/login";
    }


    @RequestMapping(value = "/resend")
    public String showResendToken(ModelMap model, Principal principal) {

        logger.error("[Resend request]");

        if (principal != null) return "redirect:/home";

        model.addAttribute("resendForm", new ResendForm());

        return "security/resendToken";
    }



    @RequestMapping(value = "/resendToken", method = RequestMethod.POST)
    public String resendActivationToken(
            @ModelAttribute("resendForm") @Valid ResendForm resendForm,
            BindingResult bindingResult,
            HttpServletRequest request,
            ModelMap model
    ) {

        logger.error("[resendToken email : `" + resendForm + "`]");

        if (bindingResult.hasErrors()) {

            logger.error("[Binding error]");

            model.addAttribute("message", "Email address is not valid");

            return "security/resendToken";
        }

        User user = userAccountService.findUserByEmail(resendForm.getEmail());

        if ( isUserNullOrEnabled(user) ) {

            logger.error("[User doesn't exist or is already enabled]");

            return "redirect:/user/account/confirm";
        }


        if ( !emailVerifierService.isValid(resendForm.getEmail()) ) {

            logger.error("[Email is not valid , api check]");

            model.addAttribute("message", "Please, enter real email address!");

            return "security/resendToken";
        }

        sendNewToken(user, request);

        return "redirect:/user/account/confirm";
    }


    private void sendNewToken(User user, HttpServletRequest request) {

        //TODO mailService send email (prepared mimeMessage)
        //generate new token
        String token = UUID.randomUUID().toString();

        String confirmUrl = "http://localhost:8080/user/account/confirm?token=" + token;

        userAccountService.saveVerificationToken(user, token);

        Map<String, Object> params = new HashMap<>();

        params.put("user", user);

        params.put("link", confirmUrl);

        mailService.sendConfirmAccountMessage(params);
    }

    private boolean isUserNullOrEnabled(User user) {

        return (user == null || user.isEnabled());
    }
}
