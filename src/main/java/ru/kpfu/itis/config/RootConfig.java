package ru.kpfu.itis.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"ru.kpfu.itis.service", "ru.kpfu.itis.util"})
@Import(value = DatabaseConfig.class)

public class RootConfig {
}
