package ru.kpfu.itis.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthenticationFailureHandler custom implementation
 * When authentication failed show user appropriate reason
 * NOTE : User shouldn't know exact information i.e. password is not correct
 * because of  vulnerability for brute-force etc.
 */

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        logger.error("[Authentication failure exception : `" + exception.getClass() + "`]");

        String message = exception.getMessage();

        if (exception instanceof DisabledException || exception instanceof BadCredentialsException) {

            message = "Wrong email or password";

        } else if (exception instanceof LockedException) {

            message = "You're locked, baby!";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {

            logger.error("[Auth != null]");

            if (auth.getPrincipal() != null) {
                logger.error("[Auth getPrincipal != null]");
            }

            logger.error("[Auth name = " + auth.getName() + "]");
        }

        //get the session, don't create if it doesn't exist
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, message);
        }

        response.sendRedirect("login?error");
    }
}
