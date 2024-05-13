package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.Locators;


public class HomePage extends BasePage {
    String endpoint;

    public HomePage(WebDriver driver, String language) {
        super(driver,language);
    }

    public HomePage load() {
        super.load("/");
        return this;
    }

}
