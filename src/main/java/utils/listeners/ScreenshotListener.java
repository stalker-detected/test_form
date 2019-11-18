package utils.listeners;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import utils.DriverManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScreenshotListener extends TestListenerAdapter {
    private static String fileSeparator = System.getProperty("file.separator");
    private static String homeDir = System.getProperty("user.home") + fileSeparator + "failedTestResult";
    public static final String ANSI_RED = "\u001B[31m";//червоний колір
    public static final String ANSI_RESET = "\u001B[0m";//білий колір

    @Override
    public void onTestFailure(ITestResult tr) {

        System.out.println(ANSI_RED+"***** Error " + tr.getName() + " test has failed *****");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

        String testClassName = getTestClassName(tr.getInstanceName());
        String methodName = tr.getName() + df.format(calendar.getTime());
        String screenShotName = methodName + ".png";

        String imgPath = homeDir + fileSeparator + testClassName + fileSeparator
                + screenShotName;

        if (!tr.isSuccess()) {
            System.out.println("Screenshot can be found : " + imgPath+ANSI_RESET);

            File scrFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);

            File screenShotFile = new File(imgPath);
            try {
                FileUtils.copyFile(scrFile, screenShotFile);
            } catch (IOException e) {
                System.out.println("An exception occurred while taking screenshot " + e.getCause());
            }
        }
    }

    public String getTestClassName(String testName) {
        String[] testClassName = testName.split("\\.");
        int i = testClassName.length - 1;
        String n = testClassName[i];

        System.out.println("Required test name " + n);
        return n;
    }
}
