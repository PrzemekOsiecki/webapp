package com.example.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.backend.persistence.repositories")
//@EntityScan(basePackages = "com.example.backend.persistence.domain.backend")
@ComponentScan(basePackages = "com.example.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("file:///${user.home}/.webapp/application-common.properties")
public class ApplicationConfiguration {
}
