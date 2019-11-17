package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static utils.DriverManager.getDriver;
import static utils.DriverManager.waitForPageLoad;
import static utils.PropertiesLoader.getProp;

public abstract class BasePO {



    private final String REL_URL;
    private static String BASE_URL;



    public BasePO(String url) {
        REL_URL = url;
        BASE_URL = getProp("baseUrl") == null ? "Base URL is not stetted!" : getProp("baseUrl");
    }

    private BasePO open(String url) {
        getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            getDriver().get(url);
        return this;
    }

    private static SessionId session = ((RemoteWebDriver)getDriver()).getSessionId();


    //получити ID сессії
    public static String getSessionID(){
        return session.toString();
    }



    public BasePO open() {
        String s = BASE_URL + REL_URL;
        System.out.println(s);
        open(s);
        return this;
    }


    public static WebElement waitUntilElementIsClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 30,100);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitUntilElementIsClickable(By by) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 30);
        WebElement el = getDriver().findElement(by);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"site-preloader\"]")));
        return wait.until(ExpectedConditions.elementToBeClickable(el));
    }

    public static void waitUntilElementIsNotDisplayed(By by) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 30);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public static WebElement waitUntilElementIsDisplayed(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 30);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    public static WebElement waitUntilElementIsDisplayed(By by) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 30);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }


    //очікування поки URL буде містити правильний URL
    public static void waitUntilUrlContainText(String url) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 30);
        wait.until(ExpectedConditions.urlContains(url));
    }

    public static void waitForTheTextToBecomeEqual(WebElement element, String text) {
        WebDriverWait wait = new WebDriverWait(getDriver(),20);
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    //якщо елемент існує, то метод повертає true(By)
    public static boolean existsElement(By by) {
        getDriver().manage().timeouts().implicitlyWait(1,
                TimeUnit.SECONDS);
        try {
            getDriver().findElement(by);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    //якщо елемент існує, то метод повертає true(WebElement)
    public static boolean existsElement(WebElement element) {
        getDriver().manage().timeouts().implicitlyWait(1,
                TimeUnit.SECONDS);
        try {
            element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }


    //клік на елементі(By)
    public static void clickElement(By by) {
        try {
            getDriver().findElement(by).click();
        } catch (WebDriverException e) {
            try {
                WebElement el = getDriver().findElement(by);
                JavascriptExecutor executor = (JavascriptExecutor) getDriver();
                executor.executeScript("arguments[0].click()", el);
            }  catch (StaleElementReferenceException r){
                clickElement(by);
            }
        }
    }
    //клік на елементі(WebElement)
    public static void clickElement(WebElement element) {
        try {
            element.click();
        } catch (WebDriverException e) {
            try{
                JavascriptExecutor executor = (JavascriptExecutor) getDriver();
                executor.executeScript("arguments[0].click()", element);}
            //отримую локатор з елемента, заново шукаю і клікаю
            catch (StaleElementReferenceException r){
                if(getLocatorFromWebElement(element).get(0).equals("xpath")){
                    clickElement(By.xpath(getLocatorFromWebElement(element).get(1)));
                    System.out.println("Exception clickElement");
                } else{
                    clickElement(By.cssSelector(getLocatorFromWebElement(element).get(1)));
                    System.out.println("Exception clickElement");
                }
            }
        }
    }


    public Boolean isOpen() {
        waitForJSandJQueryToLoad();
        String expUrl = BASE_URL+REL_URL;
        String actUrl = getDriver().getCurrentUrl();
        if (!expUrl.equalsIgnoreCase(actUrl)) {
            System.out.println("Expected URL: " + expUrl);
            System.out.println("Actual URL: " + actUrl);
            return false;
        }
        return true;
    }

    public static String getElementText(By by) {
        return DriverManager.getDriver()
                .findElement(by)
                .getText();
    }



    public static boolean waitForJSandJQueryToLoad() {

        WebDriverWait wait = new WebDriverWait(getDriver(), 30);

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long)((JavascriptExecutor)getDriver()).executeScript("return jQuery.active") == 0);
                }
                catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor)getDriver()).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad); }


    //повертає By локатор з WebElement
    private static ArrayList<String> getLocatorFromWebElement(WebElement element) {
        ArrayList<String> list = new ArrayList<>();
        String str = element.toString();
        //тип локатора(xPath чи CssSelector)
        list.add(str.substring(str.lastIndexOf("->")+2,str.lastIndexOf(":")).replaceAll("\\s+",""));
        //сам локатор
        list.add(str.substring(str.lastIndexOf(":")+1, str.length()-1));
        return list; }

}

