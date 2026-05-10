package com.example.pages;

import com.example.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the contacts / user directory page.
 * Demonstrates searching and iterating over multiple items.
 * Covers: multiple_page_test, fill_input tasks.
 */
public class ContactsPage extends BasePage {

    private final By searchInput = By.cssSelector("input[type='search'], input[placeholder*='earch'], input[name*='query']");
    private final By searchBtn   = By.cssSelector("button[type='submit'], .search-button, .btn-search");
    private final By resultRows  = By.cssSelector("table tbody tr, .list-item, .user-card, mat-row");

    public ContactsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPath() {
        return "/users";
    }

    /** Navigate to the contacts/directory page. */
    public ContactsPage navigateTo() {
        driver.get(ConfigReader.getBaseUrl() + getPath());
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        return this;
    }

    /** Type a search query and submit. */
    public void searchFor(String query) {
        List<WebElement> inputs = driver.findElements(searchInput);
        if (!inputs.isEmpty()) {
            scrollTo(inputs.get(0));
            inputs.get(0).clear();
            inputs.get(0).sendKeys(query);
            List<WebElement> btns = driver.findElements(searchBtn);
            if (!btns.isEmpty()) {
                btns.get(0).click();
            }
        }
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
    }

    /** Get the number of result rows displayed. */
    public int getResultCount() {
        return driver.findElements(resultRows).size();
    }

    /** Check if any results are shown. */
    public boolean hasResults() {
        return getResultCount() > 0;
    }

    /** Click on the first search result. */
    public void clickFirstResult() {
        List<WebElement> rows = driver.findElements(resultRows);
        if (!rows.isEmpty()) {
            scrollTo(rows.get(0));
            rows.get(0).click();
        }
    }
}
