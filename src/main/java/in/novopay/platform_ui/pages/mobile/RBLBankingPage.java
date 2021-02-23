package in.novopay.platform_ui.pages.mobile;

import java.awt.AWTException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import in.novopay.platform_ui.utils.BasePage;
import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.Log;
import in.novopay.platform_ui.utils.ServerUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class RBLBankingPage extends BasePage {
	DBUtils dbUtils = new DBUtils();
	ServerUtils srvUtils = new ServerUtils();
	DecimalFormat df = new DecimalFormat("#.00");

	WebDriverWait wait = new WebDriverWait(mdriver, 30);
	WebDriverWait waitSkip = new WebDriverWait(mdriver, 300);

	@AndroidFindBy(className = "android.widget.ImageButton")
	MobileElement hamburgerMenu;

	@AndroidFindBy(xpath = "//*contains[@text,'in.novopay.merchant:id/mobile_number')]/parent::android.widget.RelativeLayout")
	MobileElement pageFrame;

	@AndroidFindBy(id = "in.novopay.merchant:id/txn_title")
	MobileElement title;

	@AndroidFindBy(id = "in.novopay.merchant:id/spinner")
	MobileElement dropdown;

	@AndroidFindBy(xpath = "//android.widget.TextView[2]")
	MobileElement dropDownBank;

	@AndroidFindBy(id = "in.novopay.merchant:id/mobileNumberEdittext")
	MobileElement mobNum;

	@AndroidFindBy(id = "in.novopay.merchant:id/aadhaarNumberEdittext")
	MobileElement aadhaarNum;

	@AndroidFindBy(id = "in.novopay.merchant:id/txn_date_time")
	MobileElement amount;

	@AndroidFindBy(xpath = "//*[contains(@text,'OK')]")
	MobileElement okBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/btnDone")
	MobileElement submitMPINBtn;

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

	@AndroidFindBy(xpath = "//app-deposit//h4[contains(text(),'Success!')]")
	MobileElement scanSuccessScreen;

	@AndroidFindBy(xpath = "//app-deposit//div[contains(text(),'Fingerprints scanned successfully')]")
	MobileElement fingerprintSuccess;

	@AndroidFindBy(xpath = "//app-deposit//span[contains(text(),'Tap to scan fingerprint')]")
	MobileElement depositFingerprintUnscanned;

	@AndroidFindBy(xpath = "//app-deposit//button[contains(text(),'Ok')]")
	MobileElement depositFingerprintScreenOkButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'Enter 4 digit PIN')]")
	MobileElement mpinScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/button")
	MobileElement submitButton;

	@AndroidFindBy(xpath = "//*[@type='search']")
	MobileElement dropdownSearch;

	@AndroidFindBy(xpath = "//*[contains(@text,'processing...')]")
	MobileElement processingScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/process_in_background")
	MobileElement processInBackgroundButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'!')]")
	MobileElement aepsTxnScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_exit")
	MobileElement aepsTxnScreenDoneButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_exit")
	MobileElement aepsTxnScreenExitButton;

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

	// Load all objects
	public RBLBankingPage(AndroidDriver<MobileElement> mdriver) {
		super(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver), this);
	}

	// Perform action on page based on given commands
	public void rblBanking(Map<String, String> usrData)
			throws InterruptedException, AWTException, IOException, ClassNotFoundException {

		try {
			HashMap<String, String> batchFileConfig = readSectionFromIni(batchConfigSection);
			batchFileConfig = readSectionFromIni(batchConfigSection);
			if (!usrData.get("KEY").isEmpty()) {
				srvUtils.uploadFile(batchFileConfig, usrData.get("KEY"));
			}

			// Update retailer wallet balance to 0 for scenario where amount > wallet
			if (usrData.get("ASSERTION").equalsIgnoreCase("Amount > Wallet")) {
				if (usrData.get("TXNTYPE").equalsIgnoreCase("Deposit")) {
					dbUtils.updateWalletBalance(mobileNumFromIni(), "retailer", "0");
				} else if (usrData.get("TXNTYPE").equalsIgnoreCase("Withdrawal")) {
					dbUtils.updateWalletBalance(mobileNumFromIni(), "cashout", "0");
				}
			}

			if (usrData.get("ASSERTION").contains("FCM")) {
				if (usrData.get("TXNTYPE").equalsIgnoreCase("Deposit")) {
					assertionOnDepositFCM(usrData);
				} else if (usrData.get("TXNTYPE").equalsIgnoreCase("Withdrawal")) {
					assertionOnWithdrawalFCM(usrData);
				} else if (usrData.get("TXNTYPE").equalsIgnoreCase("Balance Enquiry")) {
					assertionOnBalanceEnquiryFCM(usrData);
				}
			} else {
				wait.until(ExpectedConditions.elementToBeClickable(title));
				Log.info("Title '" + title.getText() + "' displayed");
				if (usrData.get("TXNTYPE").equalsIgnoreCase("Deposit")) {
					Assert.assertEquals(title.getText(), "cash deposit");
					depositTxns(usrData);
				} else if (usrData.get("TXNTYPE").equalsIgnoreCase("Withdrawal")) {
					Assert.assertEquals(title.getText(), "cash withdrawal");
					withdrawalTxns(usrData);
				} else if (usrData.get("TXNTYPE").equalsIgnoreCase("Balance Enquiry")) {
					Assert.assertEquals(title.getText(), "balance enquiry");
					balanceEnquiryTxns(usrData);
				}
			}
		} catch (Exception e) {
			wdriver.navigate().refresh();
			e.printStackTrace();
			Log.info("Test Case Failed");
			Assert.fail();
		}
	}

	// Deposit transaction
	public void depositTxns(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		sendText(mobNum, getAEPSMobNum(usrData.get("MOBNUM")));
		Log.info("Mobile number " + usrData.get("MOBNUM") + " entered");

		sendText(aadhaarNum, getAadhaarFromIni(usrData.get("AADHAAR")));
		Log.info("Aadhaar number " + usrData.get("AADHAAR") + " entered");

		dropdown.click();
		Log.info("Dropdown clicked");
		wait.until(ExpectedConditions.elementToBeClickable(dropDownBank));
		dropDownBank.click();
		Log.info("Allahabad Bank selected");

		Assert.assertEquals(amount.getText(), "enter deposit amount*");
		sendText(amount, usrData.get("AMOUNT"));
		Log.info("Amount " + usrData.get("AMOUNT") + " entered");

		if (usrData.get("SCAN").equalsIgnoreCase("Yes")) {
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
		}

		if (usrData.get("SUBMIT").equalsIgnoreCase("Yes")) {
			wait.until(ExpectedConditions.elementToBeClickable(submitButton));
			submitButton.click();
			Log.info("Submit button clicked");
			if (usrData.get("TXNTYPE").equalsIgnoreCase("Deposit")) {
				wait.until(ExpectedConditions.visibilityOf(mpinScreen));
				Log.info("MPIN screen displayed");
				if (usrData.get("MPIN").equalsIgnoreCase("Valid")) {
					tapMPINBtn(getAuthfromIni("MPIN"));
				} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
					tapMPINBtn("9999");
				}
				if (usrData.get("ASSERTION").equalsIgnoreCase("Insufficient Balance")) {
					dbUtils.updateWalletBalance(mobileNumFromIni(), "retailer", "0");
				}

				if (usrData.get("MPIN").equalsIgnoreCase("Cancel")) {
					mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
					wait.until(ExpectedConditions.elementToBeClickable(amount));
					mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
					wait.until(ExpectedConditions.elementToBeClickable(okBtn));
					okBtn.click();
					Log.info("Home page displayed");
				} else {
					submitMPINBtn.click();
					Log.info("MPIN submitted");
				}
			}

			wait.until(ExpectedConditions.visibilityOf(processingScreen));
			Log.info("Processing screen displayed");

			if (usrData.get("TXNSCREENBUTTON").equals("Process in Background")) {
				wait.until(ExpectedConditions.visibilityOf(processInBackgroundButton));
				processInBackgroundButton.click();
				Log.info("Process in Background button clicked");
			} else {
				wait.until(ExpectedConditions.visibilityOf(aepsTxnScreen));
				Log.info("Txn screen displayed");

				if (aepsTxnScreen.getText().equalsIgnoreCase("Success!")) {
					assertionOnDepositSuccessScreen(usrData);
					if (usrData.get("ASSERTION").contains("SMS")) {
						assertionOnDepositSMS(usrData);
					}
					wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenDoneButton));
					aepsTxnScreenDoneButton.click();
					Log.info("Done button clicked");
					verifyUpdatedBalanceAfterDepositSuccessTxn(usrData,
							Double.parseDouble(getBalanceFromIni("Get", "retailer", "")));
				} else if (aepsTxnScreen.getText().equalsIgnoreCase("Failed!")) {
					assertionOnDepositFailedScreen(usrData);
					if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
						wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenExitButton));
						aepsTxnScreenExitButton.click();
						Log.info("Exit button clicked");
					} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
						wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenRetryButton));
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
						assertionOnDepositFailedScreen(usrData);
						if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
							wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenDoneButton));
							aepsTxnScreenDoneButton.click();
							Log.info("Done button clicked");
						} else {
							wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenExitButton));
							aepsTxnScreenExitButton.click();
							Log.info("Exit button clicked");
						}
					}
					if (usrData.get("ASSERTION").equalsIgnoreCase("Insufficient Balance")) {
						dbUtils.updateWalletBalance(mobileNumFromIni(), "retailer", "1000000");
					} else {
						verifyUpdatedBalanceAfterDepositFailTxn(usrData,
								Double.parseDouble(getBalanceFromIni("Get", "retailer", "")));
					}
				}
			}
		}
	}

	// Withdrawal transaction
	public void withdrawalTxns(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		sendText(mobNum, getAEPSMobNum(usrData.get("MOBNUM")));
		Log.info("Mobile number " + usrData.get("MOBNUM") + " entered");

		sendText(aadhaarNum, getAadhaarFromIni(usrData.get("AADHAAR")));
		Log.info("Aadhaar number " + usrData.get("AADHAAR") + " entered");

		dropdown.click();
		Log.info("Dropdown clicked");
		wait.until(ExpectedConditions.elementToBeClickable(dropDownBank));
		dropDownBank.click();
		Log.info("Allahabad Bank selected");

		Assert.assertEquals(amount.getText(), "enter withdrawal amount*");
		sendText(amount, usrData.get("AMOUNT"));
		Log.info("Amount " + usrData.get("AMOUNT") + " entered");

		if (usrData.get("SCAN").equalsIgnoreCase("Yes")) {
			Assert.assertEquals(scanFingerprint.getText(), "Tap to scan fingerprint");
			scanFingerprint.click();
			Log.info("Scan fingerprint button clicked");
			wait.until(ExpectedConditions.elementToBeClickable(leftThumb));
			leftThumb.click();
			rightThumb.click();
			waitSkip.until(ExpectedConditions.elementToBeClickable(skipFinger));
			Log.info("Fingerprints scanned successfully");
			proceedButton.click();
			Log.info("Proceed button clicked");
		}

		if (usrData.get("SUBMIT").equalsIgnoreCase("Yes")) {
			wait.until(ExpectedConditions.elementToBeClickable(submitButton));
			submitButton.click();
			Log.info("Submit button clicked");

			wait.until(ExpectedConditions.visibilityOf(processingScreen));
			Log.info("Processing screen displayed");

			if (usrData.get("TXNSCREENBUTTON").equals("Process in Background")) {
				wait.until(ExpectedConditions.visibilityOf(processInBackgroundButton));
				processInBackgroundButton.click();
				Log.info("Process in Background button clicked");
			} else {
				wait.until(ExpectedConditions.visibilityOf(aepsTxnScreen));
				Log.info("Txn screen displayed");

				if (aepsTxnScreen.getText().equalsIgnoreCase("Success!")) {
					assertionOnWithdrawalSuccessScreen(usrData);
					if (usrData.get("ASSERTION").contains("SMS")) {
						assertionOnWithdrawalSMS(usrData);
					}
					wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenDoneButton));
					aepsTxnScreenDoneButton.click();
					Log.info("Done button clicked");
					verifyUpdatedBalanceAfterWithdrawalSuccessTxn(usrData,
							Double.parseDouble(getBalanceFromIni("Get", "cashout", "")));
				} else if (aepsTxnScreen.getText().equalsIgnoreCase("Failed!")) {
					assertionOnWithdrawalFailedScreen(usrData);
					if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
						wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenExitButton));
						aepsTxnScreenExitButton.click();
						Log.info("Exit button clicked");
					} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
						wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenRetryButton));
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
						proceedButton.click();
						Log.info("Proceed button clicked");
						wait.until(ExpectedConditions.elementToBeClickable(submitButton));
						submitButton.click();
						Log.info("Submit button clicked");
						wait.until(ExpectedConditions.visibilityOf(processingScreen));
						Log.info("Processing screen displayed");
						wait.until(ExpectedConditions.visibilityOf(aepsTxnScreen));
						Log.info("Txn screen displayed");
						assertionOnWithdrawalFailedScreen(usrData);
						if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
							wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenDoneButton));
							aepsTxnScreenDoneButton.click();
							Log.info("Done button clicked");
						} else {
							wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenExitButton));
							aepsTxnScreenExitButton.click();
							Log.info("Exit button clicked");
						}
					}
					verifyUpdatedBalanceAfterWithdrawalFailTxn(usrData,
							Double.parseDouble(getBalanceFromIni("Get", "cashout", "")));
				}
			}
		}
	}

	// Balance Enquiry transaction
	public void balanceEnquiryTxns(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		sendText(mobNum, getAEPSMobNum(usrData.get("MOBNUM")));
		Log.info("Mobile number " + usrData.get("MOBNUM") + " entered");

		sendText(aadhaarNum, getAadhaarFromIni(usrData.get("AADHAAR")));
		Log.info("Aadhaar number " + usrData.get("AADHAAR") + " entered");

		dropdown.click();
		Log.info("Dropdown clicked");
		wait.until(ExpectedConditions.elementToBeClickable(dropDownBank));
		dropDownBank.click();
		Log.info("Allahabad Bank selected");

		if (usrData.get("SCAN").equalsIgnoreCase("Yes")) {
			Assert.assertEquals(scanFingerprint.getText(), "Tap to scan fingerprint");
			scanFingerprint.click();
			Log.info("Scan fingerprint button clicked");
			wait.until(ExpectedConditions.elementToBeClickable(leftThumb));
			leftThumb.click();
			rightThumb.click();
			waitSkip.until(ExpectedConditions.elementToBeClickable(skipFinger));
			Log.info("Fingerprints scanned successfully");
			proceedButton.click();
			Log.info("Proceed button clicked");
		}

		if (usrData.get("SUBMIT").equalsIgnoreCase("Yes")) {
			wait.until(ExpectedConditions.elementToBeClickable(submitButton));
			submitButton.click();
			Log.info("Submit button clicked");

			wait.until(ExpectedConditions.visibilityOf(processingScreen));
			Log.info("Processing screen displayed");

			if (usrData.get("TXNSCREENBUTTON").equals("Process in Background")) {
				wait.until(ExpectedConditions.visibilityOf(processInBackgroundButton));
				processInBackgroundButton.click();
				Log.info("Process in Background button clicked");
			} else {
				wait.until(ExpectedConditions.visibilityOf(aepsTxnScreen));
				Log.info("Txn screen displayed");

				if (aepsTxnScreen.getText().equalsIgnoreCase("Success!")) {
					assertionOnBalanceEnquirySuccessScreen(usrData);
					if (usrData.get("ASSERTION").contains("SMS")) {
						assertionOnDepositSMS(usrData);
					}
					wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenDoneButton));
					aepsTxnScreenDoneButton.click();
					Log.info("Done button clicked");
					verifyUpdatedBalanceAfterBalanceEnquirySuccessTxn(usrData,
							Double.parseDouble(getBalanceFromIni("Get", "cashout", "")));
				} else if (aepsTxnScreen.getText().equalsIgnoreCase("Failed!")) {
					assertionOnBalanceEnquiryFailedScreen(usrData);
					if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
						wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenExitButton));
						aepsTxnScreenExitButton.click();
						Log.info("Exit button clicked");
					} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
						wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenRetryButton));
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
						proceedButton.click();
						Log.info("Proceed button clicked");
						wait.until(ExpectedConditions.elementToBeClickable(submitButton));
						submitButton.click();
						Log.info("Submit button clicked");
						wait.until(ExpectedConditions.visibilityOf(processingScreen));
						Log.info("Processing screen displayed");
						wait.until(ExpectedConditions.visibilityOf(aepsTxnScreen));
						Log.info("Txn screen displayed");
						assertionOnBalanceEnquiryFailedScreen(usrData);
						wait.until(ExpectedConditions.elementToBeClickable(aepsTxnScreenExitButton));
						aepsTxnScreenExitButton.click();
						Log.info("Exit button clicked");
					}
					verifyUpdatedBalanceAfterBalanceEnquiryFailTxn(usrData,
							Double.parseDouble(getBalanceFromIni("Get", "cashout", "")));
				}
			}
		}
	}

	public void dropdownSelect(Map<String, String> usrData) {
		String dropdownXpath = "//li[contains(text(),'" + usrData.get("BANKNAME") + "')]";
		WebElement dropdownValue = wdriver.findElement(By.xpath(dropdownXpath));
		dropdownValue.click();
	}

	// Get Partner name
	public String partner() {
		return "RBL";
	}

	// Get mobile number from Ini file
	public String mobileNumFromIni() {
		return getLoginMobileFromIni(partner().toUpperCase() + "RetailerMobNum");
	}

	// Verify details on success screen
	public void assertionOnDepositSuccessScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("ASSERTION").contains("Deemed Success")) {
			Assert.assertEquals(aepsTxnSuccessScreenMessage.getText(),
					"cash deposited successfully (deemed success:error code 91) to customer account linked with aadhaar number XXXX XXXX 7263");
			Log.info(aepsTxnSuccessScreenMessage.getText());
		} else {
			Assert.assertEquals(aepsTxnSuccessScreenMessage.getText(),
					"cash deposited successfully to customer account linked with aadhaar number XXXX XXXX 7263");
			Log.info(aepsTxnSuccessScreenMessage.getText());
		}
		for (int i = 1; i <= 4; i++) {
			String xpath = "//android.widget.LinearLayout[" + i + "]/android.widget.TextView[2]";
			WebElement valuesXpath = mdriver.findElement(By.xpath(xpath));
			if (i == 1) {
				txnDetailsFromIni("StoreTxnRefNo", valuesXpath.getText());
				Log.info("Ref no: " + valuesXpath.getText());
			} else if (i == 2) {
				Assert.assertEquals(replaceSymbols(valuesXpath.getText()), usrData.get("AMOUNT") + ".00");
				Log.info("Transferred Amount: " + valuesXpath.getText());
				txnDetailsFromIni("StoreTxfAmount", usrData.get("AMOUNT"));
			} else if (i == 3) {
				Assert.assertEquals(replaceSymbols(valuesXpath.getText()), "0.00");
				Log.info("Charges: " + valuesXpath.getText());
				txnDetailsFromIni("StoreCharges", "0.00");
			} else if (i == 4) {
				Log.info("Balance: " + valuesXpath.getText());
			}
		}
		String comm = dbUtils.getAepsComm(usrData.get("AMOUNT"), "Deposit");
		txnDetailsFromIni("StoreComm", comm);
		double tds = Double.parseDouble(comm) * Double.parseDouble(dbUtils.getTDSPercentage(mobileNumFromIni()))
				/ 10000;
		txnDetailsFromIni("StoreTds", String.format("%.2f", tds));
		double amount = Double.parseDouble(usrData.get("AMOUNT"));
		double charges = 0.00;
		double totalAmount = amount + charges;
		String cashToBeCollected = df.format(totalAmount);
		Assert.assertEquals(replaceComma(remittanceTxnCashMessage.getText()),
				"cash to be collected: ₹ " + cashToBeCollected);
		Log.info(remittanceTxnCashMessage.getText());
	}

	// Verify details on failure screen
	public void assertionOnDepositFailedScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("ASSERTION").equalsIgnoreCase("Insufficient Balance")) {
			Assert.assertEquals(aepsTxnFailScreenMessage.getText(),
					"Agent Wallet Debit Failed :Insufficient account balance.");
		} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
			Assert.assertEquals(aepsTxnFailScreenMessage.getText(), "Authentication Failed Invalid Agent MPIN");
		} else {
			Assert.assertEquals(aepsTxnFailScreenMessage.getText(),
					"deposit for customer account linked with aadhaar no. XXXX XXXX 7263 has failed : Error - Failed to perform transaction(M3)");
		}
		Log.info(aepsTxnFailScreenMessage.getText());
	}

	// Assertion after success screen is displayed
	public void verifyUpdatedBalanceAfterDepositSuccessTxn(Map<String, String> usrData, double initialWalletBalance)
			throws ClassNotFoundException {
		double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
		double charges = Double.parseDouble(txnDetailsFromIni("GetCharges", ""));
		double comm = Double.parseDouble(txnDetailsFromIni("GetComm", ""));
		double tds = Double.parseDouble(txnDetailsFromIni("GetTds", ""));
		double newWalletBal = initialWalletBalance - amount - charges + comm - tds;
		String newWalletBalance = df.format(newWalletBal);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "retailer"), newWalletBalance + "0000");
		Log.info("Updated Retailer Wallet Balance: " + newWalletBalance);
	}

	// Assertion after failure screen is displayed
	public void verifyUpdatedBalanceAfterDepositFailTxn(Map<String, String> usrData, double initialWalletBalance)
			throws ClassNotFoundException {
		String newWalletBalance = df.format(initialWalletBalance);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "retailer"), newWalletBalance + "0000");
		Log.info("Updated Retailer Wallet Balance: " + newWalletBalance);
	}

	// SMS assertion
	public void assertionOnDepositSMS(Map<String, String> usrData) throws ClassNotFoundException {
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

	// FCM assertion
	public void assertionOnDepositFCM(Map<String, String> usrData) throws ClassNotFoundException {
		String successFCMHeading = "Cash Deposit: SUCCESS";
		String failFCMHeading = "Cash Deposit: FAIL";

		String balance = df.format(Double.parseDouble(dbUtils.getWalletBalance(mobileNumFromIni(), "retailer")));
		String successFCMContent = "Request of cash deposit: INR " + usrData.get("AMOUNT") + ".00 deposited in "
				+ usrData.get("BANKNAME") + " a/c linked with Aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + "; Charges: INR 0.00; Date " + dbUtils.aepsTxnDate()
				+ " IST Response code:(00)SUCCESS Reference No: " + txnDetailsFromIni("GetTxnRefNo", "")
				+ " Agent Wallet Balance after txn: INR " + balance;
		String deemedSuccessFCMContent = "Request of cash deposit: INR " + usrData.get("AMOUNT") + ".00 deposited in "
				+ usrData.get("BANKNAME") + " a/c linked with Aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + "; Charges: INR 0.00; Date " + dbUtils.aepsTxnDate()
				+ " IST Response code:(Error Code 91)SUCCESS Reference No: " + txnDetailsFromIni("GetTxnRefNo", "")
				+ " Agent Wallet Balance after txn: INR " + balance;
		String failFCMContent = "deposit for customer account linked with aadhaar no.  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + " has failed : Error - Failed to perform transaction(M3)";

		String fcmHeadingxpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView[1]";
		String fcmContentxpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView[2]";
		WebElement fcmHeading = mdriver.findElement(By.xpath(fcmHeadingxpath));
		WebElement fcmContent = mdriver.findElement(By.xpath(fcmContentxpath));

		switch (usrData.get("ASSERTION")) {
		case "Success FCM":
			Assert.assertEquals(fcmHeading.getText(), successFCMHeading);
			Assert.assertEquals(fcmContentString(fcmContent.getText(), 0, 153, 154, 179, 180, 206, 207, 252),
					successFCMContent);
			break;
		case "Deemed Success FCM":
			Assert.assertEquals(fcmHeading.getText(), successFCMHeading);
			Assert.assertEquals(fcmContentString(fcmContent.getText(), 0, 153, 154, 190, 191, 217, 218, 263),
					deemedSuccessFCMContent);
			break;
		case "Failed FCM":
			Assert.assertEquals(fcmHeading.getText(), failFCMHeading);
			Assert.assertEquals(fcmContent.getText(), failFCMContent);
			break;
		}
		Log.info(fcmHeading.getText());
		Log.info(fcmContent.getText());
		mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
	}

	// Verify details on success screen
	public void assertionOnWithdrawalSuccessScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		Assert.assertEquals(aepsTxnSuccessScreenMessage.getText(),
				"Cash withdrawn successfully from customer with aadhaar number XXXX XXXX 7263");
		Log.info(aepsTxnSuccessScreenMessage.getText());
		for (int i = 1; i <= 4; i++) {
			String xpath = "//android.widget.LinearLayout[" + i + "]/android.widget.TextView[2]";
			WebElement valuesXpath = mdriver.findElement(By.xpath(xpath));
			if (i == 1) {
				txnDetailsFromIni("StoreTxnRefNo", valuesXpath.getText());
				Log.info("Ref no: " + valuesXpath.getText());
			} else if (i == 2) {
				Assert.assertEquals(replaceSymbols(valuesXpath.getText()), usrData.get("AMOUNT") + ".00");
				Log.info("Transferred Amount: " + valuesXpath.getText());
				txnDetailsFromIni("StoreTxfAmount", usrData.get("AMOUNT"));
			} else if (i == 3) {
				Assert.assertEquals(replaceSymbols(valuesXpath.getText()), "0.00");
				Log.info("Charges: " + valuesXpath.getText());
				txnDetailsFromIni("StoreCharges", "0.00");
			} else if (i == 4) {
				Log.info("Balance: " + valuesXpath.getText());
			}
		}
		String comm = dbUtils.getAepsComm(usrData.get("AMOUNT"), "Withdrawal");
		txnDetailsFromIni("StoreComm", comm);
		double tds = Double.parseDouble(comm) * Double.parseDouble(dbUtils.getTDSPercentage(mobileNumFromIni()))
				/ 10000;
		txnDetailsFromIni("StoreTds", String.format("%.2f", tds));
		double amount = Double.parseDouble(usrData.get("AMOUNT"));
		double charges = 0.00;
		double totalAmount = amount + charges;
		String cashToBeCollected = df.format(totalAmount);
		Assert.assertEquals(replaceComma(remittanceTxnCashMessage.getText()),
				"cash to be paid: ₹ " + cashToBeCollected);
		Log.info(remittanceTxnCashMessage.getText());
	}

	// Verify details on failure screen
	public void assertionOnWithdrawalFailedScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		Assert.assertEquals(aepsTxnFailScreenMessage.getText(),
				"Withdrawal for customer with aadhaar XXXX XXXX 7263 has failed : Failed to perform transaction(M3)");
		Log.info(aepsTxnFailScreenMessage.getText());
	}

	// Assertion after success screen is displayed
	public void verifyUpdatedBalanceAfterWithdrawalSuccessTxn(Map<String, String> usrData, double initialWalletBalance)
			throws ClassNotFoundException {
		double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
		double charges = Double.parseDouble(txnDetailsFromIni("GetCharges", ""));
		double comm = Double.parseDouble(txnDetailsFromIni("GetComm", ""));
		double tds = Double.parseDouble(txnDetailsFromIni("GetTds", ""));
		double newWalletBal = initialWalletBalance + amount - charges + comm - tds;
		String newWalletBalance = df.format(newWalletBal);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "cashout"), newWalletBalance + "0000");
		Log.info("Updated Cashout Wallet Balance: " + newWalletBalance);
	}

	// Assertion after failure screen is displayed
	public void verifyUpdatedBalanceAfterWithdrawalFailTxn(Map<String, String> usrData, double initialWalletBalance)
			throws ClassNotFoundException {
		String newWalletBalance = df.format(initialWalletBalance);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "cashout"), newWalletBalance + "0000");
		Log.info("Updated Cashout Wallet Balance: " + newWalletBalance);
	}

	// SMS assertion
	public void assertionOnWithdrawalSMS(Map<String, String> usrData) throws ClassNotFoundException {
		String successSMS = "Cash Withdrawal: INR " + usrData.get("AMOUNT") + ".00 withdrawn from your "
				+ usrData.get("BANKNAME") + " a/c linked to Aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + ". Reference Number: "
				+ txnDetailsFromIni("GetTxnRefNo", "") + ", Charges: 0.00, Date: " + dbUtils.aepsTxnDate() + " IST";

		if (usrData.get("ASSERTION").equalsIgnoreCase("Success SMS")) {
			Assert.assertEquals(successSMS, dbUtils.sms());
			Log.info(successSMS);
		}
	}

	// FCM assertion
	public void assertionOnWithdrawalFCM(Map<String, String> usrData) throws ClassNotFoundException {
		String successFCMHeading = "Cash Withdrawal: SUCCESS";
		String failFCMHeading = "Cash Withdrawal: FAIL";

		String balance = df.format(Double.parseDouble(dbUtils.getWalletBalance(mobileNumFromIni(), "cashout")));
		String successFCMContent = "Request for cash withdrawal: INR " + usrData.get("AMOUNT")
				+ ".00 withdrawn from your " + usrData.get("BANKNAME") + " a/c linked with Aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + "; Charges INR 0.00; Date " + dbUtils.aepsTxnDate()
				+ " IST Response code: (00) SUCCESS Reference No: " + txnDetailsFromIni("GetTxnRefNo", "")
				+ " Balance after txn: INR " + balance;
		String failFCMContent = "Withdrawal for customer with aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + " has failed : Failed to perform transaction(M3)";

		String fcmHeadingxpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView[1]";
		String fcmContentxpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView[2]";
		WebElement fcmHeading = mdriver.findElement(By.xpath(fcmHeadingxpath));
		WebElement fcmContent = mdriver.findElement(By.xpath(fcmContentxpath));

		switch (usrData.get("ASSERTION")) {
		case "Success FCM":
			Assert.assertEquals(fcmHeading.getText(), successFCMHeading);
			Assert.assertEquals(fcmContentString(fcmContent.getText(), 0, 163, 164, 191, 192, 218, 219, 252),
					successFCMContent);
			break;
		case "Failed FCM":
			Assert.assertEquals(fcmHeading.getText(), failFCMHeading);
			Assert.assertEquals(fcmContent.getText(), failFCMContent);
			break;
		}
		Log.info(fcmHeading.getText());
		Log.info(fcmContent.getText());
		mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
	}

	// Verify details on success screen
	public void assertionOnBalanceEnquirySuccessScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		Assert.assertEquals(aepsTxnSuccessScreenMessage.getText(),
				"balance enquiry for customer account linked with aadhaar XXXX XXXX 7263 - successful");
		Log.info(aepsTxnSuccessScreenMessage.getText());
		for (int i = 1; i <= 2; i++) {
			String xpath = "//android.widget.LinearLayout[" + i + "]/android.widget.TextView[2]";
			WebElement valuesXpath = mdriver.findElement(By.xpath(xpath));
			if (i == 1) {
				txnDetailsFromIni("StoreTxnRefNo", valuesXpath.getText());
				Log.info("Ref no: " + valuesXpath.getText());
			} else if (i == 2) {
				Assert.assertEquals(replaceSymbols(valuesXpath.getText()), "1511.00");
				Log.info("Customer balance: " + valuesXpath.getText());
			}
		}
	}

	// Verify details on failure screen
	public void assertionOnBalanceEnquiryFailedScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		Assert.assertEquals(aepsTxnFailScreenMessage.getText(),
				"Balance Enquiry for customer with Aadhaar XXXX XXXX 7263 has failed : Failed to perform transaction(M3)");
		Log.info(aepsTxnFailScreenMessage.getText());
	}

	// Assertion after success screen is displayed
	public void verifyUpdatedBalanceAfterBalanceEnquirySuccessTxn(Map<String, String> usrData,
			double initialWalletBalance) throws ClassNotFoundException {
		String newWalletBalance = df.format(initialWalletBalance);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "cashout"), newWalletBalance + "0000");
		Log.info("Updated Cashout Wallet Balance: " + newWalletBalance);
	}

	// Assertion after failure screen is displayed
	public void verifyUpdatedBalanceAfterBalanceEnquiryFailTxn(Map<String, String> usrData, double initialWalletBalance)
			throws ClassNotFoundException {
		String newWalletBalance = df.format(initialWalletBalance);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "cashout"), newWalletBalance + "0000");
		Log.info("Updated Cashout Wallet Balance: " + newWalletBalance);
	}

	// SMS assertion
	public void assertionOnBalanceEnquirySMS(Map<String, String> usrData) throws ClassNotFoundException {
		String successSMS = "Balance in " + usrData.get("BANKNAME") + " a/c linked with Aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + " as on " + dbUtils.aepsTxnDate()
				+ " IST is Led Bal: INR 1511.00, Ava Bal: INR 1511.00, Ref No: " + txnDetailsFromIni("GetTxnRefNo", "");

		if (usrData.get("ASSERTION").equalsIgnoreCase("Success SMS")) {
			Assert.assertEquals(successSMS, dbUtils.sms());
			Log.info(successSMS);
		}
	}

	// FCM assertion
	public void assertionOnBalanceEnquiryFCM(Map<String, String> usrData) throws ClassNotFoundException {
		String successFCMHeading = "Balance Enquiry: SUCCESS";
		String failFCMHeading = "Balance Enquiry: FAIL";

		String successFCMContent = "Balance in " + usrData.get("BANKNAME") + " a/c linked with Aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + " as on " + dbUtils.aepsTxnDate()
				+ " IST is Led Bal: 1511.00, Ava Bal: 1511.00 Response code: (00) SUCCESS Reference No: "
				+ txnDetailsFromIni("GetTxnRefNo", "");
		String failFCMContent = "Balance Enquiry for customer with Aadhaar  XXXX XXXX "
				+ usrData.get("AADHAAR").substring(8, 12) + " has failed : Failed to perform transaction(M3)";

		String fcmHeadingxpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView[1]";
		String fcmContentxpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView[2]";
		WebElement fcmHeading = mdriver.findElement(By.xpath(fcmHeadingxpath));
		WebElement fcmContent = mdriver.findElement(By.xpath(fcmContentxpath));
		
		switch (usrData.get("ASSERTION")) {
		case "Success FCM":
			Assert.assertEquals(fcmHeading.getText(), successFCMHeading);
			Assert.assertEquals(fcmContentString(fcmContent.getText(), 0, 131, 132, 159, 160, 186, 0, 0),
					successFCMContent);
			break;
		case "Failed FCM":
			Assert.assertEquals(fcmHeading.getText(), failFCMHeading);
			Assert.assertEquals(fcmContent.getText(), failFCMContent);
			break;
		}
		Log.info(fcmHeading.getText());
		Log.info(fcmContent.getText());
		mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
	}

	public String fcmContentString(String fcm, int a, int b, int c, int d, int e, int f, int g, int h) {
		String fcmContent = "";
		if (e == 0 && f == 0 && g == 0 && h == 0) {
			fcmContent = fcm.substring(a, b) + " " + fcm.substring(c, d);
		} else if (g == 0 && h == 0) {
			fcmContent = fcm.substring(a, b) + " " + fcm.substring(c, d) + " " + fcm.substring(e, f);
		} else {
			fcmContent = fcm.substring(a, b) + " " + fcm.substring(c, d) + " " + fcm.substring(e, f) + " "
					+ fcm.substring(g, h);
		}
		return fcmContent;
	}

	// Wait for screen to complete loading
	public void waitForSpinner() {
		wait.until(ExpectedConditions
				.invisibilityOfElementLocated(By.xpath("//div/ng-busy/div/ng-component/div/div/div[1]")));
		Log.info("Please wait...");
	}

	// Remove rupee symbol and comma from the string
	public String replaceSymbols(String element) {
		String editedElement = element.replaceAll("₹", "").replaceAll(",", "").replaceAll(" ", "").trim();
		return editedElement;
	}

	// Remove rupee symbol and comma from the string
	public String replaceComma(String element) {
		String editedElement = element.replaceAll(",", "").trim();
		return editedElement;
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
