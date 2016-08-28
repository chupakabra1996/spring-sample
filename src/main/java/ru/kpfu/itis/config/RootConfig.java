package ru.kpfu.itis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import ru.kpfu.itis.config.hibernate.HibernateConfig;
import ru.kpfu.itis.config.security.SecurityConfig;

import java.util.Properties;

@Configuration
@Import({ HibernateConfig.class, SecurityConfig.class })
@ComponentScan(basePackages = {"ru.kpfu.itis.service"})

@PropertySource(value = {"classpath:/api/email_verification.properties",
        "classpath:/application.properties", "classpath:/smtp/gmail_smtp.properties"})

public class RootConfig {

    @Autowired
    private Environment env;

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
    public FreeMarkerConfigurer freeMarkerConfigurer() {

        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();

        configurer.setDefaultEncoding("UTF-8");

        configurer.setTemplateLoaderPath("/WEB-INF/mail/freemarker");

        return configurer;
    }
}
