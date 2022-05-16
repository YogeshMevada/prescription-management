package com.prescription.management.config;

import com.prescription.commons.util.SecurityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public SecurityUtil securityUtil() {
        return new SecurityUtil();
    }
}
