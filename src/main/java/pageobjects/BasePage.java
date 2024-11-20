package pageobjects;

import Data.ScenarioContext;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.codehaus.groovy.reflection.stdclasses.FloatCachedClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage<T extends BasePage<T>> extends BaseComponent {

    private final By tableRowLoc = By.cssSelector("tbody tr");
    private final By cellValue = By.cssSelector("td");
    private final By inputLoc = By.tagName("input");

    protected final WebDriver driver;
    protected final ScenarioContext context;
    protected final Integer SHORT_WAIT = 2000;

    public BasePage (ScenarioContext context) {
        this.context = context;
        this.driver = context.getDriver();
    }

    public WebElement find (By locator) {
        return driver.findElement(locator);
    }

    public List<WebElement> findElements (By locator) {
        return driver.findElements(locator);
    }

    public void waitFor (ExpectedCondition<?> condition, Integer timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds((long)timeout));
        wait.until(condition);
    }

    public void click (WebElement element) {
        waitFor(ExpectedConditions.elementToBeClickable(element), SHORT_WAIT);
        element.click();
    }

    public void clear (By locator) {
        this.find(locator).clear();
    }

    public void type (String inputText, WebElement elemnts) {
        elemnts.sendKeys(inputText);
    }

    public void clearAndType (String text, By locator) {
        this.clear(locator);
        this.type(text, find(locator));
    }

    public void selectValueByText (By locator,String visibleText) {
        Select select = new Select(find(locator));
        select.selectByVisibleText(visibleText);
    }

    public String getText (By locator) {
        return find(locator).getText();
    }

    public WebElement getInputTableCell (String columValue) {
        for (WebElement element : findElements(tableRowLoc)) {
            if(element.findElements(cellValue).size() >1) {
                if (element.findElement(inputLoc).getAttribute("name").equalsIgnoreCase(columValue)) {
                    return find(inputLoc);
                }
            }
        }
        return null;
    }

    public void sleep (long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract BasePage isLoaded();
}
