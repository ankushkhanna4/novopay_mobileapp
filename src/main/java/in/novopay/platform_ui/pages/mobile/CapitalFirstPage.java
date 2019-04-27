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
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class CapitalFirstPage extends BasePage {
	DBUtils dbUtils = new DBUtils();
	DecimalFormat df = new DecimalFormat("#.00");

	WebDriverWait wait = new WebDriverWait(mdriver, 30);

	@AndroidFindBy(id = "in.novopay.merchant:id/pic")
	MobileElement cfIcon;

	@AndroidFindBy(id = "in.novopay.merchant:id/et_despositor_mobile_number")
	MobileElement depositorMobNum;

	@AndroidFindBy(id = "in.novopay.merchant:id/rb_cfl")
	MobileElement cflRadioButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/rb_hfc")
	MobileElement hfcRadioButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/et_batch_id")
	MobileElement batchId;

	MobileElement execLicNumber;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_paying_amount_collection")
	MobileElement fetchedAmount;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_due_amount")
	MobileElement dueAmnt;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_company")
	MobileElement tvCompany;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_company_batch_id")
	MobileElement tvBatchId;

	@AndroidFindBy(id = "in.novopay.merchant:id/tv_paying_amount")
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
	MobileElement cfTxnScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_exit")
	MobileElement doneButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_exit")
	MobileElement exitButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_retry")
	MobileElement retryButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_msg")
	MobileElement cfTxnSuccessScreenMessage;

	@AndroidFindBy(id = "in.novopay.merchant:id/failure_msg")
	MobileElement cfTxnFailScreenMessage;

	// Load all objects
	public CapitalFirstPage(AndroidDriver<MobileElement> mdriver) {
		super(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver), this);
	}

	// Perform action on page based on given commands
	public void capitalFirst(Map<String, String> usrData)
			throws InterruptedException, AWTException, IOException, ClassNotFoundException {

		try {
			// Click on CapitalFirstPage icon
			wait.until(ExpectedConditions.elementToBeClickable(cfIcon));
			Log.info("CapitalFirstPage icon displayed");

			// Click on depositor mobile number field
			wait.until(ExpectedConditions.elementToBeClickable(depositorMobNum));
			sendText(depositorMobNum, usrData.get("MOBILENUMBER"));
			Log.info("Depositor mobile number entered");

			// Selecting company radio button
			if (usrData.get("COMPANY").equalsIgnoreCase("CFL")) {
				clickElement(cflRadioButton);
			} else if (usrData.get("COMPANY").equalsIgnoreCase("HFC")) {
				clickElement(hfcRadioButton);
			}
			Log.info("Company selected");

			// Click on batch id field
			wait.until(ExpectedConditions.elementToBeClickable(batchId));
			batchId.click();
			sendText(batchId, usrData.get("BATCHID"));
			Log.info("Batch Id entered");
			cmsDetailsFromIni("StoreCfBatchId", usrData.get("BATCHID"));

			// Click on Submit button
			wait.until(ExpectedConditions.elementToBeClickable(proceedButton));
			proceedButton.click();
			Log.info("Submit button clicked");

			if (usrData.get("SUBMIT").equalsIgnoreCase("Yes")) {
				wait.until(ExpectedConditions.elementToBeClickable(confirmAmount));
				Assert.assertEquals(tvCompany.getText(), usrData.get("COMPANY"));
				Log.info("Company confirmed as " + tvCompany.getText());
				Assert.assertEquals(tvBatchId.getText(), usrData.get("BATCHID"));
				Log.info("Batch Id confirmed as " + tvBatchId.getText());
				cmsDetailsFromIni("StoreCfAmount", replaceSymbols(confirmAmount.getText()));
				Log.info("Paying amount confirmed as " + confirmAmount.getText());

				proceedButton.click();

				wait.until(ExpectedConditions.visibilityOf(enterMPIN));
				Log.info("MPIN screen displayed");
				if (usrData.get("MPIN").equalsIgnoreCase("Valid")) {
					tapMPINBtn(getAuthfromIni("MPIN"));
				} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
					tapMPINBtn("9999");
				}

				if (usrData.get("MPINSCREENBUTTON").equalsIgnoreCase("Cancel")) {
					mdriver.pressKeyCode(AndroidKeyCode.BACK);
					wait.until(ExpectedConditions.elementToBeClickable(confirmAmount));
					mdriver.pressKeyCode(AndroidKeyCode.BACK);
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
						wait.until(ExpectedConditions.visibilityOf(cfTxnScreen));
						Log.info("Txn screen displayed");

						// Verify the details on transaction screen
						if (cfTxnScreen.getAttribute("text").equalsIgnoreCase("success!")) {
							assertionOnSuccessScreen(usrData);
							wait.until(ExpectedConditions.elementToBeClickable(doneButton));
							doneButton.click();
							Log.info("Done button clicked");
							if (!usrData.get("ASSERTION").equalsIgnoreCase("Processing")) {
								verifyUpdatedBalanceAfterSuccessTxn(usrData, getBalanceFromIni("Get", "retailer", ""));
							}
						} else if (cfTxnScreen.getAttribute("text").equalsIgnoreCase("failed!")) {
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
									wait.until(ExpectedConditions.visibilityOf(cfTxnScreen));
									Log.info("Txn screen displayed");
									assertionOnFailedScreen(usrData);
								}
								exitButton.click();
								Log.info("Exit button clicked");
								verifyUpdatedBalanceAfterFailTxn(usrData, getBalanceFromIni("Get", "retailer", ""));
							} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
								wait.until(ExpectedConditions.elementToBeClickable(cfTxnFailScreenMessage));
								Log.info(cfTxnFailScreenMessage.getText());
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
									tapMPINBtn("1111");
									wait.until(ExpectedConditions.elementToBeClickable(submitMPINBtn));
									submitMPINBtn.click();
									Log.info("Submit button clicked");
									wait.until(ExpectedConditions.visibilityOf(processingScreen));
									Log.info("Processing screen displayed");
									wait.until(ExpectedConditions.visibilityOf(cfTxnScreen));
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
			} else if (usrData.get("SUBMIT").equalsIgnoreCase("Fetch")) {
				wait.until(ExpectedConditions.visibilityOf(confirmAmount));
				mdriver.pressKeyCode(AndroidKeyCode.BACK);
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
		Assert.assertEquals(cfTxnSuccessScreenMessage.getText(), "Deposit to Capital-First success.");
		Log.info(cfTxnSuccessScreenMessage.getText());
	}

	// Verify details on failed screen
	public void assertionOnFailedScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("ASSERTION").equalsIgnoreCase("Invalid MPIN")) {
			Assert.assertEquals(cfTxnFailScreenMessage.getText(), "Authentication Failed Invalid MPIN");
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Insufficient Balance")) {
			Assert.assertEquals(cfTxnFailScreenMessage.getText(), "Insufficient balance");
		} else {
			Assert.assertEquals(cfTxnFailScreenMessage.getText(),
					"Deposit to Capital First failed. Transaction reversed successfully.");
		}
		Log.info(cfTxnFailScreenMessage.getText());
	}

	// SMS assertion
	public void assertionOnSMS(Map<String, String> usrData) throws ClassNotFoundException {
		String successSMS = "Success! Deposit of Rs " + cmsDetailsFromIni("CfAmount", "") + " for BATCH-ID "
				+ cmsDetailsFromIni("CfBatchId", "") + " was successful.";
		String failSMS = "Failure! Deposit of Rs " + cmsDetailsFromIni("CfAmount", "") + " for BATCH-ID "
				+ cmsDetailsFromIni("CfBatchId", "") + " failed.";
		if (usrData.get("ASSERTION").equalsIgnoreCase("Success SMS")) {
			Assert.assertEquals(successSMS, dbUtils.sms());
			Log.info(successSMS);
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Fail SMS")) {
			Assert.assertEquals(failSMS, dbUtils.sms());
			Log.info(successSMS);
		}
	}

	// Assertion after success or orange screen is displayed
	public void verifyUpdatedBalanceAfterSuccessTxn(Map<String, String> usrData, String oldWalletBalance)
			throws ClassNotFoundException {
		double amount = Double.parseDouble(cmsDetailsFromIni("CfAmount", ""));
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