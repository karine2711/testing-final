package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.Locators;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchResultPage extends BasePage {
    public static final String DATA_CI_PAGINATION_PAGE = "data-ci-pagination-page";
    Pattern ageInProductPattern = Pattern.compile("(\\d+(\\.\\d+)?)");

    public SearchResultPage(WebDriver driver, String language) {
        super(driver, language);
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

    //if all items are visible
    public Integer goToLastPage() {
        Optional<WebElement> lastPageElementOptional = getLastPageElement();
        if (lastPageElementOptional.isPresent()) {
            lastPageElementOptional.get().click();
            return Integer.parseInt(getActivePage().get().getText());
        }
        return 1; // pagination doesn't exist, so there is only one page
    }

    public void goToFirstPage() {

        try {
            driver.findElement(Locators.PAGINATION_START).click();
        } catch (NoSuchElementException e) {
            System.out.println("Pagination not present. Can't go to first page");
        }
    }

    private Optional<WebElement> getActivePage() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(Locators.PAGINATION_ACTIVE_LINK));

            return Optional.of(driver.findElement(Locators.PAGINATION_ACTIVE_LINK));
        } catch (TimeoutException e) {
            return Optional.empty(); // no pagination present
        }
    }

    public Optional<WebElement> getLastPageElement() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(Locators.PAGINATION_ITEM_LINK));
            List<WebElement> paginationItems = driver.findElements(Locators.PAGINATION_ITEM_LINK);
            return Optional.of(calculateMaxPaginationElement(paginationItems));
        } catch (TimeoutException e) {
            System.out.println("Pagination element not present");
            return Optional.empty();
        }
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

    private WebElement calculateMaxPaginationElement(List<WebElement> paginationItems) {
        // Initialize variables to keep track of the largest page number and its corresponding <li> element
        int maxPageNumber = Integer.MIN_VALUE;
        WebElement maxPageElement = null;

        // Iterate through the <li> elements to find the one with the largest page number
        for (WebElement a : paginationItems) {
            String pageNumberAttribute = a.getAttribute(DATA_CI_PAGINATION_PAGE);
            if (pageNumberAttribute != null && !pageNumberAttribute.isEmpty()) {
                int pageNumber = Integer.parseInt(pageNumberAttribute);
                if (pageNumber > maxPageNumber) {
                    maxPageNumber = pageNumber;
                    maxPageElement = a;
                }
            }
        }
        return maxPageElement;
    }


    /**
     * If the next page exists goes to it and returns true,
     * otherwise, stays on the same page and returns false
     */
    public boolean getNextPage(int lastPage) {
        Optional<WebElement> activePageOptional = getActivePage();
        if (activePageOptional.isPresent()) {
            int activePage = Integer.parseInt(activePageOptional.get().getText());
            if (lastPage != activePage) {
                wait.until(ExpectedConditions.elementToBeClickable(Locators.PAGINATION_NEXT));
                driver.findElement(Locators.PAGINATION_NEXT).click();
                return true;
            }
        }
        return false;
    }
}
