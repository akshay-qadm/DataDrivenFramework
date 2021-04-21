package testcases;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.TestBase;

public class SourceTest extends TestBase{
	
	@Test
	public void sourceTest() throws InterruptedException {
		
		boolean source= isElementPresent(By.xpath(or.getProperty("source")));
		Assert.assertTrue(source, "Source field is present");
	}

}
