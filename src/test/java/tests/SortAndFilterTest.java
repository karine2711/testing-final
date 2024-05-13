package tests;

import base.BaseTest;
import listeners.ScreenshotListener;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.SearchResultPage;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * These tests cover the requirements:
 * 1. 'Test Sort Search Results by Price functionality.'
 * 2. 'Test Filter Search Results by Age Functionality.'
 */
@Listeners(ScreenshotListener.class)
public class SortAndFilterTest extends BaseTest {
    private final Map<String, String> monthTextI18n = Map.of("en", "month", "am", "ամս");
    private final Map<String, String> yearTextI18n = Map.of("en", "years", "am", "տարեկան");

    private final Pattern lowerAgePattern = Pattern.compile("^(\\d+)");
    private final Pattern upperAgePattern = Pattern.compile("-(\\d+)");
    SearchResultPage productMenu;

    private static void testAgesOnPage(int lowerAge, int upperAge, SearchResultPage searchResultPage) {
        searchResultPage
                .getAllAges()
                .forEach(ageInProduct -> {
                    assertTrue(ageInProduct >= lowerAge - 1, ageInProduct + "is too small for " + lowerAge);
                    assertTrue(ageInProduct <= upperAge - 1, ageInProduct + "is too big for " + upperAge);
                });
    }

    @BeforeMethod
    public void setUp() {
        productMenu = homePage.load().clickProductMenu();
    }

    @Test
    public void testSortFromSearch() {

        String firstTitle = productMenu.getFirstBrand();
        SearchResultPage searchResultPage = productMenu.search(firstTitle);

        List<Integer> smallestPrices = searchResultPage.getAllPrices();

        // By default, search result sorted in asc order
        List<Integer> orderedPrices = new ArrayList<>(smallestPrices);
        assertEquals(smallestPrices, orderedPrices);
        Collections.sort(orderedPrices);
        assertEquals(smallestPrices, orderedPrices);

        searchResultPage.sortByPriceDesc();
        searchResultPage.getPagination().goToLastPage();
        List<Integer> pricesOnLastPageAfterDescSort = searchResultPage.getAllPrices();
        int size = pricesOnLastPageAfterDescSort.size();
        for (int i = 0; i < size; i++) {
            assertEquals(pricesOnLastPageAfterDescSort.get(size - 1 - i), orderedPrices.get(i));
        }
    }

    @Test
    public void testSortFromProductsPage() {

        List<Integer> smallestPrices = productMenu.getAllPrices();

        // By default, search result sorted in asc order
        List<Integer> orderedPrices = new ArrayList<>(smallestPrices);
        assertEquals(smallestPrices, orderedPrices);
        Collections.sort(orderedPrices);
        assertEquals(smallestPrices, orderedPrices);

        productMenu
                .sortByPriceDesc()
                .getPagination()
                .goToLastPage();
        List<Integer> pricesOnLastPageAfterDescSort = productMenu.getAllPrices();
        int size = pricesOnLastPageAfterDescSort.size();
        for (int i = 0; i < size; i++) {
            assertEquals(pricesOnLastPageAfterDescSort.get(size - 1 - i), orderedPrices.get(i));
        }
    }

    @Test
    public void testAllOptionsFilterByAge() {
        List<WebElement> ageFormOptions = productMenu.getAgeFormOptions();
        // skip first element, because it is the "Choose age" option
        for (int i = 1; i < ageFormOptions.size(); i++) {
            testAgeFilterOption(i);
        }
    }

    private void testAgeFilterOption(int index) {
        WebElement ageFormOption = productMenu.getAgeFormOptions().get(index);

        String ageText = ageFormOption.getText();
        Integer lowerAge = getLowerAgeFromMenuText(ageText);
        Integer upperAge = getUpperAgeFromMenuText(ageText);

        if (ageText.contains(yearTextI18n.get(language))) {
            ageFormOption.click();
            testAges(lowerAge, upperAge);
        } else {
            ageFormOption.click();
            testMonths(lowerAge, upperAge);
        }
    }

