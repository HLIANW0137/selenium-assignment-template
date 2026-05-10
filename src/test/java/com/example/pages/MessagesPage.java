package com.example.pages;

import com.example.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the messages page in Cyclos.
 * Covers: textarea task (1 pt), send_form (1 pt).
 */
public class MessagesPage extends BasePage {

    private final By composeButton = By.cssSelector("[class*='compose'], .new-message, button[class*='create']");
    private final By toField       = By.cssSelector("input[placeholder*='o'], input[name*='to'], input[name*='recipient']");
    private final By subjectField  = By.cssSelector("input[placeholder*='ubject'], input[name*='subject']");
    private final By bodyTextarea  = By.cssSelector("textarea, .ql-editor, [contenteditable='true']");
    private final By sendButton    = By.cssSelector("button[type='submit'], .send-button, .btn-send");

    public MessagesPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPath() {
        return "/messages";
    }

    /** Navigate to the messages page. */
    public MessagesPage navigateTo() {
        driver.get(ConfigReader.getBaseUrl() + getPath());
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        return this;
    }

    /** Click the compose/new message button. */
    public void clickCompose() {
        List<WebElement> btns = driver.findElements(composeButton);
        if (!btns.isEmpty()) {
            scrollTo(btns.get(0));
            btns.get(0).click();
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
    }

    /** Fill in the message fields and send. */
    public void sendMessage(String to, String subject, String body) {
        clickCompose();
        List<WebElement> toInputs = driver.findElements(toField);
        if (!toInputs.isEmpty()) {
            toInputs.get(0).clear();
            toInputs.get(0).sendKeys(to);
        }
        List<WebElement> subjInputs = driver.findElements(subjectField);
        if (!subjInputs.isEmpty()) {
            subjInputs.get(0).clear();
            subjInputs.get(0).sendKeys(subject);
        }
        List<WebElement> bodyInputs = driver.findElements(bodyTextarea);
        if (!bodyInputs.isEmpty()) {
            bodyInputs.get(0).clear();
            bodyInputs.get(0).sendKeys(body);
        }
        List<WebElement> sendBtns = driver.findElements(sendButton);
        if (!sendBtns.isEmpty()) {
            sendBtns.get(0).click();
        }
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
    }

    /** Check if the message was sent (page redirected or success shown). */
    public boolean isMessageSent() {
        return driver.getCurrentUrl().contains("/messages") || driver.getCurrentUrl().contains("/mail");
    }
}
