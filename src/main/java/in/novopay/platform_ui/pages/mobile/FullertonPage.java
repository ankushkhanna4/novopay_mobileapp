package in.novopay.platform_ui.pages.mobile;

import java.awt.AWTException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import in.novopay.platform_ui.utils.BasePage;
import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.Log;
import in.novopay.platform_ui.utils.MongoDBUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class FullertonPage extends BasePage {
	DBUtils dbUtils = new DBUtils();
	MongoDBUtils mongoDbUtils = new MongoDBUtils();
	DecimalFormat df = new DecimalFormat("#.00");

	WebDriverWait wait = new WebDriverWait(mdriver, 30);

	@AndroidFindBy(id = "in.novopay.merchant:id/pic")
	MobileElement fullertonIcon;

	@AndroidFindBy(id = "in.novopay.merchant:id/et_emp_id")
	MobileElement empId;

	@AndroidFindBy(id = "in.novopay.merchant:id/text_get_details")
	MobileElement getDetailsButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/text_get_clear")
	MobileElement clearButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/flurtton_executive_name")
	MobileElement execName;

	@AndroidFindBy(id = "in.novopay.merchant:id/flurtton_executive_mob")
	MobileElement mobNumber;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_paying_amount_collection")
	MobileElement fetchedAmount;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_due_amount")
	MobileElement dueAmnt;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_confirm_executive_name")
	MobileElement confirmExecName;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_swiggy_contact_number")
	MobileElement confirmExecNumber;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_confirm_executive_driving_licence")
	MobileElement confirmExecLicNumber;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_confirm_paying_amount")
	MobileElement confirmAmount;

	@AndroidFindBy(id = "in.novopay.merchant:id/button")
	MobileElement proceedButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'Enter 4 digit PIN')]")
	MobileElement enterMPIN;

	@AndroidFindBy(xpath = "//*[contains(@text,'OK')]")
	MobileElement okBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/btnDone")
	MobileElement submitMPINBtn;

	@AndroidFindBy(xpath = "//*[contains(@text,'processing...')]")
	MobileElement processingScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/process_in_background")
	MobileElement processInBackgroundButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'!')]")
	MobileElement ftTxnScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_exit")
	MobileElement doneButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_exit")
	MobileElement exitButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_retry")
	MobileElement retryButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_msg")
	MobileElement ftTxnSuccessScreenMessage;

	@AndroidFindBy(id = "in.novopay.merchant:id/failure_msg")
	MobileElement ftTxnFailScreenMessage;

	// Load all objects
	public FullertonPage(AndroidDriver<MobileElement> mdriver) {
		super(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver), this);
	}

	// Perform action on page based on given commands
	public void fullerton(Map<String, String> usrData)
			throws InterruptedException, AWTException, IOException, ClassNotFoundException {

		try {
			// Click on Fullerton icon
			wait.until(ExpectedConditions.elementToBeClickable(fullertonIcon));
			Log.info("Fullerton icon displayed");

			String dueDate = "";
			if (usrData.get("DUEDATE").equalsIgnoreCase("Today")) {
				dueDate = getTodaysDate("dd-MMM-yy");
			} else if (usrData.get("DUEDATE").equalsIgnoreCase("Old Date")) {
				dueDate = "30-Aug-18";
			}

			int dueAmount = 0, paidAmount = 0;
			dueAmount = Integer.parseInt(usrData.get("DUEAMOUNT"));
			paidAmount = Integer.parseInt(usrData.get("PAIDAMOUNT"));

			if (!usrData.get("STATUS").equalsIgnoreCase("SUCCESS")) {
				mongoDbUtils.updateValues("111", dueDate, dueAmount, paidAmount, usrData.get("STATUS"),
						usrData.get("OFFICERMOBNUM"), usrData.get("ADDMOBNUM"));
			}

			// Click on emp id field
			wait.until(ExpectedConditions.elementToBeClickable(empId));
			sendText(empId, usrData.get("EMPID"));
			Log.info("Employee ID entered");

			if (usrData.get("GETAMOUNT").equalsIgnoreCase("YES")) {
				// Click on Get Amount button
				wait.until(ExpectedConditions.elementToBeClickable(getDetailsButton));
				getDetailsButton.click();

				if (usrData.get("EMPID").equalsIgnoreCase("999")
						|| usrData.get("DUEDATE").equalsIgnoreCase("Old Date")) {
//					wait.until(ExpectedConditions.visibilityOf(toasterMsg));
//					Assert.assertEquals(toasterMsg.getText(), "Employee ID not found. Please recheck employee ID");
					Log.info("Employee ID not found. Please recheck employee ID");
					mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
					wait.until(ExpectedConditions.elementToBeClickable(okBtn));
					okBtn.click();
					Log.info("Home page displayed");
				} else if (usrData.get("STATUS").equalsIgnoreCase("SUCCESS")) {
//					wait.until(ExpectedConditions.visibilityOf(toasterMsg));
//					Assert.assertEquals(toasterMsg.getText(), "You have reached maximum deposit limit for the day");
					Log.info("You have reached maximum deposit limit for the day");
					mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
					wait.until(ExpectedConditions.elementToBeClickable(okBtn));
					okBtn.click();
					Log.info("Home page displayed");
				} else {
					wait.until(ExpectedConditions.visibilityOf(execName));
					Assert.assertEquals(execName.getText(), "Bhagirath Singh");
					Log.info("Executive name: " + execName.getText());
					Assert.assertEquals(mobNumber.getText(), usrData.get("OFFICERMOBNUM"));
					Log.info("Executive mobile number: " + mobNumber.getText());
//					Assert.assertEquals(dueAmnt.getText() + ".00", "Due Amount: ₹ " + fetchedAmount.getText());
					Log.info(dueAmnt.getText());
					String amount = fetchedAmount.getText();
					Log.info("Fetched amount: " + amount);
					cmsDetailsFromIni("StoreFtAmount", replaceSymbols(amount)); // Store fetched amount

					if (usrData.get("ASSERTION").contains("Reversed")) {
						mongoDbUtils.updateValues("111", dueDate, dueAmount, paidAmount, "SUCCESS",
								usrData.get("OFFICERMOBNUM"), usrData.get("ADDMOBNUM"));
					}
				}
			} else if (usrData.get("GETAMOUNT").equalsIgnoreCase("Clear")) {
				// Click on Clear button
				clearButton.click();
			}

			if (usrData.get("SUBMIT").equalsIgnoreCase("Yes")) {
				if (!usrData.get("AMOUNT").equalsIgnoreCase("SKIP")) {
					fetchedAmount.clear();
					sendText(fetchedAmount, usrData.get("AMOUNT"));
					cmsDetailsFromIni("StoreFtAmount", usrData.get("AMOUNT") + ".00");
					Log.info("Entered amount: " + usrData.get("AMOUNT"));
				}
				// Click on Submit button
				wait.until(ExpectedConditions.elementToBeClickable(proceedButton));
				proceedButton.click();

				if (usrData.get("ASSERTION").equalsIgnoreCase("Amount > Max")) {
					Log.info("Amount has to be less than or equal to 10,000.00");
					mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
					wait.until(ExpectedConditions.elementToBeClickable(okBtn));
					okBtn.click();
					Log.info("Home page displayed");
				} else {

					wait.until(ExpectedConditions.visibilityOf(enterMPIN));
					Log.info("MPIN screen displayed");
					if (usrData.get("MPIN").equalsIgnoreCase("Valid")) {
						tapMPINBtn(getAuthfromIni("MPIN"));
					} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
						tapMPINBtn("9999");
					}

					if (usrData.get("MPINSCREENBUTTON").equalsIgnoreCase("Cancel")) {
						mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
						wait.until(ExpectedConditions.elementToBeClickable(fetchedAmount));
						mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
						wait.until(ExpectedConditions.elementToBeClickable(okBtn));
						okBtn.click();
						Log.info("Home page displayed");
					} else if (usrData.get("MPINSCREENBUTTON").equalsIgnoreCase("Submit")) {
						submitMPINBtn.click();
						Log.info("Submit button clicked");
						wait.until(ExpectedConditions.visibilityOf(processingScreen));
						Log.info("Processing screen displayed");
						if (usrData.get("TXNSCREENBUTTON").equals("Process in Background")) {
							wait.until(ExpectedConditions.visibilityOf(processInBackgroundButton));
							processInBackgroundButton.click();
							Log.info("Process in Background button clicked");
						} else {
							wait.until(ExpectedConditions.visibilityOf(ftTxnScreen));
							Log.info("Txn screen displayed");

							// Verify the details on transaction screen
							if (ftTxnScreen.getAttribute("text").equalsIgnoreCase("success!")) {
								assertionOnSuccessScreen(usrData);
								wait.until(ExpectedConditions.elementToBeClickable(doneButton));
								doneButton.click();
								Log.info("Done button clicked");
								if (!usrData.get("ASSERTION").equalsIgnoreCase("Processing")) {
									verifyUpdatedBalanceAfterSuccessTxn(usrData,
											getBalanceFromIni("Get", "retailer", ""));
								}
							} else if (ftTxnScreen.getAttribute("text").equalsIgnoreCase("failed!")) {
								if (usrData.get("MPIN").equalsIgnoreCase("Valid")) {
									assertionOnFailedScreen(usrData);
									if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
										wait.until(ExpectedConditions.elementToBeClickable(exitButton));
									} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
										retryButton.click();
										Log.info("Retry button clicked");
										wait.until(ExpectedConditions.visibilityOf(proceedButton));
										proceedButton.click();
										Log.info("Submit button clicked");
										wait.until(ExpectedConditions.visibilityOf(enterMPIN));
										Log.info("MPIN screen displayed");
										wait.until(ExpectedConditions.elementToBeClickable(enterMPIN));
										tapMPINBtn(getAuthfromIni("MPIN"));
										Log.info("MPIN entered");
										submitMPINBtn.click();
										Log.info("Submit MPIN button clicked");
										wait.until(ExpectedConditions.visibilityOf(processingScreen));
										Log.info("Processing screen displayed");
										wait.until(ExpectedConditions.visibilityOf(ftTxnScreen));
										Log.info("Txn screen displayed");
										assertionOnFailedScreen(usrData);
									}
									exitButton.click();
									Log.info("Exit button clicked");
									verifyUpdatedBalanceAfterFailTxn(usrData, getBalanceFromIni("Get", "retailer", ""));
								} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
									wait.until(ExpectedConditions.elementToBeClickable(ftTxnFailScreenMessage));
									Log.info(ftTxnFailScreenMessage.getText());
									if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
										exitButton.click();
										Log.info("Exit button clicked");
									} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
										retryButton.click();
										Log.info("Retry button clicked");
										wait.until(ExpectedConditions.visibilityOf(proceedButton));
										proceedButton.click();
										wait.until(ExpectedConditions.visibilityOf(enterMPIN));
										Log.info("MPIN screen displayed");
										tapMPINBtn(getAuthfromIni("MPIN"));
										wait.until(ExpectedConditions.elementToBeClickable(submitMPINBtn));
										submitMPINBtn.click();
										Log.info("Submit button clicked");
										wait.until(ExpectedConditions.visibilityOf(processingScreen));
										Log.info("Processing screen displayed");
										wait.until(ExpectedConditions.visibilityOf(ftTxnScreen));
										Log.info("Txn screen displayed");
										assertionOnSuccessScreen(usrData);
										doneButton.click();
										Log.info("Done button clicked");
										verifyUpdatedBalanceAfterSuccessTxn(usrData,
												getBalanceFromIni("Get", "retailer", ""));
									}
								}
							}
							assertionOnSMS(usrData);
						}
					}
				}
			} else if (usrData.get("SUBMIT").equalsIgnoreCase("Clear")) {
				clearButton.click();
				Log.info("Clear button clicked");
				mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
				wait.until(ExpectedConditions.elementToBeClickable(okBtn));
				okBtn.click();
				Log.info("Home page displayed");
			} else if (usrData.get("SUBMIT").equalsIgnoreCase("Fetch")) {
				mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
				wait.until(ExpectedConditions.elementToBeClickable(okBtn));
				okBtn.click();
				Log.info("Home page displayed");
			}
		} catch (Exception e) {
			Log.info("Test Case Failed");
			e.printStackTrace();
			Assert.fail();
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

	// Get Partner name
	public String partner() {
		return "RBL";
	}

	// Get mobile number from Ini file
	public String mobileNumFromIni() {
		return getLoginMobileFromIni(partner().toUpperCase() + "RetailerMobNum");
	}

	// Remove rupee symbol and comma from the string
	public String replaceSymbols(String element) {
		String editedElement = element.replaceAll("₹", "").replaceAll(",", "").trim();
		return editedElement;
	}

	// click on MobileElement forcefully
	public void clickElement(MobileElement element) {
		try {
			element.click();
		} catch (Exception e) {
			clickInvisibleElement(element);
		}
	}

	// Verify details on success screen
	public void assertionOnSuccessScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		Assert.assertEquals(ftTxnSuccessScreenMessage.getText(), "Deposit to Fullerton successful.");
		Log.info(ftTxnSuccessScreenMessage.getText());
	}

	// Verify details on failed screen
	public void assertionOnFailedScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("ASSERTION").equalsIgnoreCase("Invalid MPIN")) {
			Assert.assertEquals(ftTxnFailScreenMessage.getText(), "Authentication Failed Invalid MPIN");
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Amount > Wallet")) {
			Assert.assertEquals(ftTxnFailScreenMessage.getText(), "Insufficient balance");
		} else {
			Assert.assertEquals(ftTxnFailScreenMessage.getText(),
					"Information about this employee is not present in our system. Transaction reversed successfully.");
		}
		Log.info(ftTxnFailScreenMessage.getText());
	}

	// SMS assertion
	public void assertionOnSMS(Map<String, String> usrData) throws ClassNotFoundException {
		String successSMS = "Success! Deposit of Rs "
				+ cmsDetailsFromIni("FtAmount", "").substring(0, cmsDetailsFromIni("FtAmount", "").length() - 3)
				+ " for EMP-ID " + usrData.get("EMPID") + " was successful.";
		String failSMS = "Failure! Deposit of Rs "
				+ cmsDetailsFromIni("FtAmount", "").substring(0, cmsDetailsFromIni("FtAmount", "").length() - 3)
				+ " for EMP-ID " + usrData.get("EMPID") + " failed.";
		if (usrData.get("ASSERTION").equalsIgnoreCase("Success SMS")) {
			Assert.assertEquals(successSMS, dbUtils.sms());
			Assert.assertEquals(successSMS, dbUtils.smsFt());
			try {
				Assert.assertEquals(mongoDbUtils.getOffMobNum(), dbUtils.smsNum2());
				Assert.assertEquals(mongoDbUtils.getAddMobNum(), dbUtils.smsNum1());
			} catch (Exception e) {
				Assert.assertEquals(mongoDbUtils.getAddMobNum(), dbUtils.smsNum1());
				Assert.assertEquals(mongoDbUtils.getAddMobNum(), dbUtils.smsNum2());
			}
			Log.info(successSMS);
			Log.info("SMS sent successfully to " + dbUtils.smsNum2() + " and " + dbUtils.smsNum1());
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Reversed SMS")) {
			Assert.assertEquals(failSMS, dbUtils.sms());
			Assert.assertEquals(mongoDbUtils.getOffMobNum(), dbUtils.smsNum1());
			Log.info(failSMS);
			Log.info("SMS sent successfully to " + dbUtils.smsNum1());
		}
	}

	// Assertion after success or orange screen is displayed
	public void verifyUpdatedBalanceAfterSuccessTxn(Map<String, String> usrData, String oldWalletBalance)
			throws ClassNotFoundException {
		double amount = Double.parseDouble(cmsDetailsFromIni("FtAmount", ""));
		double comm = amount * 4 / 1000;
		double commission = Math.round(comm * 100.0) / 100.0;
		double taxDS = commission * Double.parseDouble(dbUtils.getTDSPercentage(mobileNumFromIni())) / 10000;
		double tds = Math.round(taxDS * 100.0) / 100.0;
		double initialWalletBalance = Double.parseDouble(oldWalletBalance);
		double newWalletBal = initialWalletBalance - amount + commission - tds;
		txnDetailsFromIni("StoreComm", String.valueOf(commission));
		txnDetailsFromIni("StoreTds", String.valueOf(tds));
		String newWalletBalance = df.format(newWalletBal);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "retailer"), newWalletBalance + "0000");
		Log.info("Updated Retailer Wallet Balance: " + newWalletBalance);
	}

	// Assertion after success screen is displayed
	public void verifyUpdatedBalanceAfterFailTxn(Map<String, String> usrData, String oldWalletBalance)
			throws ClassNotFoundException {
		Double initialWalletBalance = Double.parseDouble(oldWalletBalance);
		String newWalletBalance = df.format(initialWalletBalance);
		if (newWalletBalance == ".00") {
			newWalletBalance = "0.00";
		}
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "retailer"), newWalletBalance + "0000");
		Log.info("Updated Retailer Wallet Balance: " + newWalletBalance);
	}

	public Boolean tapMPINBtn(String mpins) {
		char[] mpin = mpins.toCharArray();
		try {
			for (char pin : mpin) {
				String mpinBtn = "in.novopay.merchant:id/btnNum" + pin;
				mdriver.findElement(By.id(mpinBtn)).click();
			}
			Log.info("Mpin Entered");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}