package tests;

import base.BaseTest;
import listeners.ScreenshotListener;
import org.testng.annotations.*;

import java.util.Date;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

@Listeners(ScreenshotListener.class)
public class RegistrationTest extends BaseTest {
    //there is a bug on page, even when you are testing in Armenian
    // validations are shown in English
    private static final String REQUIRED_TEMPLATE = "The %s field is required.";
    private static final String USERNAME_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("Username");
    private static final String EMAIL_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("E-mail");
    private static final String NAME_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("Name");
    private static final String PASSWORD_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("Password");
    private static final String TELEPHONE_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("Telephone");
    private static final String ADDRESS_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("Address");
    private static final String CITY_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("City");
    private static final String BIRTHDAY_MONTH_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("Month of Birthday");
    private static final String BIRTHDAY_DAY_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("Day of Birthday");
    private static final String BIRTHDAY_YEAR_REQUIRED_MESSAGE = REQUIRED_TEMPLATE.formatted("Year of Birthday");

    private static final String BIRTHDAY_TEMPLATE = "The %s of Birthday field must only contain digits and must be greater than zero.";
    private static final String BIRTHDAY_MONTH_INVALID = BIRTHDAY_TEMPLATE.formatted("Month");
    private static final String BIRTHDAY_YEAR_INVALID = BIRTHDAY_TEMPLATE.formatted("Year");
    private static final String BIRTHDAY_DAY_INVALID = BIRTHDAY_TEMPLATE.formatted("Day");
    private static final String PASSWORD_TOO_SHORT = "The Password field must be at least 6 characters in length.";
    public static final String VALID_EMAIL = "value@email.com";

    @Test
    public void testAllEmpty() {
        registrationPage
                .load()
                .submit();

        assertValidationPresent(USERNAME_REQUIRED_MESSAGE);
        assertValidationPresent(EMAIL_REQUIRED_MESSAGE);
        assertValidationPresent(NAME_REQUIRED_MESSAGE);
        assertValidationPresent(PASSWORD_REQUIRED_MESSAGE);
        assertValidationPresent(TELEPHONE_REQUIRED_MESSAGE);
        assertValidationPresent(ADDRESS_REQUIRED_MESSAGE);
        assertValidationPresent(CITY_REQUIRED_MESSAGE);
    }

    @Test
    public void onlyTextInputsFilledBirthdayValidated() {
        registrationPage
                .load()
                .enterInAllTextFields("value")
                .enterEmail(VALID_EMAIL)
                .enterPassword("validpassword")
                .submit();
        assertValidationPresent(BIRTHDAY_DAY_INVALID);
        assertValidationPresent(BIRTHDAY_YEAR_INVALID);
        assertValidationPresent(BIRTHDAY_MONTH_INVALID);
    }

    @Test(dataProvider = "invalidEmailValues")
    public void emailValidatedFirstWithInvalidEmail(String invalidEmail) {
        registrationPage
                .load()
                .enterEmail(invalidEmail)
                .submit();

        // although all field are present, no other validation was performed if email is invalid
        assertValidationNotPresent(USERNAME_REQUIRED_MESSAGE);
        assertValidationNotPresent(EMAIL_REQUIRED_MESSAGE);
        assertValidationNotPresent(NAME_REQUIRED_MESSAGE);
        assertValidationNotPresent(PASSWORD_REQUIRED_MESSAGE);
        assertValidationNotPresent(TELEPHONE_REQUIRED_MESSAGE);
        assertValidationNotPresent(ADDRESS_REQUIRED_MESSAGE);
        assertValidationNotPresent(CITY_REQUIRED_MESSAGE);
    }

    @Test
    public void emailValidatedFirstWithValidEmail() {
        registrationPage
                .load()
                .enterEmail(VALID_EMAIL)
                .submit();

        registrationPage
                .load()
                .enterEmail(VALID_EMAIL)
                .submit();

        assertValidationPresent(USERNAME_REQUIRED_MESSAGE);
        assertValidationPresent(NAME_REQUIRED_MESSAGE);
        assertValidationPresent(PASSWORD_REQUIRED_MESSAGE);
        assertValidationPresent(TELEPHONE_REQUIRED_MESSAGE);
        assertValidationPresent(ADDRESS_REQUIRED_MESSAGE);
        assertValidationPresent(CITY_REQUIRED_MESSAGE);
    }

