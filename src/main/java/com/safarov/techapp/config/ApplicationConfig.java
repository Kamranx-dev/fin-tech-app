package com.safarov.techapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(getClass().getName());
    }
}
