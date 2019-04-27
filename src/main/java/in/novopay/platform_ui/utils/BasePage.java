package in.novopay.platform_ui.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;

public class BasePage extends JavaUtils {

	public static AndroidDriver<MobileElement> mdriver;
	public static WebDriver wdriver;
	public Map<String, String> dataMap;
	public String destFile;

	// @AndroidFindBy(id = "android:id/alertTitle")
	// public MobileElement alertPopupTitle ;

	// @AndroidFindBy(id = "android:id/button1")
	// public MobileElement alertPopupOkButton;

	public BasePage(AndroidDriver<MobileElement> mdriver) {
		BasePage.mdriver = mdriver;
	}

	public BasePage(WebDriver wdriver) {
		BasePage.wdriver = wdriver;
	}

	/**
	 * @return AndroidDriver, To launch the app using its activity and package on a
	 *         particular device mentioned across the test case in the test-data
	 */
	public AndroidDriver<MobileElement> launchApp(String deviceName) throws IOException {

		dataMap = readDeviceConfig(deviceName);
		String server = configProperties.get("appiumServer");
		String port = dataMap.get("PORT");
//		super.startAppiumServer(server, port);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		if (deviceName.equalsIgnoreCase("Emulator1")) {
			DefaultExecutor executor = new DefaultExecutor();
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

			CommandLine launchEmul = new CommandLine("C:/Program Files/Genymobile/Genymotion/player");
			launchEmul.addArgument("--vm-name");
			launchEmul.addArgument("\"" + dataMap.get("DEVICENAME") + "\"");
			executor.setExitValue(1);
			executor.execute(launchEmul, resultHandler);
		}
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(MobileCapabilityType.UDID, dataMap.get("UDID"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, dataMap.get("DEVICENAME"));
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
			capabilities.setCapability(MobileCapabilityType.VERSION, dataMap.get("ANDROIDVERSION"));
			capabilities.setCapability("appPackage", "in.novopay.merchant");
			capabilities.setCapability("appActivity", ".ui.activities.SplashScreenActivity");
//			capabilities.setCapability("appPackage", "com.climate.farmrise");
//			capabilities.setCapability("appActivity", "com.climate.farmrise.SplashScreen");
			capabilities.setCapability("noReset", "true");
			
			mdriver = new AndroidDriver<MobileElement>(new URL("http://" + server + ":" + port + "/wd/hub"),
					capabilities);
			
			Log.info("Launched Mobile app successfully");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return mdriver;
	}

	/**
	 * @return The web driver instance.
	 */
	public WebDriver launchBrowser() {

		// local machine
		String browser = configProperties.get("browser");

		if (browser.equalsIgnoreCase("Firefox")) {
			// System.setProperty("webdriver.gecko.marionette",
			// "./drivers/geckodriver.exe");

			wdriver = new FirefoxDriver();

		} else if (browser.equalsIgnoreCase("Chrome")) {

			if (configProperties.get("OperatingSystem").equalsIgnoreCase("WINDOWS")) {
				System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
			} else if (configProperties.get("OperatingSystem").equalsIgnoreCase("MAC")) {
				System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
			}
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("profile.default_content_setting_values.notifications", 1);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", prefs);
			wdriver = new ChromeDriver(options);
			
		}
		String url = configProperties.get("webAppUrl");
		wdriver.get(url);
		wdriver.manage().window().maximize();
		return wdriver;
	}