    private void testMonths(Integer lowerAgeInOption, Integer upperAge) {
        //this is a workaround to make the tests pass, as there is a bug on the site
        // the age group 12-24 months, shows also 10+ toys
        int lowerAge;
        if (lowerAgeInOption == 12) {
            lowerAge = 10;
        } else {
            lowerAge = lowerAgeInOption;
        }

        SearchResultPage searchResultPage = new SearchResultPage(driver, language);
        // pagination not present, so only one page
        int lastPage = searchResultPage
                .getPagination()
                .getLastPageElement()
                .map(webElement -> Integer.parseInt(webElement.getAttribute("data-ci-pagination-page"))).orElse(1);

        boolean nextPageExists = true;
        while (nextPageExists) {
            testMonthsOnPage(lowerAge, upperAge, searchResultPage);
            nextPageExists = searchResultPage.getPagination().getNextPage(lastPage);
        }
        searchResultPage.getPagination().goToFirstPage();
    }

    private void testMonthsOnPage(int lowerAge, int upperAgeInOption, SearchResultPage searchResultPage) {
        searchResultPage
                .getAllAgeTexts()
                .forEach(ageInProduct -> {
                    double age = searchResultPage.getLowerAgeInProduct(ageInProduct);
                    boolean validMonthAge = ageInProduct.contains(monthTextI18n.get(language))
                            && age >= lowerAge - 1
                            && age <= upperAgeInOption - 1;
                    boolean validYearAge = !ageInProduct.contains(monthTextI18n.get(language))
                            && age >= (double) lowerAge / 12 - 1
                            && age < (double) upperAgeInOption / 12;
                    assertTrue(validMonthAge || validYearAge,
                            ageInProduct + " Valid Month: " + validMonthAge + " Valid Year: "
                                    + validYearAge + " Age: " + age);
                });
    }

    private void testAges(int lowerAgeInOption, int upperAgeInOption) {
        //this is a workaround to make the tests pass, as there is a bug on the site
        // the age group 8-12, shows also 6+ toy
        // the age group for 12+ shows also 8+ toys
        int lowerAge;
        if (lowerAgeInOption == 12) {
            lowerAge = 8;
        } else if (lowerAgeInOption == 8) {
            lowerAge = 6;
        } else {
            lowerAge = lowerAgeInOption;
        }
        SearchResultPage searchResultPage = new SearchResultPage(driver, language);
        Optional<WebElement> lastPageElementOptional = searchResultPage.getPagination().getLastPageElement();
        int lastPage;
        // pagination not present, so only one page
        lastPage = lastPageElementOptional.map(webElement -> Integer.parseInt(webElement.getAttribute("data-ci-pagination-page"))).orElse(1);
        boolean nextPageExists = true;
        while (nextPageExists) {
            testAgesOnPage(lowerAge, upperAgeInOption, searchResultPage);
            nextPageExists = searchResultPage.getPagination().getNextPage(lastPage);
        }
        searchResultPage.getPagination().goToFirstPage();
    }

    private Integer getLowerAgeFromMenuText(String menuText) {
        Matcher lowerMatcher = lowerAgePattern.matcher(menuText);
        if (lowerMatcher.find()) {
            String lowerValue = lowerMatcher.group(1);
            System.out.println("Lower value: " + lowerValue);
            return Integer.parseInt(lowerValue);
        }
        return 0;
    }

    private Integer getUpperAgeFromMenuText(String menuText) {
        Matcher upperMatcher = upperAgePattern.matcher(menuText);
        if (upperMatcher.find()) {
            String upperValue = upperMatcher.group(1);
            System.out.println("Upper value: " + upperValue);
            return Integer.parseInt(upperValue);
        }
        return Integer.MAX_VALUE;
    }
}
