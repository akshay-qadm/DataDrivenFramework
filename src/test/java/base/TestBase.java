package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import utilities.ExcelReader;

public class TestBase {
	
	public static WebDriver driver;
	public static Properties config= new Properties();
	public static Properties or= new Properties();
	public static FileInputStream fis;
	public static Logger log= Logger.getLogger("devpinoyLogger");
	public static ExcelReader excel= new ExcelReader(System.getProperty("user.dir") +"\\src\\test\\resources\\excel\\goibibo.xlsx"); 
	//public static WebDriverWait wait= new WebDriverWait(driver, 10);
	
	@BeforeSuite
	public void setup() throws IOException {
		
		if(driver==null) {
			fis= new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\config.properties");
			
			config.load(fis);
			log.debug("config file loaded");
			
			fis= new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\OR.properties");
			or.load(fis);
			log.debug("OR file loaded");
		}
		if(config.getProperty("browser").equals("chrome")) {
			
			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\chromedriver.exe");
			Map<String, Object> prefs= new HashMap<String, Object>();
			prefs.put("profile.default_content_setting_values.notifications", 2);
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			
			ChromeOptions options= new ChromeOptions();
			options.setExperimentalOption("prefs", prefs);
			options.addArguments("--disable-extensions");
			options.addArguments("--disable-infobars");
			driver= new ChromeDriver(options);
			log.debug("browser launched");
			
		}
		
		driver.get(config.getProperty("URL"));
		log.debug("navigated to: "+ config.getProperty("URL"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public boolean isElementPresent(By by) {
		
		try {
			driver.findElement(by);
			return true;
		} 
		catch(NoSuchElementException e) {
			return false;
		}
		
	}
	
	public static int currentDay;
	public static int currentMonth;
	public static int currentYear;
	
	public static int targetDay;
	public static int targetMonth;
	public static int targetYear;
	
	public void click(String locator) {
		
		driver.findElement(By.xpath(or.getProperty(locator))).click();
		Reporter.log("clicked at"+ locator);
	}
	
	public void type(String locator, String value) {
		
		driver.findElement(By.xpath(or.getProperty(locator))).sendKeys(value);
		Reporter.log("typing in"+ locator + "value entered: "+ value);
	}
	
	
	@AfterSuite
	public void teardown() {
		 
		if(driver!=null) {
			driver.quit();
		}
	}

}
