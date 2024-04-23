package StepDefinitions;

import com.Utilities.Constants;
import com.base.DriverManager;
import com.base.WebDriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.qameta.allure.testng.config.AllureTestNgConfig;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class SingleOne {

    private WebDriver driver;

    @Given("Google is open in the browser")
    public void google_is_open_in_the_browser() {
        driver = DriverManager.getWebDriverInstance();
        driver.get("https://www.google.com/");

        String message = "url launched : " + "https://www.google.com/";
//        Allure.addAttachment("screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        Allure.step(message, (step) -> {
            Allure.addAttachment("screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        });
    }



    @When("User searches for Test")
    public void user_searches_for_test() {

    }

    @When("User searches for Selenium cross browser testing")
    public void user_searches_for_Selenium_cross_browser_testing(){

    }

    @Then("Results are displayed")
    public void results_are_displayed() {
    }

}
