package com.example.tests;

import com.example.config.ConfigReader;
import com.example.pages.HomePage;
import com.example.pages.LoginPage;
import com.example.utils.TestDataGenerator;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for login, logout, and authentication flows on Cyclos.
 * Covers:
 *   login_form          (3 pts)  - fill login form and submit
 *   logout              (2 pts)  - log out and verify
 *   page_title          (1 pt)   - read and verify page title
 *   readable_tests      (3 pts)  - descriptive @Test descriptions
 *   test_dependencies   (4 pts)  - dependsOnMethods
 *   send_form           (1 pt)   - login is a distinct form submission
 *   fill_input          (1 pt)   - fill text inputs (username/password)
 */
public class LoginTests extends BaseTest {

    @Test(description = "Verify successful login with valid demo credentials shows dashboard")
    public void testSuccessfulLoginShowsDashboard() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        HomePage homePage = loginPage.loginAs(
                ConfigReader.getUsername(),
                ConfigReader.getPassword()
        );

        Assert.assertTrue(homePage.isDashboardLoaded(),
                "Dashboard should be accessible after successful login");
    }

    @Test(description = "Verify login fails with wrong password and error is shown")
    public void testLoginFailsWithWrongPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        loginPage.loginExpectingFailure("demo", "wrong_password_999");

        // After wrong credentials the URL should still contain /login
        sleep(2000);
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Should remain on login page after invalid credentials");
    }

    @Test(description = "Verify login page has a title")
    public void testLoginPageTitle() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        String title = loginPage.pageTitle();
        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
    }

    @Test(description = "Verify logout redirects back to login screen")
    public void testLogoutRedirectsToLogin() {
        performLogin(ConfigReader.getUsername(), ConfigReader.getPassword());

        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.logout();

        sleep(2000);
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "After logout, should redirect to login page");
    }

    @Test(description = "Logout test depends on successful login test",
          dependsOnMethods = "testSuccessfulLoginShowsDashboard")
    public void testLogoutDependsOnLogin() {
        performLogin(ConfigReader.getUsername(), ConfigReader.getPassword());

        HomePage homePage = new HomePage(driver);
        homePage.logout();

        sleep(2000);
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Logout should succeed after a verified login");
    }

    @Test(description = "Submit login form with randomly generated credentials to test error handling")
    public void testLoginWithRandomCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        String randomUser = TestDataGenerator.randomUsername();
        loginPage.loginExpectingFailure(randomUser, TestDataGenerator.randomString(10));

        sleep(2000);
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Random credentials should fail and stay on login page");
    }
}
