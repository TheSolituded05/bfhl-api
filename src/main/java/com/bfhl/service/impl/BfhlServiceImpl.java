package com.bfhl.service.impl;

import com.bfhl.config.BfhlUserProperties;
import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.BfhlService;
import com.bfhl.util.BfhlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BfhlServiceImpl implements BfhlService {

    private final BfhlUserProperties userProperties;

    @Override
    public BfhlResponse processData(BfhlRequest request) {
        log.info("Processing BFHL request");

        if (request.getData() == null) {
            log.warn("Request data list is null");
            return BfhlResponse.builder()
                    .isSuccess(false)
                    .build();
        }

        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        BigInteger sum = BigInteger.ZERO;

        for (String item : request.getData()) {
            if (item == null) {
                continue;
            }
            if (BfhlUtil.isNumeric(item)) {
                BigInteger val = new BigInteger(item);
                sum = sum.add(val);
                if (val.remainder(BigInteger.valueOf(2)).signum() == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (BfhlUtil.isAlphabetic(item)) {
                alphabets.add(item.toUpperCase());
            } else {
                specialCharacters.add(item);
            }
        }

        // Generate concat string from upper-cased alphabets sequence
        String concatString = BfhlUtil.generateConcatString(alphabets);

        // Standard user_id formulation: <name>_<dob> in lowercase
        String userId = String.format("%s_%s",
                userProperties.getName().toLowerCase(),
                userProperties.getDob());

        BfhlResponse response = BfhlResponse.builder()
                .isSuccess(true)
                .userId(userId)
                .email(userProperties.getEmail())
                .rollNumber(userProperties.getRollNumber())
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialCharacters)
                .sum(sum.toString())
                .concatString(concatString)
                .build();

        log.info("Successfully processed request for user: {}", userId);
        return response;
    }
}
