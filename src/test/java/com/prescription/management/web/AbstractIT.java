package com.prescription.management.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prescription.management.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ComponentScan
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "test")
public abstract class AbstractIT {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected UserRepository userRepository;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    public String toJsonObject(final Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    public <T> T toObject(final String data, final Class<T> clazz) throws Exception {
        return objectMapper.readValue(data, clazz);
    }

    public <T> T toObject(final String data, final TypeReference<T> typeReference) throws Exception {
        return objectMapper.readValue(data, typeReference);
    }
}
