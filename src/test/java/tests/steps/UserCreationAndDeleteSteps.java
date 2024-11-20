package tests.steps;

import Data.ScenarioContext;
import Utility.ScreenshotUtil;
import com.github.javafaker.Faker;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.eo.Se;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import pageobjects.Way2Automation;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static tests.steps.UserCreationAndDeleteSteps.MapKeys.LASTNAME;


public class UserCreationAndDeleteSteps {

//    private ScenarioContext context;
    private final Faker faker;
    private WebDriver driver;
    private ScenarioContext scenarioContext;
    protected static Map<MapKeys, Object> map = new HashMap<>();

    public UserCreationAndDeleteSteps () {
        this.faker = new Faker();
    }



    protected enum MapKeys {
        FIRSTNAME("First Name"),
        LASTNAME("Last Name"),
        USERNAME("User Name"),
        CUSTOMER("Customer"),
        ROLE("Role"),
        EMAIL("E-mail"),
        CELLPHONE("Cell Phone"),
        SCENARIO_CONTEXT("ScenarioContext");

        private final String displayName;

        MapKeys(String displayName) {
            this.displayName = displayName;
        }
    }

    private void launchDriver () {
//        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        options.setAcceptInsecureCerts(true);
        String url = "https://www.way2automation.com/angularjs-protractor/webtables/";
        try {
//            this.driver = new RemoteWebDriver(options);
            this.driver = new RemoteWebDriver(new URL("http://localhost:56795"), options);
            driver.get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void before (Scenario scenario) {
        launchDriver();
        this.scenarioContext = new ScenarioContext(driver, scenario);
        scenarioContext.getDriver().manage().window().maximize();
    }


    @After
    public void after (Scenario scenario) {
        if(scenario.isFailed()) {
            ScreenshotUtil.captureScreenshot(this.scenarioContext, scenario.getName());
        }
        scenarioContext.getDriver().quit();
    }

    @Given("launch the URL")
    public void launch_the_url() {

    }

    @When("the user tries to add new user")
    public void the_user_tries_to_add_new_user() {
        Way2Automation page = new Way2Automation(scenarioContext);
        map.get(MapKeys.SCENARIO_CONTEXT);

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String userName = firstName+"."+lastName;
        map.put(MapKeys.USERNAME, userName);
        map.put(MapKeys.FIRSTNAME, firstName);
        map.put(MapKeys.LASTNAME, lastName);

        page.clickOnAddUserButton().enterFirstName(firstName)
                .enterLastName(lastName)
                .enterUserName(userName)
                .enterPassword(faker.internet().password())
                .selectCustomerCompany("Company AAA")
                .enterRole("Customer")
                .enterEmail(userName+"@example.com")
                .enterCellPhone(String.valueOf(faker.phoneNumber()))
                .clickOnSaveButton();
        page.sleep(3000);
    }

    @Then("user is added")
    public void user_is_added() {
    }

}
