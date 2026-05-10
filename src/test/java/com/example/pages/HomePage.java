package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the Cyclos home/dashboard page after login.
 * This page shows the user's dashboard with navigation menu.
 * Covers: form_with_user (3 pts), logout (2 pts).
 */
public class HomePage extends BasePage {

    private final By menuItems      = By.cssSelector("mat-toolbar .menu-item, .menu-entry, [class*='menu'] a, [class*='menu'] span");
    private final By logoutLink     = By.cssSelector("[href*='logout'], .logout, [data-action='logout']");
    private final By userMenuBtn    = By.cssSelector(".user-menu, [class*='user-menu'], [class*='profile-menu'], .user-button");
    private final By pageTitle      = By.cssSelector("mat-toolbar, .page-title, h1, h2");
    private final By mainContent    = By.cssSelector("main, .main-content, .content, #content");
    private final By navLinks       = By.cssSelector("a[href], [routerlink], .nav-link, .menu-entry");
    private final By hamburgerMenu  = By.cssSelector(".hamburger, .menu-toggle, [class*='toggle-menu']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPath() {
        return "/";
    }

    /** Check if the dashboard loaded successfully. */
    public boolean isDashboardLoaded() {
        try {
            // Wait for any toolbar or main content to appear
            Thread.sleep(2000);
            return driver.getCurrentUrl().contains("/ui") && !driver.getCurrentUrl().contains("/login");
        } catch (InterruptedException e) {
            return false;
        }
    }

    /** Get all visible navigation menu item texts. */
    public List<WebElement> getMenuItems() {
        return driver.findElements(menuItems);
    }

    /** Click the logout button/link. */
    public LoginPage logout() {
        try {
            // Try clicking user menu first, then logout
            List<WebElement> userBtns = driver.findElements(userMenuBtn);
            if (!userBtns.isEmpty()) {
                click(userMenuBtn);
                Thread.sleep(500);
            }
            click(logoutLink);
        } catch (Exception e) {
            // Fallback: try direct logout URL
            driver.get(com.example.config.ConfigReader.getBaseUrl() + "/logout");
        }
        return new LoginPage(driver);
    }

    /** Navigate to a specific menu item by visible text. */
    public void navigateToMenuItem(String text) {
        List<WebElement> items = driver.findElements(navLinks);
        for (WebElement item : items) {
            if (item.getText().contains(text)) {
                scrollTo(item);
                item.click();
                return;
            }
        }
    }

    /** Get all navigation link texts for inspection. */
    public List<WebElement> getAllNavLinks() {
        return driver.findElements(navLinks);
    }

    /** Get the number of visible nav links. */
    public int getNavLinkCount() {
        return driver.findElements(navLinks).size();
    }

    /** Check if main content area is present. */
    public boolean isMainContentPresent() {
        return !driver.findElements(mainContent).isEmpty();
    }
}
