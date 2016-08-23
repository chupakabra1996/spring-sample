package ru.kpfu.itis.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = {"ru.kpfu.itis.repository"}) //scan jpa repos
@EnableTransactionManagement
@PropertySource(
        value = {"classpath:/database/database.properties", "classpath:/database/hibernate.properties"}
) //inject it to spring's Environment

public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driver"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.uri"));
        dataSource.setUsername(env.getRequiredProperty("db.user"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));

        return dataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {

        LocalContainerEntityManagerFactoryBean emFactoryBean = new LocalContainerEntityManagerFactoryBean();

        emFactoryBean.setDataSource(dataSource());
        emFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        emFactoryBean.setPackagesToScan(env.getRequiredProperty("entitymanager.packages.to.scan"));
        emFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emFactoryBean.setJpaProperties(getHibernateJpaProperties());

        return emFactoryBean;

    }


    private Properties getHibernateJpaProperties() {

        Properties properties = new Properties();

        properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.enable_lazy_load_no_trans", env.getRequiredProperty("hibernate.enable_lazy_load_no_trans"));

        return properties;
    }


    @Bean
    public JpaTransactionManager transactionManager() {

        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();

        jpaTransactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());

        return jpaTransactionManager;
    }

}
