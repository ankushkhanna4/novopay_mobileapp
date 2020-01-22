package in.novopay.platform_ui.pages.mobile;

import java.awt.AWTException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import in.novopay.platform_ui.utils.BasePage;
import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.Log;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class HomePage extends BasePage {
	DBUtils dbUtils = new DBUtils();
	DecimalFormat df = new DecimalFormat("#.00");

	WebDriverWait wait = new WebDriverWait(mdriver, 30);
	WebDriverWait waitLogin = new WebDriverWait(mdriver, 5);

	@AndroidFindBy(className = "android.widget.ImageButton")
	MobileElement hamburgerMenu;

	@AndroidFindBy(id = "in.novopay.merchant:id/refresh_balance")
	MobileElement refreshButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/textViewBalance")
	MobileElement retailerWalletBalance;

	@AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'.')]")
	MobileElement retailerWalletBalance1;

	@AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'cash to account')]/parent::android.widget.LinearLayout")
	MobileElement cashToAccount;

	@AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'status inquiry')]/parent::android.widget.LinearLayout")
	MobileElement statusEnquiry;

	@AndroidFindBy(id = "in.novopay.merchant:id/signInMobileNumber")
	MobileElement mobNumTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/fab")
	MobileElement submitLoginBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/btnDone")
	MobileElement submitMPINBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/notification_count")
	MobileElement fcmCount;

	@AndroidFindBy(id = "in.novopay.merchant:id/bell")
	MobileElement bellIcon;

	@AndroidFindBy(xpath = "//*[contains(@text,'Notification')]")
	MobileElement fcmScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/your_current_balance_label")
	MobileElement balanceLabel;

	@AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'swiggy')]/parent::android.widget.LinearLayout")
	MobileElement swiggy;

	// Load all objects
	public HomePage(AndroidDriver<MobileElement> mdriver) {
		super(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver), this);
	}

	// Perform action on page based on given commands
	public void home(Map<String, String> usrData)
			throws InterruptedException, AWTException, IOException, ClassNotFoundException {

		try {
			try {
				locateRefresh();
			} catch (Exception e) {
				wait.until(ExpectedConditions.visibilityOf(mobNumTextField));
				mobNumTextField.clear();
				Log.info("entering mobile number");
				sendText(mobNumTextField, readValue(getLoginMobileFromIni(usrData.get("MOBILENUMBER"))));
				Log.info("clicking on Login button");
				submitLoginBtn.click();
				Log.info("Login button clicked");
				tapMPINBtn(usrData.get("MPIN"));
				submitMPINBtn.click();
				Log.info("MPIN submitted");
				waitForSpinner();
				wait.until(ExpectedConditions.visibilityOf(balanceLabel));
			}
			Log.info("Home page displayed");

			// Refresh wallet balances whenever required
			if (usrData.get("REFRESH").equalsIgnoreCase("YES")) {
				refreshBalance(); // refresh wallet balances
			}
			Thread.sleep(2000);
			displayWalletBalance(); // display wallet balances in console

			if (usrData.get("FEATURE").equalsIgnoreCase("Notification Icon")) {
				locateBellIcon();
				bellIcon.click();
				wait.until(ExpectedConditions.elementToBeClickable(fcmScreen));
				Log.info("FCM screen displayed");
			} else {
				clickIcon(usrData);
			}
		} catch (Exception e) {
			Log.info("Test Case Failed");
			e.printStackTrace();
			Assert.fail();
		}
	}

	// Show balances in console
	public void displayWalletBalance() throws ClassNotFoundException, InterruptedException {
		String walletBalance = dbUtils.getWalletBalance(mobileNumFromIni(), "retailer");
		String walletBal = walletBalance.substring(0, walletBalance.length() - 4);
		wait.until(ExpectedConditions.elementToBeClickable(retailerWalletBalance1));
		String initialWalletBal = replaceSymbols(retailerWalletBalance1.getText());

		Assert.assertEquals(initialWalletBal, walletBal);
		System.out.println("Balance asserted successfully");
		getBalanceFromIni("Store", "retailer", initialWalletBal);
		Log.info("Retailer Balance: " + initialWalletBal);
		getBalanceFromIni("Store", "cashout", dbUtils.getWalletBalance(mobileNumFromIni(), "cashout"));
		Log.info("Cashout Balance: " + dbUtils.getWalletBalance(mobileNumFromIni(), "cashout"));
	}

	// Get wallet(s) balance
	@SuppressWarnings("null")
	public double getInitialBalance(String wallet) throws ClassNotFoundException {
		String initialWalletBal = replaceSymbols(retailerWalletBalance.getText());
//		String initialCashoutBal = replaceSymbols(cashoutWalletBalance.getText());
//		String initialMerchantBal = replaceSymbols(merchantWalletBalance.getText());

		// Converting balance from String to Double and returning the same
		if (wallet.equalsIgnoreCase("retailer")) {
			return Double.parseDouble(initialWalletBal);
//		} else if (wallet.equalsIgnoreCase("cashout")) {
//			return Double.parseDouble(initialCashoutBal);
//		} else if (wallet.equalsIgnoreCase("merchant")) {
//			return Double.parseDouble(initialMerchantBal);
		}
		return (Double) null;
	}

	public double getInitialWalletBalance() throws ClassNotFoundException {
		String initialWalletBal = replaceSymbols(retailerWalletBalance.getText());

		// Converting balance from String to Double and returning the same
		return Double.parseDouble(initialWalletBal);
	}

	// To refresh the wallet balance
	public void refreshBalance() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(refreshButton));
		refreshButton.click();
		Log.info("Balance refreshed successfully");
	}

	// Get mobile number from Ini file
	public String mobileNumFromIni() {
		return getLoginMobileFromIni("RBLRetailerMobNum");
	}

	public void clickIcon(Map<String, String> usrData) throws InterruptedException {
		while (true) {
			try {
				String homeFeatureName = "in.novopay.merchant:id/" + usrData.get("HOMEFEATURE");
				WebElement homeFeature = mdriver.findElement(By.id(homeFeatureName));
				homeFeature.click();
				System.out.println(usrData.get("HOMEFEATURE") + " feature clicked");
				while (true) {
					try {
						String featureXpath = "//android.widget.TextView[contains(@text,'"
								+ usrData.get("FEATURE").toLowerCase() + "')]/parent::android.widget.LinearLayout";
						wait.until(ExpectedConditions.visibilityOf(mdriver.findElement(By.xpath(featureXpath))));
						mdriver.findElement(By.xpath(featureXpath)).click();
						break;
					} catch (Exception e) {
						swipeVertical(0.900, 0.200, 1);
						Thread.sleep(1000);
					}
				}
				break;
			} catch (Exception e) {
				swipeVertical(0.900, 0.200, 1);
				Thread.sleep(1000);
			}
		}
	}

	public void locateRefresh() throws InterruptedException {
		int i = 1;
		while (i < 10) {
			try {
				wait.until(ExpectedConditions
						.elementToBeClickable(mdriver.findElement(By.id("in.novopay.merchant:id/refresh_balance"))));
				break;
			} catch (Exception e) {
				swipeVertical(0.200, 0.900, 1);
				Thread.sleep(1000);
			}
			i++;
		}
	}

	public void locateBellIcon() throws InterruptedException {
		int i = 1;
		while (i < 10) {
			try {
				wait.until(ExpectedConditions
						.elementToBeClickable(mdriver.findElement(By.id("in.novopay.merchant:id/bell"))));
				break;
			} catch (Exception e) {
				swipeVertical(0.200, 0.900, 1);
				Thread.sleep(1000);
			}
			i++;
		}
	}

	// Scroll down the page
	public void pageScrollDown() {
		JavascriptExecutor jse = (JavascriptExecutor) wdriver;
		jse.executeScript("scroll(0, 250);");
	}

	// Wait for screen to complete loading
	public void waitForSpinner() {
		wait.until(ExpectedConditions
				.invisibilityOfElementLocated(By.xpath("//div/ng-busy/div/ng-component/div/div/div[1]")));
		Log.info("Please wait...");
	}

	// Remove rupee symbol and comma from the string
	public String replaceSymbols(String element) {
		String editedElement = element.replaceAll("₹", "").replaceAll(",", "").trim();
		return editedElement;
	}

	public Boolean tapMPINBtn(String mpins) {
		char[] mpin = mpins.toCharArray();
		try {
			for (char pin : mpin) {
				String mpinBtn = "in.novopay.merchant:id/btnNum" + pin;
				mdriver.findElement(By.id(mpinBtn)).click();
			}
			Log.info("MPIN Entered");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public void launchChrome() throws MalformedURLException {
		// Set the Desired Capabilities
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "Micromax_Q4310");
		caps.setCapability("udid", "GQOZUOY9Q87DLJSK");
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "7.0");
		caps.setCapability("browserName", "Chrome");
		caps.setCapability("noReset", true);

		// Set ChromeDriver location
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");

		// Instantiate Appium Driver
		AppiumDriver<MobileElement> driver = null;
		try {
			driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), caps);

		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}

		// Open URL in Chrome Browser
		driver.get("https://pre-prod.novopay.in/portal/remittance/disable/queuing?bankcode=AUCB");

		driver.closeApp();

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "Micromax_Q4310");
		capabilities.setCapability("udid", "GQOZUOY9Q87DLJSK");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "7.0");
		capabilities.setCapability("appPackage", "in.novopay.merchant");
		capabilities.setCapability("appActivity", ".ui.activities.LoginActivity");
		capabilities.setCapability("noReset", "true");

		mdriver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
	}
}
