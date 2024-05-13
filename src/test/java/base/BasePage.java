package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.SearchResultPage;
import util.Locators;

import java.time.Duration;
import java.util.List;

import static util.Locators.*;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected String language;

    public BasePage(WebDriver driver, String language) {
        this.driver = driver;
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.pollingEvery(Duration.ofSeconds(5));
        actions = new Actions(driver);
        this.language = language;
    }

    protected void load(String endPoint) {
        driver.get("https://megatoys.am/" + language + endPoint);
    }

    public SearchResultPage search(String input) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
        WebElement searchInput = driver.findElement(SEARCH_INPUT);
        searchInput.sendKeys(input);
        searchInput.findElement(SEARCH_BUTTON).click();
        return new SearchResultPage(driver, language);
    }

    public SearchResultPage clickProductMenu() {
        driver.findElement(Locators.PRODUCT_MENU).findElement(Locators.DROPDOWN_TOGGLE).click();
        return new SearchResultPage(driver, language);
    }

    public List<WebElement> getAgeFormOptions() {
        wait.until(ExpectedConditions.presenceOfElementLocated(AGE_OPTIONS));
        return driver.findElements(Locators.AGE_OPTIONS);
    }

}
