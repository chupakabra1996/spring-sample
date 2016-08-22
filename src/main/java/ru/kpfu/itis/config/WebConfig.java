package ru.kpfu.itis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
@ComponentScan(basePackages = {"ru.kpfu.itis.controller"})

@EnableWebMvc //Enable web mvc configuration by spring
public class WebConfig extends WebMvcConfigurerAdapter { //extending it to customize web mvc configuration


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {

        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();

        configurer.setTemplateLoaderPath("/WEB-INF/freemarker/");

        configurer.setDefaultEncoding("UTF-8");

        return configurer;
    }

    @Bean
    public ViewResolver viewResolver() {

        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();

        resolver.setCache(true);

        resolver.setPrefix("");

        resolver.setSuffix(".ftl");

        resolver.setContentType("text/html; charset=UTF-8");

        return resolver;
    }

}
