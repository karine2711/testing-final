package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import util.Locators;

public class RegistrationPage extends BasePage {
    JavascriptExecutor js = (JavascriptExecutor) driver;


    public RegistrationPage(WebDriver driver,String language) {
        super(driver,language);
        load();
    }

    public RegistrationPage load() {
        super.load("/users/edit/");
        return this;
    }

    private WebElement getSubmitButton() {
        return getForm().findElement(Locators.SUBMIT_BUTTON);
    }

    private WebElement getForm() {
        return driver.findElement(By.cssSelector(Locators.REGISTRATION_FORM.formatted(language)));
    }

    public RegistrationPage submit() {
        getSubmitButton().click();
        return this;
    }

    public RegistrationPage enterInAllTextFields(String value) {
        getForm().findElements(Locators.INPUT_TEXT).forEach(e -> e.sendKeys(value));
        return this;
    }

    public RegistrationPage enterEmail(String email) {
        getForm().findElement(Locators.EMAIL_INPUT).sendKeys(email);
        return this;
    }

    public RegistrationPage enterPassword(String password) {
        getForm().findElement(Locators.PASSWORD_INPUT).sendKeys(password);
        return this;
    }

    public RegistrationPage enterBirthDay(String birthDay) {
        Select select = new Select(getForm().findElement(Locators.BIRTH_DAY));
        select.selectByValue(birthDay);
        return this;
    }

    public RegistrationPage enterBirthMonth(String birthMonth) {
        Select select = new Select(getForm().findElement(Locators.BIRTH_MONTH));
        select.selectByValue(birthMonth);
        return this;
    }

    public RegistrationPage enterBirthYear(String birthYear) {
        Select select = new Select(getForm().findElement(Locators.BIRTH_YEAR));
        select.selectByValue(birthYear);
        return this;
    }

    public RegistrationPage injectInvalidBirthdayThroughJs(String birthDay) {
        js.executeScript("document.querySelector('" + Locators.BIRTH_DAY_CSS + "').value='" + birthDay + "'");
        return this;
    }

    public RegistrationPage injectInvalidBirthMonthThroughJs(String birthMonth) {
        js.executeScript("document.querySelector('" + Locators.BIRTH_MONTH_CSS + "').value='" + birthMonth + "'");
        return this;
    }

    public RegistrationPage injectInvalidBirthYearThroughJs(String birthYear) {
        js.executeScript("document.querySelector('" + Locators.BIRTH_YEAR_CSS + "').value='" + birthYear + "'");
        return this;
    }

    public boolean validationPresent(String message) {
        WebElement element = driver.findElement(By.xpath("//div[contains(@class, 'container')][.//*[@id='name']]"));
        return element.getText().contains(message);
    }
}
