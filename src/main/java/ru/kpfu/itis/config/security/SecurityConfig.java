package ru.kpfu.itis.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.kpfu.itis.security.authentication.CustomAuthenticationFailureHandler;
import ru.kpfu.itis.service.UserDetailsServiceImpl;

import java.util.Properties;

@EnableWebSecurity
public class SecurityConfig {


    /**
     * Login form based config
     */
    @Order(2)
    @Configuration
    @ComponentScan(value = {"ru.kpfu.itis.security"})
    @PropertySource("classpath:/smtp/gmail_smtp.properties")
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class FormLoginWebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private Environment env;

        @Autowired
        private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

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
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/", "/user/signup/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/home")
                    .failureHandler(customAuthenticationFailureHandler)
                .and()
                .logout()
                    .logoutSuccessUrl("/login?logout")
                    .logoutUrl("/perform_logout")
                    .permitAll()
                .and()
                .rememberMe()
                    .tokenValiditySeconds(86400)
                    .tokenRepository(persistentTokenRepository)
                .and()
                .csrf()
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/access_denied");

            http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);
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

            mailSender.setHost(env.getRequiredProperty("smtp.host"));
            mailSender.setPort(env.getRequiredProperty("smtp.port", Integer.class));
            mailSender.setUsername(env.getRequiredProperty("smtp.username"));
            mailSender.setPassword(env.getRequiredProperty("smtp.password"));

            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", env.getRequiredProperty("mail.smtp.auth"));
            props.setProperty("mail.smtp.starttls.enable", env.getRequiredProperty("mail.smtp.starttls.enable"));
            props.setProperty("mail.debug", env.getRequiredProperty("mail.debug"));

            mailSender.setJavaMailProperties(props);

            return mailSender;
        }

        @Bean
        public CharacterEncodingFilter characterEncodingFilter() {
            CharacterEncodingFilter filter = new CharacterEncodingFilter();
            filter.setEncoding("UTF-8");
            filter.setForceEncoding(true);
            return filter;
        }

    }


}
