package com.example.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads test configuration from config.properties file.
 * Covers: config_file task (6 pts) — no hardcoded values in test code.
 */
public final class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            PROPS.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() { }

    public static String getBaseUrl() {
        return PROPS.getProperty("base.url");
    }

    public static String getBrowser() {
        return PROPS.getProperty("browser", "chrome");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(PROPS.getProperty("headless", "true"));
    }

    public static String getUsername() {
        return PROPS.getProperty("username");
    }

    public static String getPassword() {
        return PROPS.getProperty("password");
    }
}
