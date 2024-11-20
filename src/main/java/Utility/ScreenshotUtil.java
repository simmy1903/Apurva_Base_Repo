package Utility;

import Data.ScenarioContext;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;


public class ScreenshotUtil {

    public static void captureScreenshot (ScenarioContext scenarioContext, String fileName) {
        File source = ((TakesScreenshot) scenarioContext).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File("Screenshot" + fileName + ".png");
        try {
            FileUtils.copyFile(source, destinationFile);
        } catch (IOException e) {
            System.out.println("File didn't get capture");
        }
    }
}
