package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.Locators;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchResultPage extends BasePage {
    private final Pattern ageInProductPattern = Pattern.compile("(\\d+(\\.\\d+)?)");
    private final Pagination pagination;

    public SearchResultPage(WebDriver driver, String language) {
        super(driver, language);
        pagination = new Pagination(driver, language);
    }

    public Pagination getPagination() {
        return pagination;
    }

    public List<String> getAllTitles() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(Locators.PRODUCT_TITLES));
        return driver
                .findElements(Locators.PRODUCT_TITLES)
                .stream()
                .map(e -> e.getAttribute("innerText"))
                .toList();
    }

    public List<String> getAllTitlesWithoutWaiting() {
        return driver
                .findElements(Locators.PRODUCT_TITLES)
                .stream()
                .map(e -> e.getAttribute("innerText"))
                .toList();
    }

    public String getFirstTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.PRODUCT_TITLES));
        var titleAnchor = driver.findElement(Locators.PRODUCT_TITLES);
        var title = titleAnchor.getAttribute("innerText");
        System.out.println("Title found: " + title);

        return title;
    }

    public String getFirstBrand() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.PRODUCT_BRAND));
        var titleAnchor = driver.findElement(Locators.PRODUCT_BRAND);
        var title = titleAnchor.getAttribute("innerText");
        System.out.println("Brand found: " + title);
        return title;
    }

    public List<String> getAllBrands() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(Locators.PRODUCT_BRAND));
        return driver
                .findElements(Locators.PRODUCT_BRAND)
                .stream()
                .map(e -> e.getAttribute("innerText"))
                .toList();
    }

    public SearchResultPage sortByPriceDesc() {
        wait.until(ExpectedConditions.elementToBeClickable(Locators.SORT_LINK));
        driver.findElement(Locators.SORT_LINK).click();
        return this;
    }

    public List<Integer> getAllPrices() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(Locators.PRODUCT_PRICE));
        return driver
                .findElements(Locators.PRODUCT_PRICE)
                .stream().map(this::getPriceFromElement)
                .collect(Collectors.toList());
    }

    public List<Double> getAllAges() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(Locators.PRODUCT_AGE_XPATH));

        return driver
                .findElements(Locators.PRODUCT_AGE_XPATH)
                .stream().map(e -> getLowerAgeInProduct(e.getText())).toList();

    }

    public List<String> getAllAgeTexts() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(Locators.PRODUCT_AGE_XPATH));

        return driver
                .findElements(Locators.PRODUCT_AGE_XPATH)
                .stream().map(WebElement::getText).toList();

    }

    public Double getLowerAgeInProduct(String ageText) {
        Matcher ageMatcher = ageInProductPattern.matcher(ageText);
        if (ageMatcher.find()) {
            String ageValue = ageMatcher.group(1);
            return Double.parseDouble(ageValue);
        }
        return 0.0;
    }

    private int getPriceFromElement(WebElement elem) {
        WebElement oldPrice = null;
        try {
            oldPrice = elem.findElement(Locators.OLD_PRICE);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // ignore
        }
        return Integer.parseInt(elem.getText()
                .replace(elem.findElement(By.tagName("strong")).getText(), "")
                .replace(oldPrice != null ? oldPrice.getText() : "", "")
                .replace("Դր.", "")
                .replace("AMD", "")
                .trim());
    }
}
