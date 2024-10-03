package com.test.devteria.devteria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.devteria.devteria.dto.request.UserCreationRequest;
import com.test.devteria.devteria.dto.response.UserRespone;
import com.test.devteria.devteria.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
// CREATE MOCK REQUEST TO CONTROLLER
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // INPUT OF TESTCASE
    private UserCreationRequest request;
    // OUTPUT OF TESTCASE
    private UserRespone respone;
    private LocalDate dob;

    // THIS ANNOTATION WILL START BEFOR @TEST
    @BeforeEach
    void initData() {
        dob = LocalDate.of(2000, 5, 5);
        request = UserCreationRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        respone = UserRespone.builder()
                .id("7eab3d21055c")
                .username("john")
//                .firstName("John")
//                .lastName("Doe")
//                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // CONVERT STRING TO JSON
        String content = objectMapper.writeValueAsString(request);

        // RETURN RESULT DIRECTLY NOT NEED MENTION TO FUNCTION createUser IN USERSERVICE
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(respone);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("7eab3d21055c"));
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN
        request.setUsername("duy");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // CONVERT STRING TO JSON
        String content = objectMapper.writeValueAsString(request);

        // RETURN RESULT DIRECTLY NOT NEED MENTION TO FUNCTION createUser IN USERSERVICE
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(respone);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // THEN
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 4 characters"));
    }
}
