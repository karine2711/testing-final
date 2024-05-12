package tests;

import base.BaseTest;
import listeners.ScreenshotListener;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.SearchResultPage;
import util.Locators;

import java.util.*;

import static org.testng.Assert.*;

/**
 * These test cover the requirement:
 * 'Test Search Functionality.'
 */
@Listeners(ScreenshotListener.class)
public class SearchTests extends BaseTest {
    @Test
    public void searchByFirstTitleFull() {
        homePage.load();
        String firstTitle = homePage.getFirstTitle();
        SearchResultPage searchResultPage = homePage.search(firstTitle);
        List<String> foundProductTitles = searchResultPage.getAllTitles();
        System.out.println(foundProductTitles);
        assertTrue(foundProductTitles.contains(firstTitle));
    }

    @Test
    public void searchByFirstTitlePart() {
        homePage.load();
        String firstTitle = homePage.getFirstTitle();
        var titlePart = firstTitle.substring(0, firstTitle.length() / 2);
        SearchResultPage searchResultPage = homePage.search(titlePart);
        List<String> foundProductTitles = searchResultPage.getAllTitles();
        System.out.println(foundProductTitles);
        assertTrue(foundProductTitles.stream().anyMatch(title -> title.contains(firstTitle)));
    }

    @Test
    public void searchByFirstBrandFull() {
        homePage.load();
        String firstBrand = homePage.getFirstBrand();
        SearchResultPage searchResultPage = homePage.search(firstBrand);
        List<String> foundProductBrands = searchResultPage.getAllBrands();
        System.out.println(foundProductBrands);
        assertTrue(foundProductBrands.contains(firstBrand));
    }

    @Test
    public void searchByFirstBrandPart() {
        homePage.load();
        String firstBrand = homePage.getFirstBrand();
        var brandPart = firstBrand.substring(0, firstBrand.length() / 2);
        SearchResultPage searchResultPage = homePage.search(brandPart);
        List<String> foundProductBrands = searchResultPage.getAllBrands();
        System.out.println(foundProductBrands);
        assertTrue(foundProductBrands.stream().anyMatch(title -> title.contains(firstBrand)));
    }

    @Test
    public void emptySearchShouldReturnAllProducts() {
        homePage.load();
        SearchResultPage productMenu = homePage.clickProductMenu();
        Integer lastPageNumberInProductMenu = productMenu.goToLastPage();

        SearchResultPage searchResultPage = homePage.search("");
        Integer lastPageNumberInSearch = searchResultPage.goToLastPage();

        assertEquals(lastPageNumberInProductMenu, lastPageNumberInSearch);
    }


    @Test
    public void sqlInjectionDoesNotWork() {
        homePage.load();
        SearchResultPage searchResultPage = homePage.search("' OR '1'='1' --");
        List<String> titles = searchResultPage.getAllTitles();

        assertTrue(titles.isEmpty());
    }

    @Test
    public void isNotCaseSensitiveTitleUpperCase() {
        homePage.load();
        String firstTitle = homePage.getFirstTitle();
        SearchResultPage searchResultPage = homePage.search(firstTitle.toUpperCase());
        List<String> foundProductTitles = searchResultPage.getAllTitles();
        System.out.println(foundProductTitles);
        assertTrue(foundProductTitles.contains(firstTitle));
    }

    @Test
    public void isNotCaseSensitiveTitleLowerCase() {
        homePage.load();
        String firstTitle = homePage.getFirstTitle();
        SearchResultPage searchResultPage = homePage.search(firstTitle.toLowerCase());
        List<String> foundProductTitles = searchResultPage.getAllTitles();
        System.out.println(foundProductTitles);
        assertTrue(foundProductTitles.contains(firstTitle));
    }
}
