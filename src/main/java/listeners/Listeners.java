package listeners;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import resources.Base;
import utilities.ExtentReporter;


public class Listeners extends Base  implements  ITestListener {
	
	
	WebDriver driver=null;
	ExtentReports extenReport =ExtentReporter.getExtentReport();
	ExtentTest extentTest;
	//This Below Line is Threadsafe for Parallel Running Tests
	ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<ExtentTest>();
	
	
	@Override
	public void onTestStart(ITestResult result) {
		
		String testName= result.getName();
		extentTest=	extenReport.createTest(testName);
		extentTestThread.set(extentTest);
		
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName=result.getName();
		//extentTest.log(Status.PASS,testName+"Test Passed");
		//above line is for series test below is for parallel 
		extentTestThread.get().log(Status.PASS,testName+"Test Passed");
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		
		//extentTest.fail(result.getThrowable());
		//above line is for series test below is for parallel
		extentTestThread.get().fail(result.getThrowable());
		
		WebDriver driver = null;
		
		String testMethodName = result.getName();
		 
		try {
			driver = (WebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
		String screenshotFilePath=	takeScreenshot(testMethodName,driver);
		//Screenshot will be added in Extent Report By Below Line
		extentTestThread.get().addScreenCaptureFromPath(screenshotFilePath, testMethodName);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		
	}

	@Override
	public void onStart(ITestContext context) {
		
	}

	@Override
	public void onFinish(ITestContext context) {
		
		extenReport.flush();
		
		
	}

	
	
}
