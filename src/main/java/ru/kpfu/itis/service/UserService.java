package ru.kpfu.itis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.exception.UserRegistrationException;
import ru.kpfu.itis.model.entity.User;
import ru.kpfu.itis.model.entity.UserAuthority;
import ru.kpfu.itis.model.form.UserFrom;
import ru.kpfu.itis.repository.UserAuthorityRepository;
import ru.kpfu.itis.repository.UserRepository;

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


    /**
     * Register user account
     *
     * @param userFrom - registering user data transfer object
     * @return registered user
     * @throws UserRegistrationException - if E-Mail Address is already registered
     */
    public User register(@NotNull UserFrom userFrom) throws UserRegistrationException {

        if (isEmailExists(userFrom.getEmail())) {
            //TODO local resolver and i18n
            throw new UserRegistrationException("Warning: E-Mail Address is already registered!" +
                    " Please login at the login page.");
        }

        User user = new User();

        user.setEmail(userFrom.getEmail());
        user.setFirstName(userFrom.getFirstName());
        user.setLastName(userFrom.getLastName());

        String encodedPassword = passwordEncoder.encode(userFrom.getPassword());

        user.setPassword(encodedPassword);

        UserAuthority role = userAuthorityRepository.findByRole("ROLE_USER");

        user.addRole(role);

        return userRepository.save(user);
    }


    public boolean isEmailExists(String email) {
        return userRepository.findByEmailIgnoreCase(email) != null;
    }

}
