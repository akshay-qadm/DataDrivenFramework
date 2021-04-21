package utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import base.TestBase;

public class TestUtils extends TestBase{
	
	public static void captureScreenshot(String method) throws IOException {
		
		Date date= new Date();
		String path=date.toString().replace(":", "_").replace(" ", "_")+ ".jpg";
		
		File screenshot= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(System.getProperty("user.dir")+"target\\surefire-reports\\html\\"+method+"_"+path));
		
	}
	
	
	
	public static void getCurrentDayMonthYear() {
		
		Calendar cal= Calendar.getInstance();
		
		currentDay= cal.get(Calendar.DAY_OF_MONTH);
		currentMonth= cal.get(Calendar.MONTH)+1;
		currentYear= cal.get(Calendar.YEAR);
	}
	
	public static void getTargetDayMonthYear(String targetDate) {
		
		Calendar cal= Calendar.getInstance();
		
		String[] li=targetDate.split("/");
		
		targetDay= Integer.parseInt(li[0]);
		targetMonth= Integer.parseInt(li[1]);
		targetYear= Integer.parseInt(li[2]);
	}
	
	public static void setTargetDate(String targetDate) throws InterruptedException {
		
		getCurrentDayMonthYear();
		getTargetDayMonthYear(targetDate);
		
		if((targetMonth-currentMonth)>0) {
			int jumpMonths= targetMonth- currentMonth;
			for(int i=0; i<jumpMonths; i++) {
				driver.findElement(By.xpath(or.getProperty("nextMonth"))).click();
				Thread.sleep(1000);
			}
		}
		else if((currentMonth- targetMonth) >0) {
			int jumpMonths= currentMonth- targetMonth;
			for(int i=0; i<jumpMonths; i++) {
				driver.findElement(By.xpath(or.getProperty("previousMonth"))).click();
				Thread.sleep(1000);
			}
		}
		
		driver.findElement(By.xpath("//div[contains(@aria-label,\'"+targetDay+"\')]")).click();;
	}
	
	
	@DataProvider(name="dp")
	public Object[][] getData(Method m){
		
		String sheetName= m.getName();
		int rows= excel.getRowCount(sheetName);
		int col= excel.getColumnCount(sheetName);
		
		Object[][] data =new Object[rows-1][1];
		
		Hashtable<String,String> table= null;
		
		for(int rowNum=2; rowNum<=rows; rowNum++) {
			
			table= new Hashtable<String, String>();
			
			for(int colNum=0; colNum<col ; colNum++) {
				
				table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
				data[rowNum-2][0]= table;
			}
		}
		return data;
	}
	
	public static boolean isTestRunnable(String testName, ExcelReader excel) {
		
		String sheetName="TestRun";
		int rows= excel.getRowCount(sheetName);
		
		for(int rowNum=2; rowNum<=rows; rowNum++) {
			String testCase= excel.getCellData(sheetName, "TCID", rowNum);
			if(testCase.equalsIgnoreCase(testName)) {
				String runMode= excel.getCellData(sheetName, "Run mode", rowNum);
				if(runMode.equalsIgnoreCase("Y")) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		return false;
	}

}
