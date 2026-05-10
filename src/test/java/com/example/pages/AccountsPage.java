package com.example.pages;

import com.example.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the accounts / transaction history page.
 * Covers: static_page_test task (2 pts).
 */
public class AccountsPage extends BasePage {

    private final By accountCards = By.cssSelector(".account-card, .balance-card, mat-card");
    private final By tableRows    = By.cssSelector("table tbody tr, .transaction-row");
    private final By balanceText  = By.cssSelector("[class*='balance'], .amount, .account-balance");

    public AccountsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPath() {
        return "/banking";
    }

    /** Navigate to the accounts/banking page. */
    public AccountsPage navigateTo() {
        driver.get(ConfigReader.getBaseUrl() + getPath());
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        return this;
    }

    /** Check if account cards or balance info is displayed. */
    public boolean isAccountInfoDisplayed() {
        return !driver.findElements(accountCards).isEmpty()
            || !driver.findElements(balanceText).isEmpty();
    }

    /** Get the number of transaction rows shown. */
    public int getTransactionRowCount() {
        return driver.findElements(tableRows).size();
    }

    /** Get all account card elements. */
    public List<WebElement> getAccountCards() {
        return driver.findElements(accountCards);
    }

    /** Check if the page has any content at all. */
    public boolean hasContent() {
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        return driver.getPageSource().length() > 100;
    }
}
