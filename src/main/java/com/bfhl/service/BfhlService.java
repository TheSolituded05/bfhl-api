package com.bfhl.service;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;

public interface BfhlService {
    /**
     * Processes raw input data list and compiles numbers, alphabets, special characters, sums, and concatenated strings.
     *
     * @param request the request payload containing the list of data strings
     * @return the processed response containing categorized data and metadata
     */
    BfhlResponse processData(BfhlRequest request);
}
