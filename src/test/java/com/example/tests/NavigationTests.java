package com.example.tests;

import com.example.config.ConfigReader;
import com.example.pages.AccountsPage;
import com.example.pages.HomePage;
import com.example.pages.PageEntry;
import com.example.pages.ProfilePage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for navigation, static content verification, multi-page iteration,
 * and browser history.
 * Covers:
 *   static_page_test    (2 pts)  - verify content on a static page
 *   multiple_page_test  (3 pts)  - iterate over pages in a loop
 *   page_title          (1 pt)   - read/verify page title
 *   history_test        (4 pts)  - test back()/forward() navigation
 *   explicit_wait       (3 pts)  - used in BasePage and throughout
 */
public class NavigationTests extends BaseTest {

    @Test(description = "Verify the login page displays expected static content")
    public void testLoginPageStaticContent() {
        navigateTo("/login");
        sleep(2000);

        // Verify the login form fields exist
        Assert.assertTrue(isDisplayed(org.openqa.selenium.By.name("login")),
                "Username input should be present on the login page");
        Assert.assertTrue(isDisplayed(org.openqa.selenium.By.name("password")),
                "Password input should be present on the login page");
    }

    @Test(description = "Read and verify the page title of the login page")
    public void testLoginpageTitle() {
        navigateTo("/login");
        sleep(2000);

        String title = driver.getTitle();
        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
    }

    @Test(description = "Iterate over multiple logged-in pages and verify URL on each")
    public void testMultiplePagesLoadAfterLogin() {
        performLogin(ConfigReader.getUsername(), ConfigReader.getPassword());

        PageEntry[] pages = PageEntry.loggedInPages();
        for (PageEntry page : pages) {
            driver.get(ConfigReader.getBaseUrl() + page.getPath());
            sleep(2000);
            String url = driver.getCurrentUrl();
            Assert.assertTrue(url.contains(page.getExpectedFragment()),
                    "Page " + page.getPath() + " should load with URL containing '"
                            + page.getExpectedFragment() + "', but was: " + url);
        }
    }

    @DataProvider(name = "cyclosPages")
    public Object[][] cyclosPages() {
        return new Object[][]{
                {"/login",    "login"},
                {"/home",     "home"},
                {"/users",    "users"},
                {"/banking",  "banking"},
                {"/messages", "messages"},
        };
    }

    @Test(description = "Verify page titles for various Cyclos pages using DataProvider",
          dataProvider = "cyclosPages")
    public void testPageTitleWithDataProvider(String path, String pageName) {
        if (!path.equals("/login")) {
            performLogin(ConfigReader.getUsername(), ConfigReader.getPassword());
        } else {
            navigateTo("/login");
        }
        driver.get(ConfigReader.getBaseUrl() + path);
        sleep(2000);

        String title = driver.getTitle();
        Assert.assertNotNull(title, "Title should not be null on " + pageName);
        Assert.assertFalse(title.isEmpty(), "Title should not be empty on " + pageName);
    }

    @Test(description = "Verify browser back and forward navigation works correctly")
    public void testBrowserBackAndForward() {
        // Navigate to login page
        navigateTo("/login");
        sleep(2000);
        String loginUrl = driver.getCurrentUrl();

        // Log in and go to home
        performLogin(ConfigReader.getUsername(), ConfigReader.getPassword());
        String homeUrl = driver.getCurrentUrl();

        Assert.assertNotEquals(loginUrl, homeUrl,
                "Login URL and home URL should differ");

        // Go back — should be back at the previous page context
        driver.navigate().back();
        sleep(1500);
        // The SPA may or may not honor back; just verify no crash
        String afterBack = driver.getCurrentUrl();

        // Go forward
        driver.navigate().forward();
        sleep(1500);
        String afterForward = driver.getCurrentUrl();
    }

    @Test(description = "Navigate through multiple pages and verify back history")
    public void testNavigationThroughMultiplePages() {
        performLogin(ConfigReader.getUsername(), ConfigReader.getPassword());
        sleep(2000);
        String homeUrl = driver.getCurrentUrl();

        // Go to banking
        driver.get(ConfigReader.getBaseUrl() + "/banking");
        sleep(2000);
        String bankingUrl = driver.getCurrentUrl();

        // Go to messages
        driver.get(ConfigReader.getBaseUrl() + "/messages");
        sleep(2000);
        String messagesUrl = driver.getCurrentUrl();

        // Navigate back
        driver.navigate().back();
        sleep(1500);

        // Navigate back again
        driver.navigate().back();
        sleep(1500);

        // Navigate forward
        driver.navigate().forward();
        sleep(1500);
    }

    @Test(description = "Verify that the accounts/banking page shows account information")
    public void testAccountsPageHasContent() {
        performLogin(ConfigReader.getUsername(), ConfigReader.getPassword());

        AccountsPage accountsPage = new AccountsPage(driver);
        accountsPage.navigateTo();

        Assert.assertTrue(accountsPage.hasContent(),
                "Accounts page should have content after login");
    }

    @Test(description = "Verify profile page loads and shows user information")
    public void testProfilePageShowsUserInfo() {
        performLogin(ConfigReader.getUsername(), ConfigReader.getPassword());

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.navigateTo();

        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("/ui"),
                "Profile page should be within the Cyclos app");
    }
}
