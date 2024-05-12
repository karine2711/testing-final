package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.Locators;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchResultPage extends BasePage {
    public static final String DATA_CI_PAGINATION_PAGE = "data-ci-pagination-page";
    Pattern ageInProductPattern = Pattern.compile("(\\d+(\\.\\d+)?)\\+");

    public SearchResultPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getAllTitles() {
        return driver
                .findElements(Locators.PRODUCT_TITLES)
                .stream()
                .map(e -> e.getAttribute("innerText"))
                .toList();
    }

    public List<String> getAllBrands() {
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
            return Integer.parseInt(getActivePage().getText());
        }
        return 1; // pagination doesn't exist, so there is only one page
    }

    public void goToFirstPage() {
        driver.findElement(Locators.PAGINATION_START).click();
    }

    private WebElement getActivePage() {
        wait.until(ExpectedConditions.presenceOfElementLocated(Locators.PAGINATION_ACTIVE_LINK));
        return driver.findElement(Locators.PAGINATION_ACTIVE_LINK);
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

    private Double getLowerAgeInProduct(String ageText) {
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
        WebElement activePageki = getActivePage();
        try {
            int activePage = Integer.parseInt(activePageki.getText());
            if (lastPage != activePage) {
                wait.until(ExpectedConditions.elementToBeClickable(Locators.PAGINATION_NEXT));
                driver.findElement(Locators.PAGINATION_NEXT).click();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(activePageki);
            System.out.println(activePageki.getText());
            throw e;
        }
    }
}
