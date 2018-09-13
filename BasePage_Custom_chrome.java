package com.rdz.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.rdz.utilities.ExcelReader;
import com.rdz.utilities.ExtentManager;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.net.MalformedURLException;
import java.net.URL;

public class BasePage {

/**@author ajay.kumar4
 */
	
	
	
	// Variable declarations..
	public static FileInputStream fis;
	public static Properties config = new Properties();
	public static Logger log = Logger.getLogger("devpinoyLogger");
	public static ExtentTest test;
	public ExtentReports rep = ExtentManager.getInstance();
    private  WebDriver driver;
    public static ExcelReader excel = new ExcelReader(
	System.getProperty("user.dir") + "\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;
	public static String browsername;
	
	
	
	//Saucelab env. Optional
	public static final String USERNAME = "ajaygzb18";
	public static final String ACCESS_KEY = "08c76b17-4bca-4d68-bea6-8bcddf2c7d0a";
	public static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";
	
	
	
	
	
	
     
    @BeforeSuite
	public void setUp() {

		if (driver == null) {

			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\resources\\properties\\Config.properties");
			} catch (FileNotFoundException e) {
				log.debug("Config file Not Found !!!");
				e.printStackTrace();
			}
			try {
				config.load(fis);
				log.debug("Config file loaded !!!");
			} catch (IOException e) {
				log.debug("Config file Not loaded !!!");
				e.printStackTrace();
			}
		}

    }   


 @Parameters("browser")
 @BeforeTest
 public void beforeTest(String browser) {
	 
	 
	 
          if(browser != null && !browser.isEmpty()){
          System.out.println("Getting Browser name from TestNG XML:  "+browser);
    	  browserLaunch(browser,config.getProperty("url")); 
    	  browsername=browser;
    	  
    	  
      }else{
    	  
    	  System.out.println("Getting Browser name from config.   "+browser);
    	  browserLaunch(config.getProperty("browser"),config.getProperty("url"));   
    	  browsername=browser;
      }
	 
	 
	  
	 
	
 }
 
 @AfterTest
	public void Closedriver() {

		if (driver != null) {
			driver.close();
		}

		log.debug("browser closed called !!!");
	
	}
 
 @AfterMethod
 public void getResult(ITestResult result) throws IOException
 {
     if (result.getStatus() == ITestResult.FAILURE)
     {
         String screenShotPath = capture(driver, "screenShot"+result.getName());
         test.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" Test case FAILED due to below issues:", ExtentColor.RED));
         test.fail(result.getThrowable());
         test.fail("Snapshot below: ").addScreenCaptureFromPath(screenShotPath);
         //test.fail("Snapshot below: ", MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
     }
     else if(result.getStatus() == ITestResult.SUCCESS)
     {
         test.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
     }
     else
     {
         test.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+" Test Case SKIPPED", ExtentColor.ORANGE));
         test.skip(result.getThrowable());
     }
     rep.flush();    
 }
 
 
 
    @AfterSuite
	public void tearDown() {

		if (driver != null) {
			try{driver.quit();
			}
			catch(Exception e){
				
			}
		}

		log.debug("test execution completed !!!");
	}
    

 
	

    
    // *******************************Common Methods*******************************
    
    public void browserLaunch(String browser,String url){
   	 
    	if(browser.equalsIgnoreCase("firefox")) {
    		
    		WebDriverManager.firefoxdriver().setup();
    		if (driver == null){
    		driver = new FirefoxDriver();
    		log.debug("Firefox Launched !!!");
    		}
    		
    		
    		
    	}else if(browser.equalsIgnoreCase("chrome")){

    		WebDriverManager.chromedriver().setup();
    		if (driver == null){
    		//String userProfile= "C:\\Users\\ajay.kumar4\\AppData\\Local\\Google\\Chrome\\User Data\\";
    		ChromeOptions options = new ChromeOptions();
        	//options.addArguments("user-data-dir="+userProfile);
    		options.addArguments("--start-maximized");
    		options.addArguments("chrome.switches", "--disable-extensions");
    		options.addArguments("--disable-notifications");
    		options.addArguments("disable-infobars");
    		driver = new ChromeDriver(options);
    		log.debug("Chrome Launched !!!");
    		}
    		
    		
    	}else if(browser.equalsIgnoreCase("IE")){

    		WebDriverManager.iedriver().setup();
    		if (driver == null){
    		driver = new InternetExplorerDriver();
    		log.debug("IE Launched !!!");
    		}
    		
    		
    		
    	}else if(browser.equalsIgnoreCase("cloud")){
    		

    	    DesiredCapabilities caps = DesiredCapabilities.chrome();
    	    caps.setCapability("platform", "Windows 10");
    	    caps.setCapability("version", "latest");
    	 
    	    try {
    		  driver = new RemoteWebDriver(new URL(URL), caps);
    		} catch (MalformedURLException e) {
    			System.out.println("Unable to load remote web driver server");
    			e.printStackTrace();
    		}
    		log.debug("Cloud remote webdriver Launched !!!");
    	}
    	 driver.manage().window().maximize();
    	 driver.get(url);
    	 log.debug("Navigated to : " + url);
    	// driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")),TimeUnit.SECONDS);
    	 //wait = new WebDriverWait(driver, 5);
    	 setDriver(driver);
    	 getDriver();
    	 
    	 
     }
    
	public  void verifyEquals(String expected, String actual) throws IOException {

		try {

			Assert.assertEquals(actual, expected);

		} catch (Throwable t) {

			
			// ReportNG
			Reporter.log("<br>" + "Verification failure : " + t.getMessage() + "<br>");
			Reporter.log("<br>");
			Reporter.log("<br>");
			// Extent Reports
			test.log(Status.ERROR, " Verification failed with exception : " + t.getMessage());
		}

	}
	
	public boolean isElementPresent(By by) {

		try {

			driver.findElement(by);
			return true;

		} catch (NoSuchElementException e) {

			return false;

		}

	}


	static WebElement dropdown;
	public void select(String locator, String value) {
		dropdown = driver.findElement(By.xpath(locator));
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);

		test.log(Status.INFO, "Selecting from dropdown :   value as " + value);

	}
	
	public void type(String locator, String value) {
		
		driver.findElement(By.xpath(locator)).sendKeys(value);

		test.log(Status.INFO, "Typing in : entered value as " + value);

	}
	
	public void click(String locator) {

		driver.findElement(By.xpath(locator)).click();
		test.log(Status.INFO, "Clicking on : " + locator);
	}



	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	
	
	
	
	
	public static String capture(WebDriver driver,String screenShotName) throws IOException
    {
        TakesScreenshot ts = (TakesScreenshot)driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String dest = System.getProperty("user.dir") +"\\ErrorScreenshots\\"+screenShotName+".png";
        File destination = new File(dest);
        FileUtils.copyFile(source, destination);        
                     
        return dest;
    }


	








}