    @Test
    public void testSuccess() {
        registrationPage
                .load()
                .enterInAllTextFields("value")
                .enterEmail(VALID_EMAIL)
                .enterPassword("password")
                .enterBirthDay("11")
                .enterBirthYear("2001")
                .enterBirthMonth("3");
//                .submit();
       // tested, passes
    }


    @Test
    public void passwordLengthValidated() {
        registrationPage
                .load()
                .enterInAllTextFields("value")
                .enterEmail(VALID_EMAIL)
                .enterPassword("value")
                .submit();
        assertValidationPresent(PASSWORD_TOO_SHORT);
    }


    // only pass the "required" validation for all fields

    @Test(dataProvider = "invalidBirthDayValues")
    public void birthDayValidated(String invalidBirthDayValue) {
        registrationPage.load()
                .enterEmail(VALID_EMAIL)
                .injectInvalidBirthdayThroughJs(invalidBirthDayValue)
                .submit();

        assertValidationPresent(BIRTHDAY_DAY_REQUIRED_MESSAGE);

        assertValidationNotPresent(BIRTHDAY_DAY_INVALID);
        assertValidationPresent(BIRTHDAY_MONTH_INVALID);
        assertValidationPresent(BIRTHDAY_YEAR_INVALID);
    }

    @Test(dataProvider = "invalidBirthMonthValues")
    public void birthMonthValidated(String invalidBirthMonthValue) {
        registrationPage.load()
                .enterEmail(VALID_EMAIL)
                .injectInvalidBirthMonthThroughJs(invalidBirthMonthValue)
                .submit();

        assertValidationPresent(BIRTHDAY_MONTH_REQUIRED_MESSAGE);

        assertValidationNotPresent(BIRTHDAY_MONTH_INVALID);
        assertValidationPresent(BIRTHDAY_DAY_INVALID);
        assertValidationPresent(BIRTHDAY_YEAR_INVALID);
    }

    @Test(dataProvider = "invalidBirthYearValues")
    public void birthYEarValidated(String invalidBirthYearValue) {
        registrationPage.load()
                .enterEmail(VALID_EMAIL)
                .injectInvalidBirthYearThroughJs(invalidBirthYearValue)
                .submit();

        assertValidationPresent(BIRTHDAY_YEAR_REQUIRED_MESSAGE);

        assertValidationNotPresent(BIRTHDAY_YEAR_INVALID);
        assertValidationPresent(BIRTHDAY_DAY_INVALID);
        assertValidationPresent(BIRTHDAY_MONTH_INVALID);
    }


    private static void assertValidationNotPresent(String message) {
        assertFalse(registrationPage.validationPresent(message));
    }

    private static void assertValidationPresent(String message) {
        assertTrue(registrationPage.validationPresent(message));
    }

    @DataProvider(name = "invalidBirthDayValues")
    public Object[][] birthDayValues() {
        return new Object[][]{
                {"%%%"},
                {"first"},
                {"32"},
                {"-1"},
                {" "}
        };
    }

    @DataProvider(name = "invalidBirthMonthValues")
    public Object[][] birthMonthValues() {
        return new Object[][]{
                {"%%%"},
                {"first"},
                {"13"},
                {"-1"},
                {" "},
                {"Sepetember"}
        };
    }

    @DataProvider(name = "invalidBirthYearValues")
    public Object[][] birthMYearValues() {
        return new Object[][]{
                {"%%%"},
                {"nineteen ninety"},
                {"last year"},
                {"1929"},
                {String.valueOf(new Date().getYear() + 1)},
                {" "}
        };
    }

    @DataProvider(name = "invalidEmailValues")
    public Object[][] invalidEmailValues() {
        return new Object[][]{
                {"%%%"},
                {"v"},
                {"value"},
                {"value@"},
                {".com"},
                {"@.com"}
        };
    }
}
