package ru.kpfu.itis.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import ru.kpfu.itis.config.hibernate.HibernateConfig;
import ru.kpfu.itis.config.security.SecurityConfig;

@Configuration
@Import({ HibernateConfig.class, SecurityConfig.class })
@ComponentScan(basePackages = {"ru.kpfu.itis.service"})
@PropertySource(value = {"classpath:/api/email_verification.properties", "classpath:/application.properties"})
public class RootConfig {

}
