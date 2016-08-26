package ru.kpfu.itis.security.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.model.entity.User;
import ru.kpfu.itis.service.MailService;
import ru.kpfu.itis.service.UserService;

import java.util.UUID;

/**
 * TODO documentation
 */

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationListener.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    private MessageSourceAccessor messages;

    @Autowired
    public void setMessages(MessageSource messageSource) {
        messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {

        logger.error("");

        User user = event.getUser();

        String token = UUID.randomUUID().toString();

        logger.error("Generated token - [" + token + "]");
        userService.saveVerificationToken(user, token);

        String confirmationUrl = event.getUrl() + "/registrationConfirm?token=" + token; //TODO url check
        logger.error("confirmation url is  - " + confirmationUrl);

//        String message = messages.getMessage("Registration.user.success", "Registration success", event.getLocale());
        String message = "Hello=)";

        logger.error("Sending email ...");

        mailService.sendMail(message + " rn" + "http://localhost:8080" + confirmationUrl);

    }
}
