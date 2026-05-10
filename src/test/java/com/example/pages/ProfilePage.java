package com.example.pages;

import com.example.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the user profile page in Cyclos.
 * Covers: form_with_user task (3 pts).
 */
public class ProfilePage extends BasePage {

    private final By profileInputs  = By.cssSelector("input[type='text'], input[type='email'], textarea");
    private final By saveButton     = By.cssSelector("button[type='submit'], .save-button, .btn-primary");
    private final By profileName    = By.cssSelector("[class*='display'], .profile-name, .user-name");
    private final By editButton     = By.cssSelector("[class*='edit'], .edit-button, .btn-edit");

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPath() {
        return "/myProfile";
    }

    /** Navigate to the profile page directly. */
    public ProfilePage navigateTo() {
        driver.get(ConfigReader.getBaseUrl() + getPath());
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        return this;
    }

    /** Get all text input fields on the profile page. */
    public List<WebElement> getProfileInputs() {
        return driver.findElements(profileInputs);
    }

    /** Click the edit button to enable editing. */
    public void clickEdit() {
        List<WebElement> editBtns = driver.findElements(editButton);
        if (!editBtns.isEmpty()) {
            scrollTo(editBtns.get(0));
            editBtns.get(0).click();
        }
    }

    /** Save the profile changes. */
    public void save() {
        List<WebElement> saveBtns = driver.findElements(saveButton);
        if (!saveBtns.isEmpty()) {
            scrollTo(saveBtns.get(0));
            saveBtns.get(0).click();
        }
    }

    /** Get the displayed profile name text. */
    public String getDisplayedName() {
        try {
            return getText(profileName);
        } catch (Exception e) {
            return "";
        }
    }
}