	/**
	 * Wait until web element appears
	 */
	public void waitUntilElementAppears(WebElement element) {
		WebDriverWait wait = new WebDriverWait(wdriver, 40);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Wait until mobile element appears
	 */
	public void waiTilElementVisible(WebElement element) {
		WebDriverWait wait = new WebDriverWait(mdriver, 20);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Highlight the mobile element
	 */
	public void highlightElement(MobileElement element) {

		JavascriptExecutor jse = (JavascriptExecutor) mdriver;
		;
		jse.executeScript("arguments[0],setAttribute('style,, 'background: yellow; border: 2px solid red;');", element);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		jse.executeScript("arguments[0],setAttribute('style,, 'background: yellow; border: 2px solid white;');",
				element);

	}

	/**
	 * Wait until mobile element is clickable
	 */
	public void waitUntilElementclickable(WebElement element) {

		WebDriverWait wait = new WebDriverWait(mdriver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(element));

	}

	/**
	 * Navigate you back by one page on the browser’s history.
	 */
	public void navigateback() {
		mdriver.navigate().back();
		// sReporter.log("Navigating Back to Screen", true);
	}

	/**
	 * Close the mobile application
	 */
	public void closeApp() {

		mdriver.closeApp();
		Reporter.log("Shutdown the Mobile Application");
	}

	/**
	 * Close the web browser
	 */
	public void closeBrowser() {

		wdriver.quit();
		Reporter.log("Shutdown the Web Application");
	}

	/**
	 * @return The test execution status
	 */
	public String getExecutionResultStatus(int statusCode) {

		String testStatus = null;
		if (statusCode == 1) {
			testStatus = "PASS";
		} else if (statusCode == 2) {
			testStatus = "FAIL";
		} else if (statusCode == 3) {
			testStatus = "SKIPPED";
		}
		return testStatus;
	}

	/**
	 * @return The Object[][] values
	 */
	public Object[][] returnRowsUniqueValueFromSheet(String sheetName) {

		ArrayList<String> allValues = new ArrayList<String>();
		try {
			FileInputStream file = new FileInputStream("./TestData/MobileData.xlsx");
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet(sheetName);
			Iterator<Row> it = sheet.rowIterator();

			while (it.hasNext()) {

				Row record = it.next();
				allValues.add(record.getCell(0).toString());
			}

			Object[][] values = new Object[allValues.size()][1];
			for (int i = 0; i < allValues.size(); i++) {
				values[i][0] = allValues.get(i);
			}

			return values;
		} catch (NullPointerException e) {
			System.out.println("Caught NullPointerException");
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			System.out.println("Caught EncryptedDocumentException");
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			System.out.println("Caught InvalidFormatException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Caught IOException");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Swipe vertical in mobile
	 * 
	 * @param yStart,
	 *            yEnd, totalSwipes
	 */
	public void swipeVertical(int yStart, int yEnd, int totalSwipes) {

		for (int i = 0; i < totalSwipes; i++) {
			try {
//				mdriver.swipe(300, yStart, 300, yEnd, 3000);
			} catch (Exception e) {
			}
		}

	}

	/**
	 * Swipe horizontal in mobile
	 * 
	 * @param xStart,
	 *            xEnd, totalSwipes
	 */
	public void swipehorizontal(int xStart, int xEnd, int totalSwipes) {
		try {
			for (int i = 0; i < totalSwipes; i++) {
//				mdriver.swipe(xStart, 250, xEnd, 250, 3000);
			}
		} catch (Exception e) {

		}

	}

	/**
	 * Swipe until mobile element is visible
	 * 
	 * @param element
	 */
	public void swipeUntilElementVisible(WebElement element) {
		for (int i = 1; i < 10; i++) {
//			mdriver.swipe(100, 600, 100, 500, 1);
			try {
				element.isDisplayed();
				break;
			} catch (Exception e) {
//				mdriver.swipe(100, 700, 100, 500, 1);
			}
		}
	}

	/**
	 * Select drop down value
	 * 
	 * @param element,
	 *            text
	 */
	
	public void sendText(MobileElement el, String text) {
		AndroidElement ae = (AndroidElement) el;
		ae.setValue(text);
		try {
			mdriver.hideKeyboard();
		}catch(Exception e) {
			System.err.println("Keyboard not visible");
		}
	}
	
	public void selectDropdown(WebElement element, String text) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(wdriver, 20);
		wait.until(ExpectedConditions.visibilityOf(element));
		Select dropdown = new Select(element);
		Thread.sleep(200);
		dropdown.selectByVisibleText(text);
	}

	/**
	 * Select drop down value, here checking option values
	 * 
	 * @param element,
	 *            value
	 */
	public void selectDropdown2(WebElement element, String value) {

		Select dropdown = new Select(element);

		List<WebElement> oSize = dropdown.getOptions();
		int iListSize = oSize.size();
		System.out.println(oSize.size());
		// Setting up the loop to print all the options
		for (int i = 0; i < iListSize; i++) {
			// Storing the value of the option
			String sValue = dropdown.getOptions().get(i).getText();
			System.out.println(sValue);

			if (sValue.equals(value)) {
				dropdown.selectByVisibleText(value);
				break;
			}
		}

	}

	/**
	 * @return current date in dd mm yyyy format
	 */
	public String currentdate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		String cd = sdf.format(date);
		return cd;
	}

	/**
	 * @return current date in dd/MMMM/yy hh:mm:ss format
	 */
	public String currentdatemins() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yy hh:mm:ss");
		String cd = sdf.format(date);
		return cd;
	}

	/**
	 * @return string from float value
	 * @param String
	 *            value
	 */
	public String convertfloat_to_string(String ben) {
		float d = Float.parseFloat(ben);
		int c = (int) d;
		String benAcc = Integer.toString(c);
		return benAcc;

	}

	/**
	 * @return float from string value
	 */
	public float covertStringtoFloat(String s) {
		float f = Float.parseFloat(s);
		return f;

	}

	/**
	 * @return int from string value
	 */
	public int convertString_Float_int(String s) {
		float f = covertStringtoFloat(s);
		int i = (int) f;
		return i;

	}

	/**
	 * @return int from string value
	 */
	public int covertStringtoint(String s) {
		int f = Integer.parseInt(s);
		return f;

	}

	/**
	 * This method will assert string values
	 */
	public void assertvalue(String actual, String expected, String errorMsg) {
		try {
			Assert.assertEquals(actual, expected, errorMsg);
			Reporter.log("Expected :: " + actual + "  Expected:: " + expected, true);
		} catch (AssertionError e) {
			Reporter.log("Assertion failed");
		}

	}

	/**
	 * This method will assert float values
	 */
	public void assertfloatvalue(float actual, float expected) {
		Assert.assertEquals(actual, expected);
		Reporter.log("Expected :: " + actual + "  Expected :: " + expected, true);
	}

	/**
	 * This method will assert float values
	 */
	public void assertintvalue(float actual, float expected) {
		Assert.assertEquals(actual, expected);
		Reporter.log("Expected :: " + actual + "  Expected:: " + expected, true);
	}

	/**
	 * @return class name
	 * @param class
	 */
	public String getClassName(Class<?> className) {

		String[] clsParts = className.getName().split("\\.");
		String clsName = clsParts[(clsParts.length) - 1];
		System.out.println("Class Name is : " + clsName);
		return clsName;
	}

	/**
	 * This method will wait page to load
	 * 
	 * @param sec
	 */
	public void waitForthePageToLoad(int sec) {
		wdriver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);

	}

