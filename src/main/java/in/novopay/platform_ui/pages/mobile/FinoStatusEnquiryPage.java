package in.novopay.platform_ui.pages.mobile;

import java.awt.AWTException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;

import in.novopay.platform_ui.utils.BasePage;
import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.Log;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class FinoStatusEnquiryPage extends BasePage {
	DBUtils dbUtils = new DBUtils();

	WebDriverWait wait = new WebDriverWait(mdriver, 30);
	WebDriverWait waitWelcome = new WebDriverWait(mdriver, 3);

	DecimalFormat df = new DecimalFormat("#.00");

	@AndroidFindBy(id = "in.novopay.merchant:id/et_MobileNo")
	MobileElement statusEnquiryMobNum;

	@AndroidFindBy(id = "in.novopay.merchant:id/et_RefNo")
	MobileElement statusEnquiryTxnId;

	@AndroidFindBy(id = "in.novopay.merchant:id/button")
	MobileElement submitButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/txn_title")
	MobileElement statusEnquiryScreen;

	@AndroidFindBy(xpath = "//android.support.v7.widget.RecyclerView/android.widget.LinearLayout")
	MobileElement statusEnquiryResult;

	@AndroidFindBy(xpath = "//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]")
	MobileElement statusEnquiryResult1;

	@AndroidFindBy(id = "in.novopay.merchant:id/et_RefNo")
	MobileElement seRefNo;

	@AndroidFindBy(id = "in.novopay.merchant:id/textView8")
	MobileElement seMessageType;

	@AndroidFindBy(id = "in.novopay.merchant:id/textViewTransferSuccessToBener")
	MobileElement seMessage;

	@AndroidFindBy(id = "in.novopay.merchant:id/textView7")
	MobileElement seFailMessage;

	@AndroidFindBy(id = "in.novopay.merchant:id/responseCodeTextView")
	MobileElement seTimeoutMessage;

	@AndroidFindBy(id = "in.novopay.merchant:id/textViewDate")
	MobileElement seDateTime;

	@AndroidFindBy(id = "in.novopay.merchant:id/textView_amount")
	MobileElement seAmount;

	@AndroidFindBy(id = "in.novopay.merchant:id/textViewAmount")
	MobileElement enquiryAmount;

	@AndroidFindBy(id = "in.novopay.merchant:id/text_view_charges")
	MobileElement seCharges;

	@AndroidFindBy(id = "in.novopay.merchant:id/textViewCharges")
	MobileElement enquiryCharges;

	@AndroidFindBy(id = "in.novopay.merchant:id/button")
	MobileElement seButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'OK')]")
	MobileElement okBtn;

	@AndroidFindBy(id = "android:id/alertTitle")
	MobileElement confirmRefund;

	@AndroidFindBy(id = "android:id/button1")
	MobileElement confirmRefundProceedBtn;

	@AndroidFindBy(id = "android:id/button2")
	MobileElement confirmRefundDismissBtn;

	@AndroidFindBy(xpath = "//*[contains(@id,'in.novopay.merchant:id/cash_msg')]/parent::android.widget.ListView/android.widget.LinearLayout[1]/android.widget.TextView[2]")
	MobileElement successScreenValues;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_exit")
	MobileElement doneButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_exit")
	MobileElement exitButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_retry")
	MobileElement retryButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/cancel")
	MobileElement cancelOTPBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/otp_edit_text")
	MobileElement otpTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/resend_otp")
	MobileElement resendOTPBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/submit_otp")
	MobileElement submitOTPBtn;

	@AndroidFindBy(xpath = "//*[contains(@text,'!')]")
	MobileElement refundTxnScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_msg")
	MobileElement successMsg;

	@AndroidFindBy(id = "in.novopay.merchant:id/failure_msg")
	MobileElement failureMsg;

	@AndroidFindBy(xpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView[1]")
	MobileElement fcmHeading;

	@AndroidFindBy(xpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView[2]")
	MobileElement fcmContent;

	@AndroidFindBy(accessibility = "Navigate up")
	MobileElement backButton;

	String txnID = txnDetailsFromIni("GetTxnRefNo", "");

	public FinoStatusEnquiryPage(AndroidDriver<MobileElement> mdriver) {
		super(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver), this);
	}

	// Perform action on page based on navigation key
	public void finoStatusEnquiry(Map<String, String> usrData)
			throws InterruptedException, AWTException, IOException, ClassNotFoundException {
		try {
			if (usrData.get("STATUS").equalsIgnoreCase("To_Be_Refunded")
					|| usrData.get("STATUS").equalsIgnoreCase("Refund")) {
				updateTxnStatus("3");
			} else if (usrData.get("STATUS").equalsIgnoreCase("Timeout")) {
				updateTxnStatus("1");
			} else if (usrData.get("STATUS").equalsIgnoreCase("Queued")) {
				dbUtils.updateBatchStatus("DisableRemittanceQueuing", "STOPPED");
			}
			if (usrData.get("ASSERTION").contains("FCM")) {
				assertionOnRefundFCM(usrData);
			} else {
				if (usrData.get("TXNDETAILS").equalsIgnoreCase("MobNum")) {
					sendText(statusEnquiryMobNum, getCustomerDetailsFromIni("ExistingNum"));
					Log.info("Customer mobile number entered");
				} else if (usrData.get("TXNDETAILS").equalsIgnoreCase("TxnID")) {
					sendText(statusEnquiryTxnId, txnID);
					Log.info("Txn ID entered");
				} else {
					sendText(statusEnquiryTxnId, usrData.get("TXNDETAILS"));
				}
				Thread.sleep(1000);
				wait.until(ExpectedConditions.elementToBeClickable(submitButton));
				submitButton.click();
				Log.info("Submit button clicked");
				if (usrData.get("TXNDETAILS").equalsIgnoreCase("11112222")) {
//				wait.until(ExpectedConditions.visibilityOf(toasterMsg));
//				Assert.assertEquals("No Transaction available", toasterMsg.getText());
				} else {
					selectTxn(usrData);
					Log.info("Status enquiry of " + usrData.get("STATUS") + " Transaction");
					wait.until(ExpectedConditions.visibilityOf(seRefNo));
					assertionOnTxnScreen(usrData);
					if (usrData.get("STATUS").equalsIgnoreCase("Success")
							|| usrData.get("STATUS").equalsIgnoreCase("Auto-Refunded")
							|| usrData.get("STATUS").equalsIgnoreCase("Late-Refunded")
							|| usrData.get("STATUS").equalsIgnoreCase("Timeout")
							|| usrData.get("STATUS").equalsIgnoreCase("Queued")) {
						Assert.assertEquals(seButton.getText().toUpperCase(), "FINISH");
						seButton.click();
						Log.info("Finish button clicked");
					} else if (usrData.get("STATUS").equalsIgnoreCase("To_Be_Refunded")) {
						Assert.assertEquals(seButton.getText().toUpperCase(), "REFUND");
					} else if (usrData.get("STATUS").equalsIgnoreCase("Refund")) {
						Assert.assertEquals(seButton.getText().toUpperCase(), "REFUND");
						seButton.click();
						Log.info("Refund button clicked");
						wait.until(ExpectedConditions.visibilityOf(confirmRefund));
						confirmRefundProceedBtn.click();
						Log.info("Proceed button clicked");
						otpScreen(usrData);
					}
				}
				if (!usrData.get("STATUS").equalsIgnoreCase("Refund")
						|| usrData.get("OTP").equalsIgnoreCase("Cancel")) {
					if (usrData.get("STATUS").equalsIgnoreCase("To_Be_Refunded")
							|| usrData.get("STATUS").equalsIgnoreCase("Refund")) {
						mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
						wait.until(ExpectedConditions.elementToBeClickable(statusEnquiryResult));
					}
					if (!usrData.get("STATUS").equalsIgnoreCase("NA")) {
						mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
						wait.until(ExpectedConditions.elementToBeClickable(statusEnquiryMobNum));
					}
					mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
					wait.until(ExpectedConditions.elementToBeClickable(okBtn));
					okBtn.click();
					Log.info("Home page displayed");
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

	public void updateTxnStatus(String value) throws ClassNotFoundException {
		dbUtils.updateRBLTransactionStatus(txnID, value);
		Log.info("Updated RBL txn for refund");
	}

	public void selectTxn(Map<String, String> usrData) throws ClassNotFoundException {
		wait.until(ExpectedConditions.elementToBeClickable(statusEnquiryResult));
		int i = 0;
		int result = mdriver
				.findElements(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout")).size();
		String resultXpath = "";
		if (result != 1) {
			resultXpath = "//android.widget.LinearLayout";
		} else {
			resultXpath = "//android.widget.LinearLayout[1]";
		}

		for (i = 2; i <= 4; i++) {
			String resultXpathFinal = resultXpath + "/android.widget.TextView[" + i + "]";
			WebElement resultElement = mdriver.findElement(By.xpath(resultXpathFinal));
			if (i == 2) {
				Assert.assertEquals(resultElement.getText(), "Reference Number : " + dbUtils.selectPaymentRefCode());
			} else if (i == 3) {
				if (!usrData.get("STATUS").equalsIgnoreCase("Auto-Refunded")) {
					Assert.assertEquals(resultElement.getText(), "amount : ₹ " + txnDetailsFromIni("GetTxfAmount", "")
							+ ".00 to " + getBeneNameFromIni("GetBeneName"));
				} else {
					Assert.assertEquals(resultElement.getText(), "amount : ₹ " + txnDetailsFromIni("GetFailAmount", "")
							+ ".00 to " + getBeneNameFromIni("GetBeneName"));
				}
			} else if (i == 4) {
				Assert.assertEquals(resultElement.getText(),
						"AC no-" + getAccountNumberFromIni("GetNum") + ", " + getBankNameFromIni("GetBankName"));
			}
		}
		statusEnquiryResult.click();
	}

	// Verify details on txn screen
	public void assertionOnTxnScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("STATUS").equalsIgnoreCase("Success") || usrData.get("STATUS").equalsIgnoreCase("Timeout")) {
			Assert.assertEquals(seMessageType.getText(), "success !");
			Log.info(seMessageType.getText());
			Assert.assertEquals(seMessage.getText().substring(0, 33) + " " + seMessage.getText().substring(34),
					"money transferred successfully to " + getBeneNameFromIni("GetBeneName"));
			Log.info(seMessage.getText());
		} else if (usrData.get("STATUS").equalsIgnoreCase("Auto-Refunded")
				|| usrData.get("STATUS").equalsIgnoreCase("Late-Refunded")) {
			Assert.assertEquals(seMessageType.getText(), "refunded !");
			Log.info(seMessageType.getText());
			Assert.assertEquals(seFailMessage.getText(), "Amount is already refunded to customer");
			Log.info(seFailMessage.getText());
		} else if (usrData.get("STATUS").equalsIgnoreCase("To_Be_Refunded")
				|| usrData.get("STATUS").equalsIgnoreCase("Refund")) {
			Assert.assertEquals(seMessageType.getText(), "failed !");
			Log.info(seMessageType.getText());
			Assert.assertEquals(seFailMessage.getText(), "money tranfer failed");
			Log.info(seFailMessage.getText());
		} else if (usrData.get("STATUS").equalsIgnoreCase("Queued")) {
			Assert.assertEquals(seMessageType.getText(), "success !");
			Log.info(seMessageType.getText());
			Assert.assertEquals(seMessage.getText(),
					"Transaction has been accepted for processing, Beneficiary Bank switch down, please check the status after some time");
			Log.info(seMessage.getText());
			dbUtils.updateBatchStatus("DisableRemittanceQueuing", "SUCCESS");
		}
		if (usrData.get("STATUS").equalsIgnoreCase("Timeout")) {
			Assert.assertEquals(seTimeoutMessage.getText(), "(91)");
			Log.info(seTimeoutMessage.getText());
		}
		if (!usrData.get("STATUS").equalsIgnoreCase("Auto-Refunded")) {
			try {
				Assert.assertEquals(seAmount.getText(), "amount : ₹ " + txnDetailsFromIni("GetTxfAmount", "") + ".00");
			} catch (Exception e) {
				Assert.assertEquals(enquiryAmount.getText(),
						"amount : ₹ " + txnDetailsFromIni("GetTxfAmount", "") + ".00");
			}
			Log.info(seAmount.getText());
		} else {
			try {
				Assert.assertEquals(seAmount.getText(), "amount : ₹ " + txnDetailsFromIni("GetTxfAmount", "") + ".00");
			} catch (Exception e) {
				Assert.assertEquals(enquiryAmount.getText(),
						"amount : ₹ " + txnDetailsFromIni("GetTxfAmount", "") + ".00");
			}
			Log.info(seAmount.getText());
		}
		try {
			Assert.assertEquals(seCharges.getText(), "charges : ₹ " + txnDetailsFromIni("GetCharges", ""));
		} catch (Exception e) {
			Assert.assertEquals(enquiryCharges.getText(), "charges : ₹ " + txnDetailsFromIni("GetCharges", ""));
		}
		Log.info(seCharges.getText());
	}

	// click on WebElement forcefully
	public void clickElement(WebElement element) {
		try {
			element.click();
		} catch (Exception e) {
			clickInvisibleElement(element);
		}
	}

	// Remove rupee symbol and comma from the string
	public String replaceSymbols(String element) {
		String editedElement = element.replaceAll("₹", "").replaceAll(",", "").trim();
		return editedElement;
	}

	// Get Partner name
	public String partner() {
		return "RBL";
	}

	// Get mobile number from Ini file
	public String mobileNumFromIni() {
		return getLoginMobileFromIni(partner().toUpperCase() + "RetailerMobNum");
	}

	// Get otp from Ini file
	public String otpFromIni() {
		return partner().toUpperCase() + "OTP";
	}

	public void otpScreen(Map<String, String> usrData)
			throws ClassNotFoundException, InterruptedException, ParseException {
		wait.until(ExpectedConditions.elementToBeClickable(otpTextField));
		if (usrData.get("OTP").equalsIgnoreCase("Valid")) {
			sendText(otpTextField, getAuthfromIni(otpFromIni()));
		} else if (usrData.get("OTP").equalsIgnoreCase("Invalid") || usrData.get("OTP").equalsIgnoreCase("Retry")) {
			sendText(otpTextField, "111111");
		} else if (usrData.get("OTP").equalsIgnoreCase("Resend")) {
			wait.until(ExpectedConditions.elementToBeClickable(resendOTPBtn));
			resendOTPBtn.click();
			sendText(otpTextField, getAuthfromIni(otpFromIni()));
			Log.info("OTP re-sent");
		}
		Log.info("OTP entered");
		if (usrData.get("OTP").equalsIgnoreCase("Valid") || usrData.get("OTP").equalsIgnoreCase("Resend")) {
			wait.until(ExpectedConditions.elementToBeClickable(submitOTPBtn));
			submitOTPBtn.click();
			Log.info("Submit OTP button clicked");
			wait.until(ExpectedConditions.visibilityOf(refundTxnScreen));
			double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
			double charges = Double.parseDouble(txnDetailsFromIni("GetCharges", ""));
			double totalAmount = amount + charges;
			String cashToBeRefunded = df.format(totalAmount);
			Assert.assertEquals(successMsg.getText(),
					"Money Transfer with cash payment: Rs." + txnDetailsFromIni("GetTxfAmount", "")
							+ ".00 and Charges:Rs." + txnDetailsFromIni("GetCharges", "") + ", Total: Rs."
							+ cashToBeRefunded + " has been refunded to Agent Wallet");
			Log.info(successMsg.getText());
			doneButton.click();
			Log.info("Done button clicked");
		} else if (usrData.get("OTP").equalsIgnoreCase("Invalid") || usrData.get("OTP").equalsIgnoreCase("Retry")) {
			wait.until(ExpectedConditions.elementToBeClickable(submitOTPBtn));
			submitOTPBtn.click();
			Log.info("Submit OTP button clicked");
			wait.until(ExpectedConditions.visibilityOf(refundTxnScreen));
			Assert.assertEquals(failureMsg.getText(), "Invalid Verification Code");
			Log.info(failureMsg.getText());
			if (usrData.get("OTP").equalsIgnoreCase("Invalid")) {
				exitButton.click();
				Log.info("Exit button clicked");
			} else {
				retryButton.click();
				seButton.click();
				Log.info("Refund button clicked");
				wait.until(ExpectedConditions.visibilityOf(confirmRefund));
				confirmRefundProceedBtn.click();
				wait.until(ExpectedConditions.elementToBeClickable(otpTextField));
				sendText(otpTextField, getAuthfromIni(otpFromIni()));
				Log.info("OTP entered");
				submitOTPBtn.click();
				Log.info("Submit OTP button clicked");
				wait.until(ExpectedConditions.visibilityOf(refundTxnScreen));
				double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
				double charges = Double.parseDouble(txnDetailsFromIni("GetCharges", ""));
				double totalAmount = amount + charges;
				String cashToBeRefunded = df.format(totalAmount);
				Assert.assertEquals(successMsg.getText(),
						"Money Transfer with cash payment: Rs." + txnDetailsFromIni("GetTxfAmount", "")
								+ ".00 and Charges:Rs." + txnDetailsFromIni("GetCharges", "") + ", Total: Rs."
								+ cashToBeRefunded + " has been refunded to Agent Wallet");
				Log.info(successMsg.getText());
				doneButton.click();
				Log.info("Done button clicked");
			}
		} else if (usrData.get("OTP").equalsIgnoreCase("Cancel")) {
			wait.until(ExpectedConditions.elementToBeClickable(cancelOTPBtn));
			cancelOTPBtn.click();
			Log.info("Cancel button clicked");
		}
	}

	// FCM assertion
	public void assertionOnRefundFCM(Map<String, String> usrData) throws ClassNotFoundException {
		double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
		double charges = Double.parseDouble(txnDetailsFromIni("GetCharges", ""));
		double totalAmount = amount + charges;

		String successFCMHeading = "Refund : SUCCESS";
		String failFCMHeading = "Refund : FAIL";

		String successFCMContent = "Customer Mobile: " + getCustomerDetailsFromIni("ExistingNum")
				+ " Money Transfer with cash payment: Rs." + df.format(amount) + " and Charges:Rs." + df.format(charges)
				+ ", Total: Rs." + df.format(totalAmount)
				+ " has been refunded to Agent Wallet.Transaction Reference No. " + dbUtils.paymentRefCode(partner());
		String failFCMContent = "Customer Mobile: " + getCustomerDetailsFromIni("ExistingNum")
				+ " Invalid Verification Code.";

		switch (usrData.get("ASSERTION")) {
		case "Success FCM":
			Assert.assertEquals(fcmHeading.getText(), successFCMHeading);
			Assert.assertEquals(fcmContent.getText().substring(0, 28) + fcmContent.getText().substring(29),
					successFCMContent);
			break;
		case "Failed FCM":
			Assert.assertEquals(fcmHeading.getText(), failFCMHeading);
			Assert.assertEquals(fcmContent.getText().substring(0, 28) + fcmContent.getText().substring(29),
					failFCMContent);
			break;
		}
		System.out.println(fcmHeading.getText());
		System.out.println(fcmContent.getText());
		mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
	}

}