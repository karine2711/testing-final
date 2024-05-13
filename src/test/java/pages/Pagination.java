package pages;

import base.BasePage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.Locators;

import java.util.List;
import java.util.Optional;

public class Pagination extends BasePage {
    public static final String DATA_CI_PAGINATION_PAGE = "data-ci-pagination-page";

    public Pagination(WebDriver driver, String language) {
        super(driver, language);
    }

    public void goToFirstPage() {
        try {
            driver.findElement(Locators.PAGINATION_START).click();
        } catch (NoSuchElementException e) {
            System.out.println("Pagination not present. Can't go to first page");
        }
    }

    public Integer goToLastPage() {
        Optional<WebElement> lastPageElementOptional = getLastPageElement();
        if (lastPageElementOptional.isPresent()) {
            lastPageElementOptional.get().click();
            return Integer.parseInt(getActivePage().get().getText());
        }
        return 1; // pagination doesn't exist, so there is only one page
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

    private Optional<WebElement> getActivePage() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(Locators.PAGINATION_ACTIVE_LINK));
            return Optional.of(driver.findElement(Locators.PAGINATION_ACTIVE_LINK));
        } catch (TimeoutException e) {
            return Optional.empty(); // no pagination present
        }
    }

}
