package com.prescription.management.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.AssertionErrors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerIT extends AbstractIT {

    @Test
    void login() throws Exception {
        final MockHttpServletResponse httpServletResponse = mvc.perform(get("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ""))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        AssertionErrors.assertNotNull("", httpServletResponse);
    }
}