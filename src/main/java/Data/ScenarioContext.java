package Data;

import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

public class ScenarioContext {

    private final WebDriver driver;
    private final Scenario scenario;


    public ScenarioContext (WebDriver driver, Scenario scenario) {
        this.driver = driver;
        this.scenario = scenario;
    }

    public WebDriver getDriver () {
        return driver;
    }

    public Scenario getScenario () {
        return scenario;
    }

}
