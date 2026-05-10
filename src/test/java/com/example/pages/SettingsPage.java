package com.example.pages;

import com.example.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for the user settings / preferences page.
 * Covers: dropdown task (2 pts).
 */
public class SettingsPage extends BasePage {

    private final By languageDropdown = By.cssSelector("select[name*='language'], select[name*='locale'], .language-select select");
    private final By allSelects       = By.cssSelector("select");
    private final By saveButton       = By.cssSelector("button[type='submit'], .save-button, .btn-primary");

    public SettingsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPath() {
        return "/settings";
    }

    /** Navigate to the settings page. */
    public SettingsPage navigateTo() {
        driver.get(ConfigReader.getBaseUrl() + getPath());
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        return this;
    }

    /** Select a language option from the language dropdown. */
    public void selectLanguage(String visibleText) {
        List<WebElement> selects = driver.findElements(languageDropdown);
        if (!selects.isEmpty()) {
            Select sel = new Select(selects.get(0));
            sel.selectByVisibleText(visibleText);
        } else {
            // Fallback: try any select on the page
            List<WebElement> allSels = driver.findElements(allSelects);
            if (!allSels.isEmpty()) {
                Select sel = new Select(allSels.get(0));
                sel.selectByVisibleText(visibleText);
            }
        }
    }

    /** Select an option from the first dropdown found on the page. */
    public void selectFirstDropdownByIndex(int index) {
        List<WebElement> allSels = driver.findElements(allSelects);
        if (!allSels.isEmpty()) {
            Select sel = new Select(allSels.get(0));
            sel.selectByIndex(index);
        }
    }

    /** Get selected text from the first dropdown. */
    public String getFirstDropdownSelectedText() {
        List<WebElement> allSels = driver.findElements(allSelects);
        if (!allSels.isEmpty()) {
            Select sel = new Select(allSels.get(0));
            return sel.getFirstSelectedOption().getText();
        }
        return "";
    }

    /** Check if dropdowns are present on the page. */
    public boolean hasDropdowns() {
        return !driver.findElements(allSelects).isEmpty();
    }

    /** Get count of all select elements. */
    public int getDropdownCount() {
        return driver.findElements(allSelects).size();
    }

    /** Save settings. */
    public void save() {
        List<WebElement> btns = driver.findElements(saveButton);
        if (!btns.isEmpty()) {
            scrollTo(btns.get(0));
            btns.get(0).click();
        }
    }
}
