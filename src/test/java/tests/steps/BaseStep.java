//package tests.steps;
//
//import Data.ScenarioContext;
//import Utility.ScreenshotUtil;
//import io.cucumber.java.Scenario;
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.remote.RemoteWebDriver;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class BaseStep {
//
//    private WebDriver driver;
//    private ScenarioContext scenarioContext;
//    protected static Map<MapKeys, Object> map = new HashMap<>();
//
//    protected enum MapKeys {
//        FIRSTNAME("First Name"),
//        LASTNAME("Last Name"),
//        USERNAME("User Name"),
//        CUSTOMER("Customer"),
//        ROLE("Role"),
//        EMAIL("E-mail"),
//        CELLPHONE("Cell Phone"),
//        SCENARIO_CONTEXT("ScenarioContext");
//
//        private final String displayName;
//
//        MapKeys(String displayName) {
//            this.displayName = displayName;
//        }
//    }
//
//    private void launchDriver () {
//        WebDriverManager.chromedriver().setup();
//        ChromeOptions options = new ChromeOptions();
//        options.setAcceptInsecureCerts(true);
//        String url = "https://www.way2automation.com/angularjs-protractor/webtables/";
//        try {
//            this.driver = new RemoteWebDriver(options);
//            driver.get(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void beforeMethod (Scenario scenario) {
//        launchDriver();
//        this.scenarioContext = new ScenarioContext(driver, scenario);
//        scenarioContext.getDriver().manage().window().maximize();
//    }
//
//
//    public void afterMethod (Scenario scenario) {
//        if(scenario.isFailed()) {
//            ScreenshotUtil.captureScreenshot(this.scenarioContext, scenario.getName());
//        }
//        scenarioContext.getDriver().quit();
//    }
//}
