import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.testng.annotations.*;
import pages.SurveyCompletedPO;
import pages.TestFormPO;
import utils.listeners.ScreenshotListener;


import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import static pages.BasePO.getSessionID;
import static utils.DriverManager.getDriver;

@Listeners({ScreenshotListener.class})
public class BaseTest {

    protected TestFormPO testForm = new TestFormPO();
    protected SurveyCompletedPO surveyCompleted = new SurveyCompletedPO();

    private static StopWatch stopWatch = new StopWatch();

    private static final String ANSI_YELLOW = "\u001B[33m";//жовтий колір
    private static final String ANSI_RESET = "\u001B[0m";//білий колір

    @BeforeSuite
    public void setScreenCap() {
        getDriver().manage().window().setSize(new Dimension(1920, 1080));
        Point newPoint = new Point(0, 0);
        getDriver().manage().window().setPosition(newPoint);
        System.out.println("ID цієї сесії = "+getSessionID());
    }

    //вивожу перекрашені назви тестів з часом їх запуску
    @BeforeMethod(alwaysRun = true)
    public void startNewTest(Method method) {
        System.out.println(ANSI_YELLOW+new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())+" Старт теста: "+method.getName()+" з класа "+this.getClass().getName()+" ("+stopWatch.toString()+")"+ANSI_RESET);

    }

    @AfterMethod
    public void postCondAfterMethod() {
        getDriver().manage().deleteAllCookies();
    }

    @AfterTest
    public void afterTest() {

        if (getDriver() != null)
            getDriver().quit();
    }

    //закрити Alert
    public void acceptAlert(){
        try {
            Alert alert = getDriver().switchTo().alert();
            alert.accept();
        }catch (NoAlertPresentException e){}
    }

    //обновити сторінку
    protected static void refreshPage(){
        getDriver().navigate().refresh();
    }

    //перевірка що алерт відкрився
    public Boolean alertIsVisible(){
        try {
            getDriver().switchTo().alert();
            return true;
        }catch (NoAlertPresentException e){
            return false;
        }
    }
}
