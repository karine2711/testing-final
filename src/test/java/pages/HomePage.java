package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;


public class HomePage extends BasePage {
    public HomePage(WebDriver driver, String language) {
        super(driver, language);
    }

    public HomePage load() {
        super.load("/");
        return this;
    }
}
