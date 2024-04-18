package TestRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CucumberOptions(tags = "",
        features= {"src/test/resources/Features"},//path to Features folder
        glue= {"StepDefinitions", "StepDefinitions1"},//path to StepDefinitions folder
plugin={"pretty", "html:test-output/htmlreport.html", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"})
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
