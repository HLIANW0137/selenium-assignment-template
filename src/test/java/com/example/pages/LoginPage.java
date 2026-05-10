package com.example.pages;

import com.example.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Cyclos login page (/ui/login).
 * Cyclos is an Angular SPA — selectors target Angular-rendered inputs.
 * Covers: login_form task (3 pts).
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.name("login");
    private final By passwordInput = By.name("password");
    private final By loginButton  = By.cssSelector("button[type='submit']");
    private final By errorAlert   = By.cssSelector(".error-message, .alert-danger, .mat-error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPath() {
        return "/login";
    }

    /** Navigate to the login page. */
    public LoginPage navigateTo() {
        driver.get(ConfigReader.getBaseUrl() + getPath());
        waitVisible(usernameInput);
        return this;
    }

    /** Perform a successful login and return the resulting HomePage. */
    public HomePage loginAs(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return new HomePage(driver);
    }

    /** Attempt login expecting failure; stay on login page. */
    public LoginPage loginExpectingFailure(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return this;
    }

    /** Get the visible error/alert text after a failed login, or empty string. */
    public String getErrorText() {
        try {
            return getText(errorAlert);
        } catch (Exception e) {
            return "";
        }
    }

    /** Check if the error message is visible. */
    public boolean isErrorDisplayed() {
        return isDisplayed(errorAlert);
    }
}
