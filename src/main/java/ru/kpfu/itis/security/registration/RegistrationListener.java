package ru.kpfu.itis.security.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.exception.InternetAddressException;
import ru.kpfu.itis.model.entity.User;
import ru.kpfu.itis.service.MailService;
import ru.kpfu.itis.service.UserService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.UUID;

/**
 * Registration listener to save verification token and send an email to a user
 */

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationListener.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MailException {

        logger.error("[Try to send confirmation email ...]");

        User user = (User)event.getSource();

        //generating token
        String token = UUID.randomUUID().toString();

        userService.saveVerificationToken(user, token);

        String confirmUrl = event.getContext() + "/user/signup/confirm?token=" + token;

        String message = "You are successfully registered!\nConfirm your account -> ";

        String fromMail = env.getRequiredProperty("smtp.username");

        logger.error("[Sending email to `" + user.getEmail() + "` ...]");

        try {

            InternetAddress from = new InternetAddress(fromMail);
            InternetAddress to = new InternetAddress(user.getEmail());

            mailService.sendMail(from, to, message + env.getRequiredProperty("app.url") + confirmUrl);

        } catch (AddressException e) {

            logger.error("[InternetAddress is not correct]");
            throw new InternetAddressException("Email address is not correct", e);

        } catch (MailException e) {

            logger.error("[Can't send email from `" +  fromMail + "` to `" +  user.getEmail() + "`]", e);
        }

    }
}
