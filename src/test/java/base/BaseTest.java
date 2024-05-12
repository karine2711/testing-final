package base;


import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import pages.HomePage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Date;

import static util.Constants.SCREENSHOTS_FOLDER;


public class BaseTest {
    protected static WebDriver driver;
    protected static HomePage homePage;

    //todo: add language
    @Parameters({"browser"})
    @BeforeClass
    public static void startDriver(@Optional("local") String browser) throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        driver = new ChromeDriver(options);
        homePage = new HomePage(driver);
    }

    @AfterClass
    public static void quitDriver()  {
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
