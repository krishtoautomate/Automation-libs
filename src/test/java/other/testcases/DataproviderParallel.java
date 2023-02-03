package other.testcases;

import com.DataManager.DeviceInfoReader;
import com.base.UdidMapper;
import com.ReportManager.ExtentTestManager;
import com.Utilities.ITestBase;
import com.Utilities.Utilities;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.AppiumDriverManager;
import com.base.TestBaseDeeplinks;
import com.opencsv.CSVReader;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import other.pages.PlayStoreApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class DataproviderParallel extends TestBaseDeeplinks implements ITestBase {

    String className = this.getClass().getSimpleName();

    @Test(dataProvider = "DeepLinksDataProvider", groups = {"DeepLinks"})
    public void PlaystoreUpdateScript(Map<String, String> data, ITestContext iTestContext) throws IOException, ParseException {

        //---start
        String udid = data.get("udid");
        iTestContext.setAttribute("udid", udid);
        UdidMapper.setUdid(udid);
        DataProviderParallelHelper(data, iTestContext);
        //--end

        String p_testKey = isAndroid ? data.get("AndroidTestKey") : data.get("IOSTestKey");

        log.info("TestKey : " + p_testKey);

        test.getModel().setName(String.format("%s", className));

        Utilities utils = new Utilities(driver, test);
        PlayStoreApp playstoreapp = new PlayStoreApp(driver, test);

        sleep(2);

        // 1.0 - Click navigate button
        playstoreapp.get_accountLogo().click();
        utils.logmessage(Status.PASS, "Right Account logo Button clicked");

        sleep(2);

        if (playstoreapp.verify_manageAppsAndDevice_btn()) {
            playstoreapp.get_manageAppsAndDevice_btn().click();
            utils.logmessage(Status.PASS, "'Manage apps and device' link clicked");
        }

        sleep(2);

        // 3.0 -
        if (playstoreapp.verify_updates_refresh_btn()) {
//      playstoreapp.get_updates_refresh_btn().click();
            utils.logmessage(Status.PASS, "'updates refresh Button' is clicked");
        }

        // 4.0 - Click Update All button
        if (playstoreapp.verify_updateAll_btn()) {
            playstoreapp.get_updateAll_btn().click();
            sleep(3);
            utils.logmessage(Status.PASS, "'UPDATE ALL' button clicked");
        }
//    sleep(120);
    }

    public void DataProviderParallelHelper(Map<String, String> data,ITestContext iTestContext) throws IOException, ParseException {
        /*
         * Test info
         */

        String udid = data.get("udid");
        UdidMapper.setUdid(udid);
        iTestContext.setAttribute("udid",udid);
        tlDriverFactory.setDriver(iTestContext);
        driver = AppiumDriverManager.getDriverInstance();

        if ("Auto".equalsIgnoreCase(udid)) {
            udid = driver.getCapabilities().getCapability("udid").toString();
        }

        DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
        String deviceName = deviceInfoReader.getString("name");

        udid = driver.getCapabilities().getCapability("udid").toString();

        String platformVersion = deviceInfoReader.getString("platformVersion");

        // Report Content
        test = ExtentTestManager.startTest(className)
                .assignDevice(deviceName);

        log.info("Test Details : " + className);
        String[][] info = {{"<b>TestCase : </b>", className}, {"<b>Device : </b>", deviceName},
                {"<b>UDID : </b>", udid}, {"<b>Platform : </b>", isAndroid ? "Android" : "iOS"},
                {"<b>OsVersion : </b>", platformVersion}};

        test.info(MarkupHelper.createTable(info));

    }

    // ****Deep Link Data Provider *****
    @DataProvider(parallel = true)
    public synchronized Iterator<Object[]> DeepLinksDataProvider(ITestContext context) {
        List<Object[]> list = new ArrayList<Object[]>();
        String pathname = context.getCurrentXmlTest().getParameter("p_Testdata");

        File file = new File(pathname);

        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] keys = reader.readNext();
            if (keys != null) {
                String[] dataParts;
                while ((dataParts = reader.readNext()) != null) {
                    Map<String, String> testData = new HashMap<String, String>();
                    for (int i = 0; i < keys.length; i++) {
                        if (keys[i].equalsIgnoreCase("AndroidTestKey") || keys[i].equalsIgnoreCase("IOSTestKey")) {
                            context.setAttribute("testKey", dataParts[i]);
//                            log.info("Jira-TestKey : "+ keys[i] + ":"+dataParts[i]);
                        }
                        if (keys[i].equalsIgnoreCase("udid")) {
                            context.setAttribute("udid", dataParts[i]);
                        }
                        testData.put(keys[i], dataParts[i]);
                    }
                    list.add(new Object[]{testData});
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(
                    "File :" + pathname + " was not found. \n" + e.getStackTrace().toString());
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not read" + pathname + "file.\n" + e.getStackTrace().toString());
        }
        return list.iterator();
    }
}
