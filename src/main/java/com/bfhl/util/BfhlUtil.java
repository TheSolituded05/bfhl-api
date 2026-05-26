package com.bfhl.util;

import java.util.List;
import java.util.regex.Pattern;

public final class BfhlUtil {

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern ALPHABET_PATTERN = Pattern.compile("^[a-zA-Z]+$");

    private BfhlUtil() {
        // Private constructor to prevent instantiation of utility class
    }

    /**
     * Checks if a string is a valid integer (positive or negative).
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return NUMERIC_PATTERN.matcher(str).matches();
    }

    /**
     * Checks if a string consists only of alphabetic characters.
     */
    public static boolean isAlphabetic(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return ALPHABET_PATTERN.matcher(str).matches();
    }

    /**
     * Processes list of alphabet strings to extract letters, reverse them, and apply alternating caps.
     * Rules:
     * 1. Extract all letters from alphabetical strings
     * 2. Reverse sequence
     * 3. Apply alternating caps (Index 0 Upper, Index 1 Lower, etc.)
     */
    public static String generateConcatString(List<String> alphabets) {
        if (alphabets == null || alphabets.isEmpty()) {
            return "";
        }

        StringBuilder letters = new StringBuilder();
        for (String str : alphabets) {
            if (str != null) {
                for (char ch : str.toCharArray()) {
                    if (Character.isLetter(ch)) {
                        letters.append(ch);
                    }
                }
            }
        }

        // Reverse
        letters.reverse();

        // Apply alternating caps (1st char upper, 2nd lower, etc.)
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < letters.length(); i++) {
            char ch = letters.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(ch));
            } else {
                result.append(Character.toLowerCase(ch));
            }
        }

        return result.toString();
    }
}
