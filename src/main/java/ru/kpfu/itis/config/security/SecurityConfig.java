package ru.kpfu.itis.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import ru.kpfu.itis.service.UserDetailsServiceImpl;

import java.util.Properties;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    /**
     * Login form based config
     */

    @Configuration
    @Order(2)
    public static class FormLoginWebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        @Qualifier("hibernatePersistentTokenRepository")
        private PersistentTokenRepository persistentTokenRepository;

        @Autowired
        public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
            auth.authenticationProvider(authenticationProvider());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                    .antMatchers("/", "/signup/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/home")
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                .and()
                .rememberMe()
                    .tokenValiditySeconds(86400)
                    .tokenRepository(persistentTokenRepository)
                .and()
                .csrf()
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/access_denied")
                 .and()
                 .sessionManagement()
                    .invalidSessionUrl("/invalidSession");


        }


        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return new UserDetailsServiceImpl();
        }


        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService());
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
        }

        @Bean
        public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
            return new PersistentTokenBasedRememberMeServices("remember-me", userDetailsService(), persistentTokenRepository);
        }

        @Bean
        public AuthenticationTrustResolver getAuthenticationTrustResolver() {
            return new AuthenticationTrustResolverImpl();
        }

        @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
            return new HttpSessionEventPublisher();
        }

        @Bean
        public JavaMailSender mailSender() {

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

            mailSender.setDefaultEncoding("UTF-8");

            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(587);
            mailSender.setUsername("ramilsafnab1996@gmail.com");
            mailSender.setPassword("498034226pz4t");

            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.debug", "true");

            mailSender.setJavaMailProperties(props);

            return mailSender;
        }

    }


}
