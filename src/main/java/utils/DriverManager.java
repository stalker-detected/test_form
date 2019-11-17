package utils;

import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static utils.PropertiesLoader.getProp;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private static String browserName = System.getProperty("browser");
    private static String browserType = System.getProperty("driverType");

    public static WebDriver getDriver() {
        if (driver.get() == null)
            setDriver();
        return driver.get();
    }


    private static void setDriver() {
        try {
            DriverManager.driver.set(configureDriver(init()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Remote URL is not set");
        }
    }



    private static WebDriver init() throws MalformedURLException {
        if (browserName != null) {
            System.out.println("Current browser is: " +
                    browserName.toUpperCase());
        } else {
            browserName = getProp("browser");
            System.out.println("Browser is not stated. Default stated browser is: " +
                    browserName.toUpperCase());
        }

        if (browserType==null){
            browserType = getProp("driverType");
        }

        if (browserType.equalsIgnoreCase("local")) {
            switch (browserName) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("start-maximized");

                    return new ChromeDriver(chromeOptions);

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    return new FirefoxDriver(firefoxOptions);

            }
            return new ChromeDriver();
        } else
            {
            //запуск remote

            }
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");

        return new ChromeDriver(chromeOptions);
        }



    private static WebDriver configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(new Long(getProp("implicitWait")),
                TimeUnit.SECONDS);
        return driver;
    }

    public static void waitForPageLoad(int t) {
        Wait<WebDriver> wait = new WebDriverWait(getDriver(), t);
        wait.until(driver -> {
            ((JavascriptExecutor) driver).executeScript("return document.readyState");
            return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                    .equals("complete");

        });
    }

    public static void waitForPageLoad() {
        waitForPageLoad(0);
    }


}
