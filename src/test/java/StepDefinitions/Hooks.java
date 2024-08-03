package StepDefinitions;

import com.DataManager.TestDataManager;
import com.base.DriverManager;
import com.base.GlobalMapper;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Map;

public class Hooks {

    public WebDriver driver;
    protected DriverManager tlDriverFactory = new DriverManager();

    @Before
    public void setUp(){
//        String className = this.getClass().getName();
//        String methodName = method.getName();

//        Map<String, String> testParams = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getAllParameters();
//        String platForm =  testParams.get("platForm");
//        String pTestData = testParams.get("p_Testdata");
//        TestDataManager testData = new TestDataManager(pTestData);
//        try {
//            JSONObject jObject = (JSONObject) new JSONParser().parse(new FileReader(pTestData));
//            org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) jObject.get(className);
//            int index = "iOS".equalsIgnoreCase(platForm) ? 1 : 0;
//            JSONObject jObjt = (JSONObject) jsonArray.get(index);
//            JSONObject jAObject = (JSONObject) jObjt.get("parameters");
//            platForm = jAObject.get("platForm").toString();
//        } catch (Exception e) {
//            //ignore
//        }
//
//        // Set & Get ThreadLocal Driver with Browser
//        iTestContext.setAttribute("platform", "Web");
//
//        if(platForm!=null){
//            if("Desktop".equalsIgnoreCase(platForm)){
//                tlDriverFactory.setDriver("Web");
//                driver = DriverManager.getWebDriverInstance();
//            }else {
//                if (udid != null)
//                    GlobalMapper.setUdid(udid);
//
//                tlDriverFactory.setDriver("Appium-Browser");
//                driver = DriverManager.getAppiumDriverInstance();
//            }
//        }else{
            tlDriverFactory.setDriver("Web");
            driver = DriverManager.getWebDriverInstance();
//        }
    }

    @AfterClass
    public void tearDown(Scenario scenario){
//        System.out.println("Execution after every scenario");

        driver = DriverManager.getWebDriverInstance();

        if(driver!=null){
            if (scenario.isFailed()) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
            }

            try {
                driver.close();
            } catch (Exception e) {
                //ignore
            }finally{
                driver.quit();
            }
        }


    }

//    @Before("@CaseOne or @CaseTwo")
//    public void before(){
//        System.out.println("This will be executed before case one and two");
//    }
//
//    @After("@CaseOne or @CaseTwo")
//    public void after(){
//        System.out.println("This will be executed after case one and two");
//    }

    @AfterStep
    public void afterStep() {
        Allure.addAttachment("screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

}
