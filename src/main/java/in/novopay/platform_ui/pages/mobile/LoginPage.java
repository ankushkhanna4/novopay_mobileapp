package in.novopay.platform_ui.pages.mobile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import in.novopay.platform_ui.utils.BasePage;
import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.Log;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class LoginPage extends BasePage {
	WebDriverWait wait = new WebDriverWait(mdriver, 30);
	WebDriverWait waitTitle = new WebDriverWait(mdriver, 2);
	DBUtils dbUtils = new DBUtils();

	public LoginPage(AndroidDriver<MobileElement> mdriver) {
		super(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver), this);
	}

	@AndroidFindBy(id = "in.novopay.merchant:id/signInMobileNumber")
	MobileElement mobNumTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/fab")
	MobileElement submitLoginBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/btnDone")
	MobileElement submitMPINBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/textViewForgotPin")
	MobileElement forgotPin;

	@AndroidFindBy(id = "in.novopay.merchant:id/EditTextPOIPostfix")
	MobileElement enterPAN;

	@AndroidFindBy(id = "in.novopay.merchant:id/fab")
	MobileElement submitPAN;

	@AndroidFindBy(id = "in.novopay.merchant:id/otp_edit_text")
	MobileElement otpTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/cancel")
	MobileElement cancelOTPBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/resend_otp")
	MobileElement resendOTPBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/submit_otp")
	MobileElement submitOTPBtn;

	@AndroidFindBy(xpath = "//*[contains(@text,'Enter 4 digit PIN')]")
	MobileElement enterMPIN;

	@AndroidFindBy(xpath = "//*[contains(@text,'create a new 4 digit mpin')]")
	MobileElement enterNewMPIN;

	@AndroidFindBy(xpath = "//*[contains(@text,'re-enter new 4 digit mpin')]")
	MobileElement reenterNewMPIN;

	@AndroidFindBy(id = "in.novopay.merchant:id/toolbar")
	MobileElement toasterMsg;

	@AndroidFindBy(className = "android.widget.ImageButton")
	MobileElement hamburgerMenu;

	@AndroidFindBy(xpath = "//*[contains(@text,'cash to account')]")
	MobileElement title;

	@AndroidFindBy(xpath = "//*[contains(@text,'Logout')]")
	MobileElement logout;

	@AndroidFindBy(xpath = "//*[contains(@text,'OK')]")
	MobileElement okLogout;

	@AndroidFindBy(id = "in.novopay.merchant:id/refresh_balance")
	MobileElement refreshButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/textViewBalance")
	MobileElement balanceLabel;

	public void login(Map<String, String> usrData) throws Exception {

		Log.info("Retailer WebApp 2.0 Login page displayed");

		try {
			try {
				waitTitle.until(ExpectedConditions.visibilityOf(balanceLabel));
				Log.info("Not on Login page");
				hamburgerMenu.click();
				wait.until(ExpectedConditions.elementToBeClickable(logout));
				Log.info("Logging out");
				logout.click();
				wait.until(ExpectedConditions.elementToBeClickable(okLogout));
				okLogout.click();
				wait.until(ExpectedConditions.visibilityOf(mobNumTextField));
				Log.info("Logged out successfully");
			} catch (Exception e) {
				Log.info("Logging in");
			}
			String mobNumFromSheet = usrData.get("MOBILENUMBER");
			wait.until(ExpectedConditions.visibilityOf(mobNumTextField));
			mobNumTextField.clear();
			Log.info("entering mobile number");
			if (mobNumFromSheet.equalsIgnoreCase("RetailerMobNum")) {
				sendText(mobNumTextField, readValue(getLoginMobileFromIni(mobNumFromSheet)));
			} else {
				sendText(mobNumTextField, readValue(mobNumFromSheet));
			}
			if (usrData.get("FORGOTPIN").equalsIgnoreCase("NO")) {
				Log.info("clicking on Login button");
				submitLoginBtn.click();
				Log.info("Login button clicked");
				tapMPINBtn(usrData.get("MPIN"));
				submitMPINBtn.click();
				Log.info("MPIN submitted");
				if (mobNumFromSheet.equalsIgnoreCase("RetailerMobNum")) {
					mobNumFromSheet = getLoginMobileFromIni(mobNumFromSheet);
				}
				if (mobNumValidation(mobNumFromSheet).equalsIgnoreCase("valid")
						&& checkMobNumExistence(mobNumFromSheet).equalsIgnoreCase("exists")
						&& (usrData.get("MPIN").equals("1111") || usrData.get("MPIN").equals("2222"))) {
					waitForSpinner();
					wait.until(ExpectedConditions.visibilityOf(balanceLabel));
					Log.info("Logged in successfully");
				} else if (!usrData.get("MPIN").equals("1111")) {
					waitForSpinner();
					wait.until(ExpectedConditions.visibilityOf(toasterMsg));
					// Assert.assertEquals(toasterMsg.getText(), "Authentication Failed Invalid
					// MPIN");
					Log.info(toasterMsg.getText());
				} else if (checkMobNumExistence(mobNumFromSheet).equalsIgnoreCase("does not exist")) {
					waitForSpinner();
					wait.until(ExpectedConditions.visibilityOf(toasterMsg));
					// Assert.assertEquals(toasterMsg.getText(), "No Retailer Found With Given
					// Mobile Number");
					Log.info(toasterMsg.getText());
				}
			} else if (usrData.get("FORGOTPIN").equalsIgnoreCase("YES")) {
				wait.until(ExpectedConditions.elementToBeClickable(forgotPin));
				Log.info("clicking Forgot Pin");
				forgotPin.click();
				waitForSpinner();
				wait.until(ExpectedConditions.elementToBeClickable(enterPAN));
				enterPAN.clear();
				Log.info("entering PAN number");
				sendText(enterPAN, usrData.get("PAN"));
				Log.info("clicking on PROCEED button");
				wait.until(ExpectedConditions.elementToBeClickable(submitPAN));
				submitPAN.click();
				if (usrData.get("PAN").equalsIgnoreCase(
						dbUtils.getPanNumber(getLoginMobileFromIni(mobNumFromSheet)).substring(4, 10))) {
					otpScreen(usrData);
					if (!usrData.get("OTPSCREENBUTTON").equalsIgnoreCase("Cancel")) {
						wait.until(ExpectedConditions.visibilityOf(enterNewMPIN));
						Log.info("entering New PIN");
						tapMPINBtn(usrData.get("NEWPIN"));
						submitMPINBtn.click();
						wait.until(ExpectedConditions.visibilityOf(reenterNewMPIN));
						Log.info("Re-entering New PIN");
						tapMPINBtn(usrData.get("REENTERPIN"));
						submitMPINBtn.click();
						waitForSpinner();
						if (usrData.get("NEWPIN").equals(usrData.get("REENTERPIN"))) {
							if (usrData.get("OTP").equalsIgnoreCase("Invalid")) {
								wait.until(ExpectedConditions.visibilityOf(enterPAN));
								Log.info("OTP does not match");
								mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
							} else {
								wait.until(ExpectedConditions.visibilityOf(mobNumTextField));
								Log.info("MPIN Changed successfully");
							}
						} else {
							wait.until(ExpectedConditions.visibilityOf(enterPAN));
							Log.info("Enter MPIN doesn't match");
							mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
						}
					}
				} else {
					wait.until(ExpectedConditions.visibilityOf(enterPAN));
					Log.info("incorrect poi value entered");
					waitForSpinner();
					mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
				}
			}
		} catch (Exception e) {
			Log.info("Test Case Failed");
			e.printStackTrace();
			Assert.fail();
		}
	}

	public void waitForSpinner() {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("android:id/body")));
		Log.info("Please wait...");
	}

	public String mobNumValidation(String mobNum) {
		if (mobNum.length() == 10 && (mobNum.startsWith("6") || mobNum.startsWith("7") || mobNum.startsWith("8")
				|| mobNum.startsWith("9"))) {
			return "valid";
		} else {
			return "invalid";
		}
	}

	public String mpinValidation(String mPin) {
		if (mPin.length() == 4 && mPin.chars().allMatch(Character::isDigit)) {
			return "valid";
		} else {
			return "invalid";
		}
	}

	public String otpValidation(String otp) {
		if (otp.length() >= 4 && otp.chars().allMatch(Character::isDigit)) {
			return "valid";
		} else {
			return "invalid";
		}
	}

	public String panValidation(String pan) {
		String firstFiveChar = "";
		String numericChar = "";
		String lastChar = "";
		if (pan.length() == 10) {
			firstFiveChar = pan.substring(0, 5);
			numericChar = pan.substring(5, 9);
			lastChar = pan.substring(9);
			if (firstFiveChar.chars().allMatch(Character::isLetter) && numericChar.chars().allMatch(Character::isDigit)
					&& lastChar.chars().allMatch(Character::isLetter)) {
				return "valid";
			} else {
				return "invalid";
			}
		} else {
			return "invalid";
		}
	}

	public String checkMobNumExistence(String mobNum) throws ClassNotFoundException {
		List<String> list = dbUtils.getListOfRetailerMobNum(); // get list of mobile numbers of retailers
		if (list.contains(mobNum)) {
			return "exists";
		} else {
			return "does not exist";
		}

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

	public void otpScreen(Map<String, String> usrData)
			throws ClassNotFoundException, InterruptedException, ParseException {
		wait.until(ExpectedConditions.elementToBeClickable(otpTextField));
		if (usrData.get("OTP").equalsIgnoreCase("Valid")) {
			sendText(otpTextField, getAuthfromIni("LoginOTP"));
		} else if (usrData.get("OTP").equalsIgnoreCase("Invalid")) {
			sendText(otpTextField, "111111");
		} else if (usrData.get("OTP").equalsIgnoreCase("Resend")) {
			wait.until(ExpectedConditions.elementToBeClickable(resendOTPBtn));
			resendOTPBtn.click();
			wait.until(ExpectedConditions.elementToBeClickable(otpTextField));
			sendText(otpTextField, getAuthfromIni("LoginOTP"));
			Log.info("OTP re-sent");
		}
		Log.info("OTP entered");
		if (usrData.get("OTPSCREENBUTTON").equalsIgnoreCase("Confirm")) {
			wait.until(ExpectedConditions.elementToBeClickable(submitOTPBtn));
			submitOTPBtn.click();
			Log.info("Submit OTP button clicked");
		} else if (usrData.get("OTPSCREENBUTTON").equalsIgnoreCase("Cancel")) {
			wait.until(ExpectedConditions.elementToBeClickable(cancelOTPBtn));
			cancelOTPBtn.click();
			Log.info("Cancel button clicked");
			mdriver.pressKey(new KeyEvent(AndroidKey.BACK));
			wait.until(ExpectedConditions.elementToBeClickable(mobNumTextField));
			Log.info("Login page displayed");
		}
	}
}
