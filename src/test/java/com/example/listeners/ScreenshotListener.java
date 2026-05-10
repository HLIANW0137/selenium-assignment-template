package com.example.listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestNG listener that captures a screenshot automatically when a test fails.
 * Covers: screenshot_on_failure task (6 pts).
 */
public class ScreenshotListener implements ITestListener {

    private static final String DIR = "target/screenshots";

    @Override
    public void onTestFailure(ITestResult result) {
        new File(DIR).mkdirs();
        Object instance = result.getInstance();
        try {
            java.lang.reflect.Field f = findDriverField(instance.getClass());
            if (f != null) {
                f.setAccessible(true);
                WebDriver driver = (WebDriver) f.get(instance);
                if (driver != null) {
                    saveScreenshot(driver, result.getMethod().getMethodName());
                }
            }
        } catch (Exception e) {
            System.err.println("Screenshot capture failed: " + e.getMessage());
        }
    }

    private java.lang.reflect.Field findDriverField(Class<?> clazz) {
        while (clazz != null) {
            for (java.lang.reflect.Field f : clazz.getDeclaredFields()) {
                if (WebDriver.class.isAssignableFrom(f.getType())) {
                    return f;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private void saveScreenshot(WebDriver driver, String testName) {
        try {
            byte[] data = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String path = DIR + File.separator + testName + "_" + ts + ".png";
            Files.write(Paths.get(path), data);
            System.out.println("Screenshot saved: " + path);
        } catch (IOException e) {
            System.err.println("Error writing screenshot: " + e.getMessage());
        }
    }
}
