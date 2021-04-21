package testcases;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


import base.TestBase;
import utilities.ExcelReader;
import utilities.TestUtils;

public class AddItinaryTest extends TestBase{
	
	@Test(dataProviderClass=TestUtils.class, dataProvider="dp")
	public void addItinaryTest(Hashtable<String,String> data) throws InterruptedException {
		
		System.setProperty("org.uncommons.reportng.escape-output", "false"); //To enable HTML code in ReportNG
		
		if(!(TestUtils.isTestRunnable("addItinaryTest", excel))) {
			throw new SkipException("Skipping the test as run mode is No");
		}
		
		SoftAssert softassert= new SoftAssert();
		WebElement src=driver.findElement(By.xpath(or.getProperty("source")));
		WebElement dest=driver.findElement(By.xpath(or.getProperty("destination")));
		
		softassert.assertTrue(src.isEnabled(), "Source field is enabled");
		softassert.assertTrue(dest.isEnabled(), "Destination field is enabled");
		
		src.sendKeys(data.get("Source"));
		Thread.sleep(500);
		src.sendKeys(Keys.ARROW_DOWN);
		src.sendKeys(Keys.ENTER);
		dest.sendKeys(data.get("Destination"));
		Thread.sleep(500);
		dest.sendKeys(Keys.ARROW_DOWN);
		dest.sendKeys(Keys.ENTER);
		
		//wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(or.getProperty("nextMonth")))));
		softassert.assertTrue(src.getAttribute("value").contains(data.get("source")));
		TestUtils.setTargetDate(data.get("Journey Date"));
		log.debug("Departure Date entered");
		
		click(or.getProperty("search"));
		log.debug("Clicked on search results");
		softassert.assertAll();
		Reporter.log("Source, destination, departure date added successfully");
	}
	
	

}