	/**
	 * This method will wait for next element
	 * 
	 * @param sec
	 */
	public void waitFortheNextElement(int sec) {
		wdriver.manage().timeouts().implicitlyWait(sec, TimeUnit.MILLISECONDS);

	}

	/**
	 * This method will check for next element with polling time
	 * 
	 * @param webElement,
	 *            waitsec, pollsec
	 */
	public void waitandCheckfortheElement(WebElement wb, int waitsec, int pollsec) {
		new FluentWait<WebElement>(wb).withTimeout(waitsec, TimeUnit.SECONDS).pollingEvery(pollsec, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
	}

	/**
	 * To send keys in text box on mobile
	 * 
	 * @param MobileElement,
	 *            value
	 */
	public void sendKeys(MobileElement element, String value) {
		waitUntilElementAppears(element);
		element.sendKeys(value);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mdriver.navigate().back();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * To send keys in text box and hide keyboard on mobile
	 * 
	 * @param MobileElement,
	 *            value
	 */
	@SuppressWarnings("rawtypes")
	public void sendKeysTap(MobileElement element, String value) {
		KeyCode k = new KeyCode();
		waitUntilElementAppears(element);
		element.sendKeys();
		for (int i = 0; i < value.length(); i++) {
			((AndroidDriver) mdriver).pressKeyCode(k.getNumKeyCode((String.valueOf((value.charAt(i))))));

		}
		mdriver.hideKeyboard();

	}
	
	

	/**
	 * swipe vertical till element visible in mobile
	 * 
	 * @param yStart,
	 *            yEnd, MobileElement
	 */
	public void swipeVerticalUntilElementIsVisible(double yStart, double yEnd, MobileElement element) {

		mdriver.context("NATIVE_APP");
		Dimension size = mdriver.manage().window().getSize();
		// System.out.println("width : " + (size.width * .85) + " yStart : " +
		// (int) (size.height * yStart) + " yEnd : "
		// + (int) (size.height * yEnd));
		for (int i = 1; i <= 10; i++) {
			try {
				// mdriver.swipe(600, yStart, 600, yEnd, 1);
				mdriver.swipe((int) (size.width * .85), (int) (size.height * yStart), (int) (size.width * .85),
						(int) (size.height * yEnd), 2000);
				element.isDisplayed();
				break;
			} catch (Exception e) {
				mdriver.swipe((int) (size.width * .85), (int) (size.height * yStart), (int) (size.width * .85),
						(int) (size.height * yEnd), 2000);
			}
		}
	}

	/**
	 * Swipe vertical in mobile
	 * 
	 * @param yStart,
	 *            yEnd, totalSwipes
	 */
	public void swipeVertical(double yStart, double yEnd, int totalSwipes) {

		mdriver.context("NATIVE_APP");
		Dimension size = mdriver.manage().window().getSize();
		for (int i = 1; i <= totalSwipes; i++) {
			try {
				mdriver.swipe((int) (size.width * .85), (int) (size.height * yStart), (int) (size.width * .85),
						(int) (size.height * yEnd), 2000);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @return boolean, tap on Mpin
	 * @param mpin
	 */
//	public Boolean tapMPINBtn(String mpins) {
//		MobileElement submitMPINBtn = mdriver.findElement(By.id("in.novopay.merchant:id/btnDone"));
//		char[] mpin = mpins.toCharArray();
//		try {
//			for (char pin : mpin) {
//				String mpinBtn = "in.novopay.merchant:id/btnNum" + pin;
//				mdriver.findElement(By.id(mpinBtn)).click();
//			}
//			submitMPINBtn.click();
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;

//	}

	/**
	 * Takes screenshot both web and mobile
	 */
	public void takeScreenShot(Map<String, String> workFlowDataMap) {
		String destDir = "./ScreenShots";
		File scrFile;
		if (workFlowDataMap.get("TESTON").equalsIgnoreCase("WEB")) {
			scrFile = ((TakesScreenshot) wdriver).getScreenshotAs(OutputType.FILE);
		} else {
			scrFile = ((TakesScreenshot) mdriver).getScreenshotAs(OutputType.FILE);
		}
		new File(destDir).mkdirs();
		destFile = workFlowDataMap.get("TCID") + ".png";

		try {

			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wait till the element load on web
	 */
	public void waitingForTheElementToLoad(WebElement element) {
		WebDriverWait wait = new WebDriverWait(wdriver, 90);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * To press on tab
	 */
	public void keyTab() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ESCAPE);

	}

	/**
	 * To page up
	 */
	public void pageUP() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_HOME);
	}

	/**
	 * Click on in visible web element for web
	 */
	public void clickInvisibleElement(WebElement webElement) {
		JavascriptExecutor executor = (JavascriptExecutor) wdriver;
		executor.executeScript("arguments[0].click();", webElement);
	}

	/**
	 * Swipe Horizontal in mobile
	 */
	public void swipeHorizontal(double xStart, double xEnd, int totalSwipes) {
		mdriver.context("NATIVE_APP");
		Dimension size = mdriver.manage().window().getSize();
		for (int i = 1; i <= totalSwipes; i++) {
			try {
				mdriver.swipe((int) (size.width * xStart), (int) (size.height * .30), (int) (size.width * xEnd),
						(int) (size.height * .30), 800);
				
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Launch web browser though passing web application url as a parameter
	 */
	public WebDriver launchBrowser(String url) {

		// local machine
		String browser = configProperties.get("browser");

		if (browser.equalsIgnoreCase("Firefox")) {
			// System.setProperty("webdriver.gecko.marionette",
			// "./drivers/geckodriver.exe");

			wdriver = new FirefoxDriver();

		} else if (browser.equalsIgnoreCase("Chrome")) {

			if (configProperties.get("OperatingSystem").equalsIgnoreCase("WINDOWS")) {

				System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
			} else if (configProperties.get("OperatingSystem").equalsIgnoreCase("MAC")) {
				System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver1");
			}

			wdriver = new ChromeDriver();
		}
		wdriver.get(url);
		// wdriver.manage().window().maximize();
		return wdriver;
	}

	/**
	 * Scroll till web element visible only for Web
	 */
	public void scrollToElement(WebElement webElement) {
		JavascriptExecutor executor = (JavascriptExecutor) wdriver;
		executor.executeScript("arguments[0].scrollIntoView(true);", webElement);
	}

	/**
	 * To hide soft keyboard in mobile
	 */
	public static void hideKeyboard(Context ctx) {
		InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

		// check if no view has focus:
		View v = ((Activity) ctx).getCurrentFocus();
		if (v == null)
			return;

		inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/**
	 * To verify element in mobile
	 */
	public boolean findElement(String name) {
		try {
			if ((mdriver.findElementByName(name)) != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * This method will Capture screenshot on failed test script, save in
	 * Screenshots folder
	 * 
	 * @param result,
	 *            Tcid
	 */
	public void captureScreenshotOnFailedTest(ITestResult result, String Tcid) {
		if (ITestResult.FAILURE == result.getStatus()) {
			try {
				Log.info("Taking screenshot on failed test");
				File source = ((TakesScreenshot) wdriver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(source, new File("./Screenshots/" + Tcid + ".png"));
				Log.info("Screenshot taken");
			} catch (Exception e) {
				System.out.println("Exception while taking screenshot " + e.getMessage());
			}
		}
	}

}
