package in.novopay.platform_ui.pages.mobile;

import java.awt.AWTException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import in.novopay.platform_ui.utils.BasePage;
import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.Log;
import in.novopay.platform_ui.utils.ServerUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class RBLAEPSStatusEnquiryPage extends BasePage {
	DBUtils dbUtils = new DBUtils();
	ServerUtils srvUtils = new ServerUtils();
	WebDriverWait wait = new WebDriverWait(mdriver, 30);
	WebDriverWait waitSkip = new WebDriverWait(mdriver, 300);

	DecimalFormat df = new DecimalFormat("#.00");

	@AndroidFindBy(id = "in.novopay.merchant:id/rrn_ref_number")
	MobileElement rrnRefNumber;

	@AndroidFindBy(id = "in.novopay.merchant:id/button")
	MobileElement submitButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'processing...')]")
	MobileElement processingScreen;

	@AndroidFindBy(xpath = "//*[contains(@text,'!')]")
	MobileElement aepsTxnScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_exit")
	MobileElement aepsTxnScreenDoneButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_exit")
	MobileElement aepsTxnScreenExitButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_refund")
	MobileElement aepsTxnScreenRefundButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/aadhaarNumberEdittext")
	MobileElement aadhaarRefundField;

	@AndroidFindBy(id = "in.novopay.merchant:id/txn_title")
	MobileElement refundScreenTitle;

	@AndroidFindBy(id = "in.novopay.merchant:id/start_fps_scan")
	MobileElement scanFingerprint;

	@AndroidFindBy(id = "in.novopay.merchant:id/left_thumb_overlay")
	MobileElement leftThumb;

	@AndroidFindBy(id = "in.novopay.merchant:id/right_thumb_overlay")
	MobileElement rightThumb;

	@AndroidFindBy(id = "fp_scanner_layout_skip")
	MobileElement skipFinger;

	@AndroidFindBy(id = "in.novopay.merchant:id/proceed")
	MobileElement proceedButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'Enter 4 digit PIN')]")
	MobileElement mpinScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/btnDone")
	MobileElement submitMPINBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_exit")
	MobileElement aepsTxnScreenCancelButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_retry")
	MobileElement aepsTxnScreenRetryButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_msg")
	MobileElement aepsTxnSuccessScreenMessage;

	@AndroidFindBy(id = "in.novopay.merchant:id/cash_msg")
	MobileElement remittanceTxnCashMessage;

	@AndroidFindBy(id = "in.novopay.merchant:id/failure_msg")
	MobileElement aepsTxnFailScreenMessage;

	@AndroidFindBy(xpath = "//li[1][contains(@class,'notifications')]//strong")
	MobileElement fcmHeading;

	@AndroidFindBy(xpath = "//li[1][contains(@class,'notifications')]/span[2]")
	MobileElement fcmContent;

	String batchConfigSection = "rblaeps";
	String txnID = "";

	public RBLAEPSStatusEnquiryPage(AndroidDriver<MobileElement> mdriver) {
		super(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver), this);
	}

	// Perform action on page based on navigation key
	public void rBLAEPSStatusEnquiry(Map<String, String> usrData)
			throws InterruptedException, AWTException, IOException, ClassNotFoundException {
		try {
			HashMap<String, String> batchFileConfig = readSectionFromIni(batchConfigSection);
			batchFileConfig = readSectionFromIni(batchConfigSection);
			if (!usrData.get("KEY").isEmpty()) {
				srvUtils.uploadFile(batchFileConfig, usrData.get("KEY"));
			}

			if (usrData.get("STATUS").equalsIgnoreCase("REFUNDED")
					|| usrData.get("ASSERTION").equalsIgnoreCase("Refund Success FCM")) {
				txnID = txnDetailsFromIni("GetTxnRefNo", "");
			} else {
				txnID = dbUtils.aepsRefNum();
			}

			if (usrData.get("STATUS").equalsIgnoreCase("Refund")
					|| usrData.get("STATUS").equalsIgnoreCase("Ready For Refund")) {
				dbUtils.updateAEPSTxn(txnDetailsFromIni("GetTxnRefNo", ""));
			}

			if (usrData.get("ASSERTION").contains("FCM")) {
				assertionOnStatusEnquiryFCM(usrData);
			} else {
				wait.until(ExpectedConditions.elementToBeClickable(rrnRefNumber));
				sendText(rrnRefNumber, txnID);
				Log.info("Status enquiry of " + usrData.get("STATUS") + " transaction");

				wait.until(ExpectedConditions.elementToBeClickable(submitButton));
				submitButton.click();
				Log.info("Submit button clicked");

				wait.until(ExpectedConditions.visibilityOf(processingScreen));
				Log.info("Processing screen displayed");

				wait.until(ExpectedConditions.visibilityOf(aepsTxnScreen));
				Log.info("Txn screen displayed");

				if (aepsTxnScreen.getText().equalsIgnoreCase("Success!")) {
					assertionOnStatusEnquirySuccessScreen(usrData);
					if (usrData.get("ASSERTION").contains("SMS")) {
						assertionOnStatusEnquirySMS(usrData);
					}
					wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenDoneButton));
					aepsTxnScreenDoneButton.click();
					Log.info("Done button clicked");
					verifyUpdatedBalanceAfterRefundSuccessTxn(usrData,
							Double.parseDouble(getBalanceFromIni("Get", "retailer", "")));
				} else if (aepsTxnScreen.getText().equalsIgnoreCase("Failed!")) {
					assertionOnStatusEnquiryFailedScreen(usrData);
					if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
						wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenExitButton));
						aepsTxnScreenExitButton.click();
						Log.info("Exit button clicked");
					} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
						wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenRetryButton));
						aepsTxnScreenRetryButton.click();
						Log.info("Retry button clicked");
						wait.until(ExpectedConditions.elementToBeClickable(submitButton));
						submitButton.click();
						Log.info("Submit button clicked");
						wait.until(ExpectedConditions.visibilityOf(processingScreen));
						Log.info("Processing screen displayed");
						wait.until(ExpectedConditions.visibilityOf(aepsTxnScreen));
						Log.info("Txn screen displayed");
						assertionOnStatusEnquiryFailedScreen(usrData);
					} else if (usrData.get("STATUS").equalsIgnoreCase("Refund")) {
						wait.until(ExpectedConditions.elementToBeClickable(refundScreenTitle));
						wait.until(ExpectedConditions.elementToBeClickable(aadhaarRefundField));
						sendText(aadhaarRefundField, getAadhaarFromIni("GetAadhaarNum"));
						Log.info("Aadhaar Number entered");
						Assert.assertEquals(scanFingerprint.getText(), "Tap to scan fingerprint");
						scanFingerprint.click();
						Log.info("Scan fingerprint button clicked");
						wait.until(ExpectedConditions.elementToBeClickable(leftThumb));
						leftThumb.click();
						rightThumb.click();
						waitSkip.until(ExpectedConditions.elementToBeClickable(skipFinger));
						Log.info("Fingerprints scanned successfully");
						waitSkip.until(ExpectedConditions.elementToBeClickable(proceedButton));
						proceedButton.click();
						Log.info("Proceed button clicked");
						wait.until(ExpectedConditions.elementToBeClickable(submitButton));
						submitButton.click();
						Log.info("Submit button clicked");
						wait.until(ExpectedConditions.elementToBeClickable(mpinScreen));
						if (usrData.get("MPIN").equalsIgnoreCase("Valid")) {
							tapMPINBtn(getAuthfromIni("MPIN"));
						} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
							tapMPINBtn("9999");
						}
						Log.info("MPIN entered");
						submitMPINBtn.click();
						Log.info("MPIN submitted");
						wait.until(ExpectedConditions.visibilityOf(processingScreen));
						Log.info("Processing screen displayed");
						wait.until(ExpectedConditions.visibilityOf(aepsTxnScreen));
						Log.info("Txn screen displayed");
						if (usrData.get("MPIN").equalsIgnoreCase("Valid")) {
							if (!usrData.get("KEY").equalsIgnoreCase("fail")) {
								assertionOnRefundSuccessScreen(usrData);
								Log.info("Refund successful");
								assertionOnRefundSMS(usrData);
								wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenDoneButton));
								aepsTxnScreenDoneButton.click();
								Log.info("Done button clicked");
								verifyUpdatedBalanceAfterRefundSuccessTxn(usrData,
										Double.parseDouble(getBalanceFromIni("Get", "retailer", "")));
							} else {
								assertionOnRefundFailedScreen(usrData);
								Log.info("Refund failed");
								aepsTxnScreenExitButton.click();
								Log.info("Exit button clicked");
							}
						} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
							assertionOnRefundFailedScreen(usrData);
							aepsTxnScreenExitButton.click();
							Log.info("Exit button clicked");
						} else if (usrData.get("MPIN").equalsIgnoreCase("Retry")) {
							assertionOnRefundFailedScreen(usrData);
							aepsTxnScreenRetryButton.click();
							Log.info("Retry button clicked");
							wait.until(ExpectedConditions.elementToBeClickable(scanFingerprint));
							scanFingerprint.click();
							Log.info("Scan fingerprint button clicked");
							wait.until(ExpectedConditions.elementToBeClickable(leftThumb));
							leftThumb.click();
							rightThumb.click();
							waitSkip.until(ExpectedConditions.elementToBeClickable(skipFinger));
							Log.info("Fingerprints scanned successfully");
							waitSkip.until(ExpectedConditions.elementToBeClickable(proceedButton));
							proceedButton.click();
							Log.info("Proceed button clicked");
							wait.until(ExpectedConditions.elementToBeClickable(submitButton));
							submitButton.click();
							Log.info("Submit button clicked");
							wait.until(ExpectedConditions.elementToBeClickable(mpinScreen));
							tapMPINBtn(getAuthfromIni("MPIN"));
							Log.info("MPIN entered");
							submitMPINBtn.click();
							Log.info("MPIN submitted");
							wait.until(ExpectedConditions.visibilityOf(processingScreen));
							Log.info("Processing screen displayed");
							wait.until(ExpectedConditions.visibilityOf(aepsTxnScreen));
							Log.info("Txn screen displayed");
							assertionOnRefundSuccessScreen(usrData);
							Log.info("Refund successful");
							wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenDoneButton));
							aepsTxnScreenDoneButton.click();
							Log.info("Done button clicked");
							verifyUpdatedBalanceAfterRefundSuccessTxn(usrData,
									Double.parseDouble(getBalanceFromIni("Get", "retailer", "")));
						}
					}
				}
			}

		} catch (Exception e) {
			wdriver.navigate().refresh();
			e.printStackTrace();
			Log.info("Test Case Failed");
			Assert.fail();
		}
	}

	public void waitForSpinner() {
		wait.until(ExpectedConditions
				.invisibilityOfElementLocated(By.xpath("//div/ng-busy/div/ng-component/div/div/div[1]")));
		Log.info("Please wait...");
	}

	public void updateTxnStatus() throws ClassNotFoundException {
		dbUtils.updateAxisTransactionStatus(txnID);
		Log.info("Updated RBL txn for refund");
	}

	// Verify details on success screen
	public void assertionOnStatusEnquirySuccessScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("STATUS").equalsIgnoreCase("Success")) {
			Assert.assertEquals(aepsTxnSuccessScreenMessage.getText(),
					"transaction with RRN: " + txnID + " is successful");
			Log.info(aepsTxnSuccessScreenMessage.getText());
		} else if (usrData.get("STATUS").equalsIgnoreCase("Deemed Success")) {
			Assert.assertEquals(aepsTxnSuccessScreenMessage.getText(), "transaction with RRN " + txnID
					+ " is considered as deemed success(91) and amount will be credited to customer shortly.");
			Log.info(aepsTxnSuccessScreenMessage.getText());
		}
		for (int i = 1; i <= 3; i++) {
			String xpath = "//android.widget.LinearLayout[" + i + "]/android.widget.TextView[2]";
			WebElement valuesXpath = mdriver.findElement(By.xpath(xpath));
			if (i == 1) {
				txnDetailsFromIni("StoreTxnRefNo", valuesXpath.getText());
				Log.info("Ref no: " + valuesXpath.getText());
			} else if (i == 2) {
				Assert.assertEquals(replaceSymbols(valuesXpath.getText()), "0.00");
				Log.info("Charges: " + valuesXpath.getText());
				txnDetailsFromIni("StoreCharges", "0.00");
			} else if (i == 3) {
				Log.info("Balance: " + valuesXpath.getText());
			}
		}
	}

	public void assertionOnStatusEnquiryFailedScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("STATUS").equalsIgnoreCase("Failed")) {
			Assert.assertEquals(aepsTxnFailScreenMessage.getText(),
					"Status enquiry - txn has failed: Failed to perform transaction(M3)");
		} else if (usrData.get("STATUS").equalsIgnoreCase("Ready For Refund")
				|| usrData.get("STATUS").equalsIgnoreCase("Refund")) {
			Assert.assertEquals(aepsTxnFailScreenMessage.getText(),
					"AEPS deposit of Rs. " + txnDetailsFromIni("GetTxfAmount", "") + ".00 for mobile "
							+ getAEPSMobNum("GetAEPSMobNum") + " & Txn id " + txnDetailsFromIni("GetTxnRefNo", "")
							+ " on " + dbUtils.aepsStatusEnquiryDate(txnDetailsFromIni("GetTxnRefNo", ""))
							+ " was deemed success before and has failed now. Please refund cash via Refund option.");
		} else if (usrData.get("STATUS").equalsIgnoreCase("Refunded")) {
			Assert.assertEquals(aepsTxnFailScreenMessage.getText(),
					"transaction with rrn " + txnDetailsFromIni("GetTxnRefNo", "") + " has been refunded");
		}
		Log.info(aepsTxnFailScreenMessage.getText());
	}

	// SMS assertion
	public void assertionOnStatusEnquirySMS(Map<String, String> usrData) throws ClassNotFoundException {
		String successSMS = "Cash Deposit: INR " + usrData.get("AMOUNT") + ".00 deposited successfully in your "
				+ usrData.get("BANKNAME") + " a/c linked with Aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + ". Reference Number: "
				+ txnDetailsFromIni("GetTxnRefNo", "") + ", Charges: 0.00, Date: " + dbUtils.aepsTxnDate() + " IST";
		String deemedSuccessSMS = "Cash Deposit of INR " + usrData.get("AMOUNT") + ".00 to your "
				+ usrData.get("BANKNAME") + " a/c linked with Aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12)
				+ " has been initiated and will be processed shortly. Reference Number: "
				+ txnDetailsFromIni("GetTxnRefNo", "") + ", Charges: 0.00, Date: " + dbUtils.aepsTxnDate() + " IST";

		if (usrData.get("ASSERTION").equalsIgnoreCase("Success SMS")) {
			Assert.assertEquals(successSMS, dbUtils.sms());
			Log.info(successSMS);
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Deemed Success SMS")) {
			Assert.assertEquals(deemedSuccessSMS, dbUtils.sms());
			Log.info(deemedSuccessSMS);
		}
	}

	// Remove rupee symbol and comma from the string
	public String replaceComma(String element) {
		String editedElement = element.replaceAll(",", "").trim();
		return editedElement;
	}

	// SMS assertion
	public void assertionOnRefundSMS(Map<String, String> usrData) throws ClassNotFoundException {
		String successRefundSMS = "Dear Customer, Refund for aadhaar  XXXX XXXX "
				+ getAadhaarFromIni("GetAadhaarNum").substring(8, 12) + " and RRN " + txnID
				+ " is successful. Please collect the cash from Aryan";

		if (usrData.get("ASSERTION").equalsIgnoreCase("Success SMS")) {
			Assert.assertEquals(successRefundSMS, dbUtils.sms());
			Log.info(successRefundSMS);
		}
	}

	// FCM assertion
	public void assertionOnStatusEnquiryFCM(Map<String, String> usrData) throws ClassNotFoundException {
		String balance = df.format(Double.parseDouble(dbUtils.getWalletBalance(mobileNumFromIni(), "retailer")));

		String successSEFCMHeading = "Transaction Status enquiry:SUCCESS";
		String failSEFCMHeading = "Transaction Status enquiry:FAIL";
		String successRefundFCMHeading = "Cash Deposit Refund: SUCCESS";
		String failRefundFCMHeading = "Cash Deposit Refund: FAIL";

		String successSEFCMContent = "Request for Transaction status enquiry with RRN " + txnID
				+ " is completed and the status of the transaction is SUCCESS Response code: (00) SUCCESS Reference No: "
				+ txnID + " Balance after txn: " + balance;
		String failSEFCMContent = "Status enquiry - txn has failed: Failed to perform transaction(M3)";
		String deemedSuccessSEFCMContent = "Request for Transaction status enquiry with RRN " + txnID
				+ " is completed and the status of the transaction is DEEMED SUCCESS(Error Code 91)."
				+ " Reference No: " + txnID + " Agent Wallet Balance after txn: " + balance;
		String laterFailedSEFCMContent = "AEPS deposit of Rs. " + txnDetailsFromIni("GetTxfAmount", "")
				+ ".00 for mobile " + getAEPSMobNum("GetAEPSMobNum") + " & Txn id "
				+ txnDetailsFromIni("GetTxnRefNo", "") + " on " + dbUtils.aepsStatusEnquiryDate(txnID)
				+ " was deemed success before and has failed now. Please refund cash via Refund option.";
		String successRefundFCMContent = "Refund for customer with mobile " + getAEPSMobNum("GetAEPSMobNum")
				+ " and RRN " + txnID + " is successful. Please return the cash to customer";
		String failRefundFCMContent = "Refund for customer with Aadhaar XXXX XXXX "
				+ getAadhaarFromIni("GetAadhaarNum").substring(8, 12) + " and RRN " + txnID
				+ " has failed : Failed to perform transaction(M3)";

		switch (usrData.get("ASSERTION")) {
		case "Success FCM":
			fcmMethod(successSEFCMHeading, successSEFCMContent);
			break;
		case "Failed FCM":
			fcmMethod(failSEFCMHeading, failSEFCMContent);
			break;
		case "Deemed Success FCM":
			fcmMethod(successSEFCMHeading, deemedSuccessSEFCMContent);
			break;
		case "Later Failed FCM":
			fcmMethod(failSEFCMHeading, laterFailedSEFCMContent);
			break;
		case "Refund Success FCM":
			fcmMethod(successRefundFCMHeading, successRefundFCMContent);
			break;
		case "Refund Fail FCM":
			fcmMethod(failRefundFCMHeading, failRefundFCMContent);
			break;
		}
	}

	public void fcmMethod(String heading, String content) {
		Assert.assertEquals(fcmHeading.getText(), heading);
		Assert.assertEquals(fcmContent.getText(), content);
		Log.info(fcmHeading.getText());
		Log.info(fcmContent.getText());
	}

	// Verify details on success screen
	public void assertionOnRefundSuccessScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		Assert.assertEquals(aepsTxnSuccessScreenMessage.getText(),
				"Refund for customer with mobile " + getAEPSMobNum("GetAEPSMobNum") + " and RRN " + txnID
						+ " is successful. Please return the cash to customer");
		Log.info(aepsTxnSuccessScreenMessage.getText());
		double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
		for (int i = 1; i <= 2; i++) {
			String xpath = "//android.widget.LinearLayout[" + i + "]/android.widget.TextView[2]";
			WebElement valuesXpath = mdriver.findElement(By.xpath(xpath));
			if (i == 1) {
				txnDetailsFromIni("StoreTxnRefNo", valuesXpath.getText());
				Log.info("Ref no: " + valuesXpath.getText());
			} else if (i == 2) {
				Assert.assertEquals(replaceSymbols(valuesXpath.getText()), usrData.get("AMOUNT") + ".00");
				Log.info("Transferred Amount: " + valuesXpath.getText());
			}
		}
		Assert.assertEquals(replaceComma(remittanceTxnCashMessage.getText()), "cash to be refunded: ₹ " + amount);
		Log.info(remittanceTxnCashMessage.getText());
	}

	// Verify details on failure screen
	public void assertionOnRefundFailedScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("MPIN").equalsIgnoreCase("Invalid") || usrData.get("MPIN").equalsIgnoreCase("Retry")) {
			Assert.assertEquals(aepsTxnFailScreenMessage.getText(), "Authentication Failed Invalid MPIN");
		} else if (usrData.get("KEY").equalsIgnoreCase("fail")) {
			Assert.assertEquals(aepsTxnFailScreenMessage.getText(),
					"Refund for customer with Aadhaar XXXX XXXX " + getAadhaarFromIni("GetAadhaarNum").substring(8, 12)
							+ " and RRN " + txnID + " has failed : Failed to perform transaction(M3)");
		} else if (usrData.get("AADHAAR").equalsIgnoreCase("Invalid")) {
			Assert.assertEquals(aepsTxnFailScreenMessage.getText(), "Customer Aadhaar Number does not match.");
		}
		Log.info(aepsTxnFailScreenMessage.getText());
	}

	// Assertion after success screen is displayed
	public void verifyUpdatedBalanceAfterRefundSuccessTxn(Map<String, String> usrData, double initialWalletBalance)
			throws ClassNotFoundException {
		double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
		double charges = Double.parseDouble(txnDetailsFromIni("GetCharges", ""));
		double comm = Double.parseDouble(txnDetailsFromIni("GetComm", ""));
		double tds = Double.parseDouble(txnDetailsFromIni("GetTds", ""));
		double newWalletBal = initialWalletBalance + amount + charges - comm + tds;
		String newWalletBalance = df.format(newWalletBal);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "retailer"), newWalletBalance + "0000");
		Log.info("Updated Retailer Wallet Balance: " + newWalletBalance);
	}

	// Remove rupee symbol and comma from the string
	public String replaceSymbols(String element) {
		String editedElement = element.replaceAll("₹", "").replaceAll(",", "").trim();
		return editedElement;
	}

	public String mobileNumFromIni() {
		return getLoginMobileFromIni("RBLRetailerMobNum");
	}

	// click on WebElement forcefully
	public void clickElement(WebElement element) {
		try {
			element.click();
		} catch (Exception e) {
			clickInvisibleElement(element);
		}
	}

	// Get otp from Ini file
	public String otpFromIni() {
		return "123456";
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