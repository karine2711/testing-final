package tests;

import base.BaseTest;
import listeners.ScreenshotListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.SearchResultPage;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * These test cover the requirement:
 * 'Test Search Functionality.'
 */
@Listeners(ScreenshotListener.class)
public class SearchTest extends BaseTest {
    private SearchResultPage productMenu;

    @BeforeMethod
    public void setUp() {
        productMenu = homePage.load().clickProductMenu();
    }

    @Test
    public void searchByFirstTitleFull() {
        String firstTitle = productMenu.getFirstTitle();
        SearchResultPage searchResultPage = homePage.search(firstTitle);
        List<String> foundProductTitles = searchResultPage.getAllTitles();
        assertTrue(foundProductTitles.contains(firstTitle));
    }

    @Test
    public void searchByFirstTitlePart() {
        String firstTitle = productMenu.getFirstTitle();
        var titlePart = firstTitle.substring(0, firstTitle.length() / 2);
        SearchResultPage searchResultPage = homePage.search(titlePart);
        List<String> foundProductTitles = searchResultPage.getAllTitles();
        assertTrue(foundProductTitles.stream().anyMatch(title -> title.contains(firstTitle)));
    }

    @Test
    public void searchByFirstBrandFull() {
        String firstBrand = productMenu.getFirstBrand();
        List<String> foundProductBrands = homePage.search(firstBrand).getAllBrands();
        assertTrue(foundProductBrands.contains(firstBrand));
    }

    @Test
    public void searchByFirstBrandPart() {
        String firstBrand = productMenu.getFirstBrand();
        var brandPart = firstBrand.substring(0, firstBrand.length() / 2);
        List<String> foundProductBrands = productMenu.search(brandPart).getAllBrands();
        assertTrue(foundProductBrands.stream().anyMatch(title -> title.contains(firstBrand)));
    }

    @Test
    public void emptySearchShouldReturnAllProducts() {
        Integer lastPageNumberInProductMenu = productMenu
                .getPagination()
                .goToLastPage();

        Integer lastPageNumberInSearch = productMenu
                .search("")
                .getPagination()
                .goToLastPage();

        assertEquals(lastPageNumberInProductMenu, lastPageNumberInSearch);
    }


    @Test
    public void sqlInjectionDoesNotWork() {
        SearchResultPage searchResultPage = productMenu.search("' OR '1'='1' --");
        List<String> titles = searchResultPage.getAllTitlesWithoutWaiting();
        assertTrue(titles.isEmpty());
    }

    @Test
    public void isNotCaseSensitiveTitleUpperCase() {
        String firstTitle = productMenu.getFirstTitle();
        List<String> foundProductTitles = homePage.search(firstTitle.toUpperCase()).getAllTitles();
        assertTrue(foundProductTitles.contains(firstTitle));
    }

    @Test
    public void isNotCaseSensitiveTitleLowerCase() {
        String firstTitle = productMenu.getFirstTitle();
        List<String> foundProductTitles = productMenu.search(firstTitle.toLowerCase()).getAllTitles();
        assertTrue(foundProductTitles.contains(firstTitle));
    }
}
