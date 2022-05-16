package com.prescription.ui.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlHelper {

    public final String AUTH_URL;
    public final String GET_USER_URL;

    @Autowired
    public UrlHelper(@Value("${url.prescription.management.service.version}") final String managementServiceApiVersion,
                     @Value("${url.prescription.management.service}") final String managementService) {
        this.AUTH_URL = managementService.concat(managementServiceApiVersion).concat("/authenticate");
        this.GET_USER_URL = managementService.concat(managementServiceApiVersion).concat("/user");
    }
}
