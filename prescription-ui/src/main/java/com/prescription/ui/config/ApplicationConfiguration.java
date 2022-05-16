package com.prescription.ui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prescription.commons.util.SecurityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.TimeUnit;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Client client() {
        return ClientBuilder.newBuilder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().failOnUnknownProperties(false).build();
    }

    @Bean
    public SecurityUtil securityUtil() {
        return new SecurityUtil();
    }
}
