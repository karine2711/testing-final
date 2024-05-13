package base;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import pages.HomePage;
import pages.RegistrationPage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Date;

import static util.Constants.SCREENSHOTS_FOLDER;


public class BaseTest {
    protected static WebDriver driver;
    protected static HomePage homePage;
    protected static RegistrationPage registrationPage;
    protected static String language;

    //todo: add language
    @Parameters({"language"})
    @BeforeClass
    public static void startDriver(@Optional("en") String lang) throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        driver = new ChromeDriver(options);
        language = lang;
        homePage = new HomePage(driver, language);
        registrationPage = new RegistrationPage(driver, language);
    }

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }

    protected void takeScreenshot(String testName) throws Exception {

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destination = new File(SCREENSHOTS_FOLDER, testName + "_" + new Date() + ".png");
        destination.createNewFile();
        try (var outputStream = new FileOutputStream(destination); var inputStream = new FileInputStream(srcFile)) {
            Files.copy(srcFile.toPath(), outputStream);
        }
    }

}
