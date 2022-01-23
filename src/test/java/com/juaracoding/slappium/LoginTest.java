package com.juaracoding.slappium;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.juaracoding.slappium.page.HomePage;
import com.juaracoding.slappium.page.LoginPage;
import com.juaracoding.slappium.utils.Utils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class LoginTest {

    private final DesiredCapabilities caps = new DesiredCapabilities();
    private AndroidDriver<MobileElement> driver;
    ExtentReports extent;
	ExtentTest logger;
    private LoginPage loginPage;
    
    @BeforeTest
    public void init() {
    	extent = new ExtentReports (System.getProperty("user.dir") + "/Reporting/ExtentReport.html", true);
    }

    @BeforeSuite
    public void setupDeviceCapabilities() {
        caps.setCapability("deviceName", "Pixel_2_API_24");
//        caps.setCapability("uuid", "emulator-5554");
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "7.0");
        caps.setCapability("app", new File(System.getProperty("user.dir")
                + "/src/test/resources/loginApp.apk").getAbsolutePath());
        caps.setCapability("appPackage", "com.maxsoftlk.loginapplication");
        caps.setCapability("appActivity", "com.maxsoftlk.loginapplication.MainActivity");
        caps.setCapability("noReset", true);
        caps.setCapability("fullReset", false);
    }

    @BeforeMethod
    public void spinUpAndroidDriver() {
        try {
            driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        loginPage = new LoginPage(driver);
    }

    @Test(description = "Verify that a user can login to the application with valid credentials")
    public void test01ValidLogin() {
    	logger = extent.startTest("01 Test Valid Login", "Description");
        loginPage.login("Osanda", "MaxSoft123");
        HomePage homePage = new HomePage(driver);
        Assert.assertEquals(homePage.getGreetingLabelText(), "Hi Osanda,");
        Assert.assertEquals(homePage.getWelcomeLabelText(), "Welcome!");
        logger.log(LogStatus.PASS, "Verify that a user can login to the application with valid credentials");
    }

    @Test(description = "Verify that a user cannot login to the application with invalid credentials")
    public void test02InvalidLogin() {
    	logger = extent.startTest("02 Test InValid Login", "Description");
        loginPage.login("Osanda", "MaxSoft1234");
        Assert.assertEquals(loginPage.getAttemptsCounterLabelText(), "Number of attempts remaining: 5");
        if(loginPage.getAttemptsCounterLabelText().contains("Number of attempts remaining: 5")) {
        	logger.log(LogStatus.PASS, "Verify that a user cannot login to the application with invalid credentials");
        } else {
        	logger.log(LogStatus.FAIL, "Test Failed");
        }
    }

    @AfterMethod
    public void getResult(ITestResult result) throws Exception {
    	if(result.getStatus() == ITestResult.FAILURE)
        {
            String screenShotPath = Utils.capture(driver, "screenShotName");
//            logger.log(LogStatus.FAIL, result.getThrowable());
            logger.log(LogStatus.FAIL, "Snapshot below: " + logger.addScreenCapture(screenShotPath));
        }
        extent.endTest(logger);
    }
    
    @AfterTest
    public void endreport()
    {
        extent.flush();
        extent.close();
    }
}
