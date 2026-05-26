package com.bfhl.service;

import com.bfhl.config.BfhlUserProperties;
import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.impl.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BfhlServiceImplTest {

    private BfhlServiceImpl bfhlService;
    private BfhlUserProperties userProperties;

    @BeforeEach
    void setUp() {
        userProperties = new BfhlUserProperties();
        userProperties.setName("ajinkya_sukhtankar");
        userProperties.setDob("05022005");
        userProperties.setEmail("ajinkyasukhtankar230619@acropolis.in");
        userProperties.setRollNumber("0827CS231023");

        bfhlService = new BfhlServiceImpl(userProperties);
    }

    @Test
    void testProcessData_MixedInput_Success() {
        BfhlRequest request = BfhlRequest.builder()
                .data(Arrays.asList("a", "1", "334", "4", "R", "$"))
                .build();

        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertEquals("ajinkya_sukhtankar_05022005", response.getUserId());
        assertEquals("ajinkyasukhtankar230619@acropolis.in", response.getEmail());
        assertEquals("0827CS231023", response.getRollNumber());
        assertEquals(Arrays.asList("1"), response.getOddNumbers());
        assertEquals(Arrays.asList("334", "4"), response.getEvenNumbers());
        assertEquals(Arrays.asList("A", "R"), response.getAlphabets());
        assertEquals(Arrays.asList("$"), response.getSpecialCharacters());
        assertEquals("339", response.getSum());
        assertEquals("Ra", response.getConcatString());
    }

    @Test
    void testProcessData_EmptyInput_Success() {
        BfhlRequest request = BfhlRequest.builder()
                .data(Collections.emptyList())
                .build();

        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    void testProcessData_OnlyNumbers_Success() {
        BfhlRequest request = BfhlRequest.builder()
                .data(Arrays.asList("2", "-4", "13", "0"))
                .build();

        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(Arrays.asList("13"), response.getOddNumbers());
        assertEquals(Arrays.asList("2", "-4", "0"), response.getEvenNumbers());
        assertTrue(response.getAlphabets().isEmpty());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("11", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    void testProcessData_OnlyAlphabets_Success() {
        BfhlRequest request = BfhlRequest.builder()
                .data(Arrays.asList("a", "ABCD", "DOE"))
                .build();

        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertEquals(Arrays.asList("A", "ABCD", "DOE"), response.getAlphabets());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("0", response.getSum());
        // Alphabets: A, ABCD, DOE -> Letters: AABCDDOE -> Reversed: EODDCBAA -> Alternating: EoDdCbAa
        assertEquals("EoDdCbAa", response.getConcatString());
    }

    @Test
    void testProcessData_OnlySpecialCharacters_Success() {
        BfhlRequest request = BfhlRequest.builder()
                .data(Arrays.asList("$", "@", "#", "*"))
                .build();

        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        assertEquals(Arrays.asList("$", "@", "#", "*"), response.getSpecialCharacters());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    void testProcessData_LargeNumbers_Success() {
        BfhlRequest request = BfhlRequest.builder()
                .data(Arrays.asList("999999999999999999", "1", "2"))
                .build();

        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertEquals("1000000000000000002", response.getSum());
    }

    @Test
    void testProcessData_NullHandling_Success() {
        BfhlRequest request = BfhlRequest.builder()
                .data(Arrays.asList(null, "a", null, "2", null))
                .build();

        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(Arrays.asList("2"), response.getEvenNumbers());
        assertEquals(Arrays.asList("A"), response.getAlphabets());
        assertEquals("2", response.getSum());
        assertEquals("A", response.getConcatString());
    }
}
