package com.example.utils;

import java.util.Random;

/**
 * Generates random test data for form filling.
 * Covers: random_data task (8 pts).
 */
public final class TestDataGenerator {

    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RNG = new Random();

    private TestDataGenerator() { }

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(RNG.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    public static String randomEmail() {
        return "test_" + randomString(8) + "@example.com";
    }

    public static String randomUsername() {
        return "user_" + randomString(6);
    }

    public static String randomPhoneNumber() {
        return "+1" + (1000000000L + RNG.nextInt(900000000));
    }

    public static int randomNumber(int min, int max) {
        return RNG.nextInt(max - min + 1) + min;
    }
}
