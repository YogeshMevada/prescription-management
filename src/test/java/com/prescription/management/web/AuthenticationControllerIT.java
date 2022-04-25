package com.prescription.management.web;

import com.prescription.management.dto.response.AuthenticationResponse;
import com.prescription.management.entities.Users;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.AssertionErrors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerIT extends AbstractIT {

    @Test
    void authenticate() throws Exception {
        final Users user = new Users();
        user.setEmail("yogesh.mevada");
        user.setContact("9876543210");
        user.setPassword("admin");
        userRepository.save(user);

        final MockHttpServletResponse httpServletResponse = mvc.perform(post("/api/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        AssertionErrors.assertNotNull("", httpServletResponse);
        final String contentAsString = httpServletResponse.getContentAsString();
        AssertionErrors.assertNotNull("", contentAsString);
        final AuthenticationResponse authenticationResponse = toObject(contentAsString, AuthenticationResponse.class);
        AssertionErrors.assertNotNull("", authenticationResponse.getToken());
    }
}