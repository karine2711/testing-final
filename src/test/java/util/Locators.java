package util;

import org.openqa.selenium.By;

public class Locators {
    // Homepage Locators
    public static final By HOMEPAGE_SEARCH_BUBBLES = By.className("homepage_search_bubble");
    public static final By SEARCH_INPUT = By.cssSelector("input[name=\"search\"]");
    public static final By SEARCH_BUTTON = By.xpath("following-sibling::button[@type='submit']");
    public static final By PRODUCT_TITLES = By.cssSelector("div.product-list-title a");
    public static final By PRODUCT_BRAND = By.cssSelector(".products-age a");
    public static final By PRODUCT_MENU = By.className("products-menu"); // this is correct, the classes in the webpage are twisted
    public static final By DROPDOWN_TOGGLE = By.xpath("preceding-sibling::a[@class='dropdown-toggle']");
    public static final By AGE_OPTIONS = By.cssSelector("#mt-age option");


    // Products
    public static final By PAGINATION_ITEM_LINK = By.cssSelector(".pagination li a");
    public static final By PAGINATION_ACTIVE_LINK = By.cssSelector(".pagination li.active a");
    public static final By PAGINATION_NEXT = By.cssSelector(".pagination a[rel=\"next\"]");
    public static final By PAGINATION_START = By.cssSelector(".pagination a[rel=\"start\"]");

    public static final By SORT_LINK = By.className("order-link");
    public static final By PRODUCT_PRICE = By.cssSelector("p.product-price");
    public static final By OLD_PRICE = By.className("product-old-price");
    public static final By PRODUCT_AGE_XPATH = By.xpath("//p[@class='product-price']/preceding-sibling::p[@class='products-age'][1]");


    //Registration
    public static final By REGISTRATION_FORM = By.cssSelector("form[action=\"https://megatoys.am/en/users/edit\"]");
    public static final By SUBMIT_BUTTON = By.cssSelector("button[type=\"submit\"]");
    public static final By INPUT_TEXT = By.cssSelector("input[type=\"text\"]");
    public static final By EMAIL_INPUT = By.cssSelector("input[name=\"email\"]");
    public static final By PASSWORD_INPUT = By.cssSelector("input[name=\"password\"]");
    public static final String BIRTH_DAY_CSS = "select[name=\"days\"]";
    public static final String BIRTH_MONTH_CSS = "select[name=\"months\"]";
    public static final String BIRTH_YEAR_CSS = "select[name=\"years\"]";
    public static final By BIRTH_DAY = By.cssSelector(BIRTH_DAY_CSS);
    public static final By BIRTH_MONTH = By.cssSelector(BIRTH_MONTH_CSS);
    public static final By BIRTH_YEAR = By.cssSelector(BIRTH_YEAR_CSS);
}
