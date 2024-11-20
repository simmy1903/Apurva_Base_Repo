package pageobjects;

import Data.ScenarioContext;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class Way2Automation extends BasePage {

    private final By addUserLoc = By.cssSelector("i.icon.icon-plus");
    private final By modal = By.cssSelector("div.modal-header");
    private final By saveButton = By.cssSelector("button.btn.abtn-success");
    private final By roleLoc = By.xpath("//select[@name='RoleId']");
    private final String companyNameLoc = "//label[text()='"+ REGEX+"'] //input";
    private static final String REGEX = "ELEMENT1";

    public Way2Automation(ScenarioContext context) {
        super(context);
    }

    @Override
    public Way2Automation isLoaded () {
        sleep(3000);
        return this;
    }

    public Way2Automation clickOnAddUserButton () {
        click(find(addUserLoc));
        waitFor(ExpectedConditions.visibilityOfElementLocated(modal), SHORT_WAIT);
        return this;
    }

    public Way2Automation enterFirstName (String firstName) {
        type(firstName, getInputTableCell("FirstName"));
        return this;
    }

    public Way2Automation enterLastName (String lastName) {
        type(lastName, getInputTableCell("LastName"));
        return this;
    }

    public Way2Automation enterUserName (String userName) {
        type(userName, getInputTableCell("UserName"));
        return this;
    }

    public Way2Automation enterPassword (String password) {
        type(password, getInputTableCell("Password"));
        return this;
    }

    public Way2Automation enterEmail (String email) {
        type(email, getInputTableCell("Email"));
        return this;
    }

    public Way2Automation enterCellPhone (String cellPhone) {
        type(cellPhone, getInputTableCell("Mobilephone"));
        return this;
    }

    public Way2Automation clickOnSaveButton () {
        click(find(saveButton));
        return this;
    }

    public Way2Automation enterRole (String roleName) {
        selectValueByText(roleLoc, roleName);
        return this;
    }

    public Way2Automation selectCustomerCompany (String companyName) {
        click(find(getDynamicXpath(companyNameLoc, companyName)));
        return this;
    }

    private By getDynamicXpath (String oldString, String elementName) {
        return By.xpath(oldString.replaceAll("ELEMENT1", elementName));
    }


}
