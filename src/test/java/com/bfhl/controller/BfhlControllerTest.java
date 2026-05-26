package com.bfhl.controller;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BfhlController.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BfhlService bfhlService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testProcessData_Success() throws Exception {
        BfhlResponse response = BfhlResponse.builder()
                .isSuccess(true)
                .userId("ajinkya_sukhtankar_05022005")
                .email("ajinkyasukhtankar230619@acropolis.in")
                .rollNumber("0827CS231023")
                .oddNumbers(Arrays.asList("1"))
                .evenNumbers(Arrays.asList("334", "4"))
                .alphabets(Arrays.asList("A", "R"))
                .specialCharacters(Arrays.asList("$"))
                .sum("339")
                .concatString("Ra")
                .build();

        when(bfhlService.processData(any(BfhlRequest.class))).thenReturn(response);

        BfhlRequest request = BfhlRequest.builder()
                .data(Arrays.asList("a", "1", "334", "4", "R", "$"))
                .build();

        mockMvc.perform(post("/bfhl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").value("ajinkya_sukhtankar_05022005"))
                .andExpect(jsonPath("$.email").value("ajinkyasukhtankar230619@acropolis.in"))
                .andExpect(jsonPath("$.roll_number").value("0827CS231023"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    @Test
    void testProcessData_NullDataField_BadRequest() throws Exception {
        BfhlRequest request = BfhlRequest.builder()
                .data(null)
                .build();

        mockMvc.perform(post("/bfhl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testProcessData_MalformedJson_BadRequest() throws Exception {
        String malformedJson = "{\"data\": [\"a\", \"b\"";

        mockMvc.perform(post("/bfhl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false))
                .andExpect(jsonPath("$.error").value("Malformed JSON request body"));
    }
}
