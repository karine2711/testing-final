package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.Locators;


public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public SearchResultPage clickFirstSearchBubble() {
        WebElement firstSearchBubble = driver.findElements(Locators.HOMEPAGE_SEARCH_BUBBLES).get(0);
        firstSearchBubble.click();
        return new SearchResultPage(driver);
    }

    public String getFirstTitle() {
        wait.until(ExpectedConditions.presenceOfElementLocated(Locators.PRODUCT_TITLES));
        var titleAnchor = driver.findElements(Locators.PRODUCT_TITLES).get(0);
        var title = titleAnchor.getAttribute("innerText");
        System.out.println("Title found: " + title);

        return title;
    }
    public String getFirstBrand() {
        wait.until(ExpectedConditions.presenceOfElementLocated(Locators.PRODUCT_BRAND));
        var titleAnchor = driver.findElements(Locators.PRODUCT_BRAND).get(0);
        var title = titleAnchor.getAttribute("innerText");
        System.out.println("Brand found: " + title);
        return title;
    }

    public HomePage load() {
        super.load("/");
        return this;
    }

}
