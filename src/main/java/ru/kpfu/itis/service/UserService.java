package ru.kpfu.itis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.exception.EmailExistsException;
import ru.kpfu.itis.exception.UserRegistrationException;
import ru.kpfu.itis.model.entity.RegisterVerificationToken;
import ru.kpfu.itis.model.entity.User;
import ru.kpfu.itis.model.entity.UserAuthority;
import ru.kpfu.itis.model.form.UserFrom;
import ru.kpfu.itis.repository.RegisterTokensRepository;
import ru.kpfu.itis.repository.UserAuthorityRepository;
import ru.kpfu.itis.repository.UserRepository;
import ru.kpfu.itis.security.registration.OnRegistrationCompleteEvent;

import javax.validation.constraints.NotNull;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterTokensRepository registerTokenRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public User register(
        @NotNull UserFrom userFrom,
        String context
    ) throws UserRegistrationException {

        logger.error("[Try to register the user ...]");

        if (isEmailExists(userFrom.getEmail())) {
            logger.error("[Email is already exists]");
            throw new EmailExistsException("Email is already exists");
        }

        User user = new User(
                userFrom.getFirstName(),
                userFrom.getLastName(),
                userFrom.getEmail(),
                passwordEncoder.encode(userFrom.getPassword())
        );

        //publish onRegistration event
        publishOnRegistrationEvent(user, context);

        //add user's role (default ROLE_USER)
        UserAuthority role = userAuthorityRepository.findByRole("ROLE_USER");
        user.addRole(role);

        //save user
        return userRepository.save(user);
    }


    public boolean isEmailExists(String email) {
        return userRepository.findByEmailIgnoreCase(email) != null;
    }


    public void updateUser(User user) {
        userRepository.save(user);
    }


    public User findUserByVerificationToken(String token) {

        RegisterVerificationToken verificationToken = registerTokenRepository.findByToken(token);

        if (verificationToken != null) return verificationToken.getUser();
        return null;
    }


    public RegisterVerificationToken getVerificationToken(String token) {
        return registerTokenRepository.findByToken(token);
    }


    public RegisterVerificationToken saveVerificationToken(User user, String token) {

        logger.error("[Saving verification token ...]");

        RegisterVerificationToken myToken = new RegisterVerificationToken(token, user);

        return registerTokenRepository.save(myToken);
    }


    private void publishOnRegistrationEvent(User user, String context) {

        logger.error("[Publishing onRegistration event ...]");

        ApplicationEvent event = new OnRegistrationCompleteEvent(user, context);

        applicationEventPublisher.publishEvent(event);
    }

}
