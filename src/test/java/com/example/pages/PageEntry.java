package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Data holder representing a set of pages to test for the
 * multiple_page_test requirement.
 * Covers: multiple_page_test task (3 pts).
 */
public class PageEntry {

    private final String path;
    private final String expectedFragment;

    public PageEntry(String path, String expectedFragment) {
        this.path = path;
        this.expectedFragment = expectedFragment;
    }

    public String getPath() {
        return path;
    }

    public String getExpectedFragment() {
        return expectedFragment;
    }

    /**
     * Returns a list of pages that require the user to be logged in.
     */
    public static PageEntry[] loggedInPages() {
        return new PageEntry[] {
            new PageEntry("/home",       "/ui"),
            new PageEntry("/myProfile",  "/ui"),
            new PageEntry("/users",      "/ui"),
            new PageEntry("/banking",    "/ui"),
            new PageEntry("/messages",   "/ui"),
        };
    }
}
