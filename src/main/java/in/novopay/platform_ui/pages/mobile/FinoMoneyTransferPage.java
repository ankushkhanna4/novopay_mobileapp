package in.novopay.platform_ui.pages.mobile;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
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

public class FinoMoneyTransferPage extends BasePage {
	DBUtils dbUtils = new DBUtils();
	DecimalFormat df = new DecimalFormat("#.00");

	WebDriverWait wait = new WebDriverWait(mdriver, 60);
	WebDriverWait waitIFSC = new WebDriverWait(mdriver, 6);

	@AndroidFindBy(className = "android.widget.ImageButton")
	MobileElement hamburgerMenu;

	@AndroidFindBy(xpath = "//*contains[@text,'in.novopay.merchant:id/mobile_number')]/parent::android.widget.RelativeLayout")
	MobileElement pageFrame;

	@AndroidFindBy(id = "in.novopay.merchant:id/mobile_number")
	MobileElement remitterMobileNoTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/bene_spinner")
	MobileElement selectBeneficiaryList;

	@AndroidFindBy(id = "in.novopay.merchant:id/nick_name")
	MobileElement beneNickName;

	@AndroidFindBy(id = "in.novopay.merchant:id/details")
	MobileElement beneDetails;

	@AndroidFindBy(id = "in.novopay.merchant:id/is_verified")
	MobileElement beneVerification;

	@AndroidFindBy(id = "in.novopay.merchant:id/bene_name")
	MobileElement beneficiaryNameTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/bene_acct_number")
	MobileElement beneficiaryAccountNoTextField;

	@AndroidFindBy(xpath = "//*[contains(@text,'beneficiary account number*')]/following-sibling::android.widget.LinearLayout//android.widget.EditText")
	MobileElement ifscCodeTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/imageSearchButton")
	MobileElement ifscSearchButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/et_ifsc_code")
	MobileElement searchIfscCodeTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/et_bankName")
	MobileElement searchBankNameTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/state")
	MobileElement searchStateTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/et_district")
	MobileElement searchDistrictTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/et_branchName")
	MobileElement searchBranchTextField;

	@AndroidFindBy(id = "in.novopay.merchant:id/search")
	MobileElement searchIfscSearchButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'IFSC Code Search')]")
	MobileElement searchIfscScreen1;

	@AndroidFindBy(xpath = "//*[contains(@text,'IFSC Code Search Results')]")
	MobileElement searchIfscScreen2;

	@AndroidFindBy(xpath = "//android.support.v7.widget.RecyclerView/android.widget.LinearLayout")
	MobileElement ifscCodeResult;

	@AndroidFindBy(id = "in.novopay.merchant:id/bene_mobile_number")
	MobileElement beneficiaryMobileNoTextField;

	@AndroidFindBy(xpath = ".//android.widget.RadioButton[@text='IMPS']")
	MobileElement impsRadioBtn;

	@AndroidFindBy(xpath = ".//android.widget.RadioButton[@text='NEFT']")
	MobileElement neftRadioBtn;

	@AndroidFindBy(xpath = "//*[contains(@text,'Enter Amount')]")
	MobileElement transactionAmount;

	@AndroidFindBy(id = "in.novopay.merchant:id/button")
	MobileElement submitBtn;

	@AndroidFindBy(id = "android:id/button1")
	MobileElement confirmBeneReg;

	@AndroidFindBy(id = "in.novopay.merchant:id/nameEdittext")
	MobileElement remitterNameTextField;

	@AndroidFindBy(xpath = "//*[contains(text(),Male)]")
	MobileElement maleGenderRadioBtn;

	@AndroidFindBy(xpath = "//*[contains(text(),Female)]")
	MobileElement femaleGenderRadioBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/dob")
	MobileElement remitterDOBTextField;

	@AndroidFindBy(id = "android:id/button1")
	MobileElement calSubmitBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/submit")
	MobileElement remitterSubmitBtn;

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

	@AndroidFindBy(id = "in.novopay.merchant:id/deleteBeneficiary")
	MobileElement deleteBeneButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/dialog_title")
	MobileElement deleteBeneTitle;

	@AndroidFindBy(id = "in.novopay.merchant:id/delete")
	MobileElement deleteConfirmButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/dismiss")
	MobileElement dismissButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'success!')]")
	MobileElement successHeader;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_msg")
	MobileElement successMsg;

	@AndroidFindBy(xpath = "//*[contains(@text,'DONE')]")
	MobileElement doneButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/btnDone")
	MobileElement submitMPINBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/result_header")
	MobileElement successTransactionHeader;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_exit")
	MobileElement successExit;

	@AndroidFindBy(id = "in.novopay.merchant:id/bene_spinner")
	MobileElement beneficiaryDropDownBtn;

	@AndroidFindBy(className = "android.widget.ListView")
	MobileElement beneficiaryList;

	@AndroidFindBy(className = "android.widget.RelativeLayout")
	MobileElement beneficiarylistItem;

	@AndroidFindBy(id = "in.novopay.merchant:id/details")
	MobileElement beneficiaryAccNoBankName;

	@AndroidFindBy(id = "in.novopay.merchant:id/refresh_bene_list")
	MobileElement refreshBeneListButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/ref_number")
	MobileElement transactionReferenceNo;

	@AndroidFindBy(xpath = "//*[contains(@text,'Exit')]")
	MobileElement failExitButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/penny_check_validation")
	MobileElement beneValidationIcon;

	@AndroidFindBy(id = "android:id/button1")
	MobileElement proceedBeneValidationBtn;

	@AndroidFindBy(id = "android:id/button2")
	MobileElement cancelBeneValidationBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/checkBox")
	MobileElement checkBox;

	@AndroidFindBy(id = "in.novopay.merchant:id/text")
	MobileElement beneValMessage;

	@AndroidFindBy(id = "android:id/message")
	MobileElement beneValErrorMessage;

	@AndroidFindBy(id = "android:id/alertTitle")
	MobileElement beneValScreen;

	@AndroidFindBy(xpath = "//*[contains(@text,'OK')]")
	MobileElement okBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/resubmit_txn")
	MobileElement reSubmitTxnBtn;

	@AndroidFindBy(id = "in.novopay.merchant:id/limit")
	MobileElement limitRemaining;

	@AndroidFindBy(id = "in.novopay.merchant:id/cash_msg")
	MobileElement cashMsg;

	@AndroidFindBy(id = "in.novopay.merchant:id/imageViewConfirmCharges")
	MobileElement applicableChargesButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/alertTitle")
	MobileElement applicableChargesScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/toolbar")
	MobileElement toasterMsg;

	@AndroidFindBy(id = "in.novopay.merchant:id/txn_amount_value")
	MobileElement applicableTxnAmount;

	@AndroidFindBy(id = "in.novopay.merchant:id/charges_amount_value")
	MobileElement applicableCharges;

	@AndroidFindBy(id = "in.novopay.merchant:id/deposit_amount_value")
	MobileElement applicableTotalAmount;

	@AndroidFindBy(id = "in.novopay.merchant:id/label2")
	MobileElement applicableText;

	@AndroidFindBy(xpath = "//*[contains(@text,'processing...')]")
	MobileElement processingScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/process_in_background")
	MobileElement processInBackgroundButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'!')]")
	MobileElement remittanceTxnScreen;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_save")
	MobileElement remittanceTxnScreenSaveButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_print")
	MobileElement remittanceTxnScreenPrintButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_exit")
	MobileElement remittanceTxnScreenDoneButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_exit")
	MobileElement remittanceTxnScreenExitButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_exit")
	MobileElement remittanceTxnScreenCancelButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/fail_retry")
	MobileElement remittanceTxnScreenRetryButton;

	@AndroidFindBy(id = "in.novopay.merchant:id/success_msg")
	MobileElement remittanceTxnSuccessScreenMessage;

	@AndroidFindBy(id = "in.novopay.merchant:id/failure_msg")
	MobileElement remittanceTxnFailScreenMessage;

	@AndroidFindBy(id = "in.novopay.merchant:id/ref_number")
	MobileElement refNo;

	@AndroidFindBy(id = "in.novopay.merchant:id/cash_msg")
	MobileElement remittanceTxnCashMessage;

	@AndroidFindBy(xpath = "//*[contains(@content-desc,'Chrome')]")
	MobileElement chromeApp;

	@AndroidFindBy(id = "com.android.chrome:id/url_bar")
	MobileElement chromeURLBar;

	@AndroidFindBy(xpath = "//*[contains(@text,'Queuing')]")
	MobileElement disableText;

	@AndroidFindBy(id = "com.android.chrome:id/delete_button")
	MobileElement deleteURL;

	@AndroidFindBy(xpath = "//android.widget.FrameLayout[2]/android.widget.ListView/android.view.ViewGroup")
	MobileElement urlSuggestion;

	@AndroidFindBy(id = "com.android.chrome:id/search_box_text")
	MobileElement googleSearch;

	@AndroidFindBy(id = "com.android.chrome:id/mic_button")
	MobileElement micButton;

	// Load all objects
	public FinoMoneyTransferPage(AndroidDriver<MobileElement> mdriver) {
		super(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver), this);
	}

	// Perform action on page based on given commands
	public void finoMoneyTransfer(Map<String, String> usrData) {

		try {
			// Update retailer wallet balance to 0 for scenario where amount > wallet
			if (usrData.get("ASSERTION").equalsIgnoreCase("Amount > Wallet")) {
				dbUtils.updateWalletBalance(mobileNumFromIni(), "retailer", "0");
			}

			if (usrData.get("ASSERTION").contains("FCM")) {
				assertionOnFCM(usrData);
			} else {
				if (usrData.get("ASSERTION").equalsIgnoreCase("Refresh")) {
					wait.until(ExpectedConditions.elementToBeClickable(refreshBeneListButton));
					refreshBeneListButton.click();
					Log.info("Refresh button clicked");
				} else {
					// Click on customer Mobile Number field
					wait.until(ExpectedConditions.elementToBeClickable(remitterMobileNoTextField));
					if (usrData.get("ASSERTION").equalsIgnoreCase("Validate Icon")) {
						remitterMobileNoTextField.clear();
					}
					sendText(remitterMobileNoTextField, getCustomerDetailsFromIni(usrData.get("CUSTOMERNUMBER")));
					Log.info("Customer mobile number entered");
					remitterMobileNoTextField.click();
					mdriver.hideKeyboard();
				}
				limitCheck(usrData); // check limit remaining
			}

			// Provide beneficiary details based on user data
			if (usrData.get("BENE").equalsIgnoreCase("New")) { // when beneficiary is new
				if (usrData.get("BENEIFSCTYPE").equalsIgnoreCase("Manual")) {
					sendText(ifscCodeTextField, usrData.get("BENEIFSC"));
					Log.info("IFSC code '" + usrData.get("BENEIFSC") + "' entered");
				} else if (usrData.get("BENEIFSCTYPE").equalsIgnoreCase("Search Screen")) {
					ifscSearchScreen(usrData, "details");
				}
				getBankNameFromIni(dbUtils.ifscCodeDetails(usrData.get("BENEIFSC"), "bank"));

				wait.until(ExpectedConditions.elementToBeClickable(beneficiaryAccountNoTextField));
				sendText(beneficiaryAccountNoTextField, getAccountNumberFromIni(usrData.get("BENEACNUM")));
				Log.info("Bene account number '" + getAccountNumberFromIni("GetNum") + "' entered");

				wait.until(ExpectedConditions.elementToBeClickable(beneficiaryNameTextField));
				sendText(beneficiaryNameTextField, getBeneNameFromIni(usrData.get("BENENAME")));
				Log.info("Bene name '" + usrData.get("BENENAME") + "' entered");

				swipeVertical(0.886, 0.484, 1);
				wait.until(ExpectedConditions.elementToBeClickable(beneficiaryMobileNoTextField));
				sendText(beneficiaryMobileNoTextField, getBeneNumberFromIni(usrData.get("BENENUMBER")));
				Log.info("Bene mobile number '" + getBeneNumberFromIni("GetNum") + "' entered");

				// Validate beneficiary before registration
				if (usrData.get("VALIDATEBENE").equalsIgnoreCase("BEFOREREG")) {
					validateBene(usrData);
				}
			} else if (usrData.get("BENE").equalsIgnoreCase("Existing")) { // when beneficiary is existing

				wait.until(ExpectedConditions.elementToBeClickable(selectBeneficiaryList));
				selectBeneficiaryList.click();
				Log.info("Clicked on bene list drop down");
				String beneName = usrData.get("BENENAME");
				String beneIFSC = usrData.get("BENEIFSC");
				String beneACNum = getAccountNumberFromIni("GetNum");
				String beneXpath = ".//android.widget.RelativeLayout[@index='1']/android.widget.LinearLayout/android.widget.TextView[contains(@text,'"
						+ beneName + "')]/following-sibling::android.widget.TextView[contains(@text,'" + beneACNum
						+ ", " + dbUtils.getBank(beneIFSC) + "')]";
				WebElement beneficiary = mdriver.findElement(By.xpath(beneXpath));
				wait.until(ExpectedConditions.elementToBeClickable(beneficiary));
				if (usrData.get("ASSERTION").equalsIgnoreCase("Validate Icon")) {
					wait.until(ExpectedConditions.elementToBeClickable(beneVerification));
					Log.info("Validate icon visible");
				}
				beneficiary.click();
				Log.info(beneName + " beneficiary selected");
			}

			// Validate beneficiary after registration
			if (usrData.get("VALIDATEBENE").equalsIgnoreCase("AFTERREG")) {
				validateBene(usrData);
			}

			if (usrData.get("SUBMIT").equalsIgnoreCase("YES")) {
				if (usrData.get("ASSERTION").equalsIgnoreCase("Bene Limit")) {
					wait.until(ExpectedConditions.visibilityOf(toasterMsg));
					Assert.assertEquals(toasterMsg.getText(), "You have reached the maximum beneficiary count for 10. "
							+ "Please delete any existing beneficiary to add a new one");
					Log.info(toasterMsg.getText());
				} else {
					if (usrData.get("ADDBENE").equalsIgnoreCase("Indirectly")) {
						wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
						submitBtn.click();
						Log.info("Submit button clicked");
						try {
							waitIFSC.until(ExpectedConditions.elementToBeClickable(confirmBeneReg));
						} catch (Exception e) {
							Log.info("entering IFSC code again");
							ifscSearchScreen(usrData, "code");
							wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
							submitBtn.click();
							Log.info("Submit button clicked");
							wait.until(ExpectedConditions.elementToBeClickable(confirmBeneReg));
						}
						confirmBeneReg.click();
						Log.info("Proceed button clicked");

						// Provide customer details based on user data
						if (usrData.get("CUSTOMERNUMBER").equalsIgnoreCase("NewNum")
								&& usrData.get("VALIDATEBENE").equalsIgnoreCase("No") || usrData.get("ASSERTION").equalsIgnoreCase("Other Partner")) {
							enterRemitterDetails(usrData);
						}

						otpScreen(usrData);

					} else {
						if (usrData.get("ADDBENE").equalsIgnoreCase("Directly")) {
							swipeVertical(0.886, 0.484, 1);
							wait.until(ExpectedConditions.elementToBeClickable(transactionAmount));
							Log.info("Amount entered");
							sendText(transactionAmount, usrData.get("AMOUNT"));
							if (dbUtils.modeOfTransfer(usrData.get("BENEIFSC")).equals("1")) {
//								Assert.assertEquals(impsRadioBtn.getAttribute("checked").equals("true"), true);
								Log.info("IMPS mode auto-selected");
							} else if (dbUtils.modeOfTransfer(usrData.get("BENEIFSC")).equals("0")) {
//								Assert.assertEquals(neftRadioBtn.getAttribute("checked").equals("true"), true);
								Log.info("NEFT mode auto-selected");
							}

							// Verify applicable charges
							if (usrData.get("CHARGES").equalsIgnoreCase("YES")) {
								wait.until(ExpectedConditions.elementToBeClickable(applicableChargesButton));
								applicableChargesButton.click();
								Log.info("Charges icon clicked");
								try {
									waitIFSC.until(ExpectedConditions.visibilityOf(applicableChargesScreen));
								} catch (Exception e) {
									Log.info("entering IFSC code again");
									ifscSearchScreen(usrData, "code");
									wait.until(ExpectedConditions.elementToBeClickable(applicableChargesButton));
									applicableChargesButton.click();
									wait.until(ExpectedConditions.visibilityOf(applicableChargesScreen));
								}
								assertionOnApplicableCharges(usrData);
								okBtn.click();
							}

							wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
							submitBtn.click();
							Log.info("Submit button clicked");
							try {
								// Provide customer details based on user data
								if ((usrData.get("CUSTOMERNUMBER").equalsIgnoreCase("NewNum")
										|| usrData.get("CUSTOMERNUMBER").equalsIgnoreCase("GetBeneNum") || usrData.get("ASSERTION").equalsIgnoreCase("Other Partner"))
										&& !usrData.get("VALIDATEBENE").equalsIgnoreCase("BEFOREREG")) {
									enterRemitterDetails(usrData);
								}
								otpScreen(usrData);
							} catch (Exception e) {
								Log.info("entering IFSC code again");
								ifscSearchScreen(usrData, "code");
								submitBtn.click();
								Log.info("Submit button clicked");

								if ((usrData.get("CUSTOMERNUMBER").equalsIgnoreCase("NewNum")
										|| usrData.get("CUSTOMERNUMBER").equalsIgnoreCase("GetBeneNum"))
										&& !usrData.get("VALIDATEBENE").equalsIgnoreCase("BEFOREREG")) {
									enterRemitterDetails(usrData);
								}
								otpScreen(usrData);
							}

						} else if (usrData.get("ADDBENE").equalsIgnoreCase("No")) {
							swipeVerticalUntilElementIsVisible(0.886, 0.484, neftRadioBtn);
							wait.until(ExpectedConditions.elementToBeClickable(transactionAmount));
							Log.info("Amount entered");
							sendText(transactionAmount, usrData.get("AMOUNT"));
							if (dbUtils.modeOfTransfer(usrData.get("BENEIFSC")).equals("1")) {
//								Assert.assertEquals(impsRadioBtn.getAttribute("checked").equals("true"), true);
								Log.info("IMPS mode auto-selected");
							} else if (dbUtils.modeOfTransfer(usrData.get("BENEIFSC")).equals("0")) {
//								Assert.assertEquals(neftRadioBtn.getAttribute("checked").equals("true"), true);
								Log.info("NEFT mode auto-selected");
							}

							// Verify applicable charges
							if (usrData.get("CHARGES").equalsIgnoreCase("YES")) {
								wait.until(ExpectedConditions.elementToBeClickable(applicableChargesButton));
								applicableChargesButton.click();
								Log.info("Charges icon clicked");
								try {
									waitIFSC.until(ExpectedConditions.visibilityOf(applicableChargesScreen));
								} catch (Exception e) {
									Log.info("entering IFSC code again");
									ifscSearchScreen(usrData, "code");
									wait.until(ExpectedConditions.elementToBeClickable(applicableChargesButton));
									applicableChargesButton.click();
									wait.until(ExpectedConditions.visibilityOf(applicableChargesScreen));
								}
								assertionOnApplicableCharges(usrData);
								okBtn.click();
							}

							wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
							submitBtn.click();
							Log.info("Submit button clicked");
							try {
								moneyTransfer(usrData);
							} catch (Exception e) {
								Log.info("entering IFSC code again");
								ifscSearchScreen(usrData, "code");
								wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
								submitBtn.click();
								Log.info("Submit button clicked");
								moneyTransfer(usrData);
							}
						}
					}
				}
			} else if (usrData.get("SUBMIT").equalsIgnoreCase("No")) {
				mdriver.pressKeyCode(AndroidKeyCode.BACK);
				wait.until(ExpectedConditions.elementToBeClickable(okBtn));
				okBtn.click();
				Log.info("Home page displayed");
			}

			// Delete Beneficiary
			if (usrData.get("DELETEBENE").equalsIgnoreCase("YES")) {
				deleteBene(usrData, getAuthfromIni(otpFromIni()));
			}

		} catch (Exception e) {
			Log.info("Test Case Failed");
			e.printStackTrace();
			Assert.fail();
		}
	}

	// Method to validate beneficiary based on user data
	public void validateBene(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(beneValidationIcon));
		Log.info("Validating beneficiary");
		beneValidationIcon.click();
		try {
			waitIFSC.until(ExpectedConditions.visibilityOf(proceedBeneValidationBtn));
		} catch (Exception e) {
			Log.info("entering IFSC code again");
			ifscSearchScreen(usrData, "code");
			wait.until(ExpectedConditions.elementToBeClickable(beneValidationIcon));
			beneValidationIcon.click();
			wait.until(ExpectedConditions.visibilityOf(proceedBeneValidationBtn));
		}
		proceedBeneValidationBtn.click();
		if (usrData.get("CUSTOMERNUMBER").equalsIgnoreCase("NewNum")) {
			enterRemitterDetails(usrData);
		}
		wait.until(ExpectedConditions.visibilityOf(beneValScreen));
		if (usrData.get("VALIDATEBENECASE").equalsIgnoreCase("5")) {
			Log.info(beneValErrorMessage.getText());
		} else {
			Log.info(beneValMessage.getText());
		}
		assertionOnBeneValidationScreen(usrData, getBalanceFromIni("Get", "retailer", ""));
		if (usrData.get("VALIDATEBENECASE").equalsIgnoreCase("2")) {
			proceedBeneValidationBtn.click();
			Log.info("Proceed button clicked");
		} else if (usrData.get("VALIDATEBENECASE").equalsIgnoreCase("U")) {
			checkBox.click();
			Log.info("Checkbox selected");
			proceedBeneValidationBtn.click();
			Log.info("Proceed button clicked");
			Assert.assertNotEquals(beneficiaryNameTextField.getText(), usrData.get("BENENAME"));
			Log.info("Name updated");
		} else {
			okBtn.click();
			Log.info("OK button clicked");
		}
		if (!usrData.get("MPIN").equalsIgnoreCase("Valid")) {
			mdriver.pressKeyCode(AndroidKeyCode.BACK);
			wait.until(ExpectedConditions.elementToBeClickable(okBtn));
			okBtn.click();
			Log.info("Home page displayed");
		}
	}

	public void ifscSearchScreen(Map<String, String> usrData, String mode) throws ClassNotFoundException {
		wait.until(ExpectedConditions.elementToBeClickable(ifscSearchButton));
		ifscSearchButton.click();
		Log.info("IFSC search icon clicked");
		wait.until(ExpectedConditions.visibilityOf(searchIfscScreen1));
		if (mode.equalsIgnoreCase("code")) {
			wait.until(ExpectedConditions.elementToBeClickable(searchBankNameTextField));
			searchIfscCodeTextField.click();
			sendText(searchIfscCodeTextField, usrData.get("BENEIFSC"));
			Log.info("IFSC code entered");
		} else if (mode.equalsIgnoreCase("details")) {
			wait.until(ExpectedConditions.elementToBeClickable(searchBankNameTextField));
//		searchBankNameTextField.click();
			sendText(searchBankNameTextField, dbUtils.ifscCodeDetails(usrData.get("BENEIFSC"), "bank"));
			Log.info("IFSC bank selected");
			mdriver.pressKeyCode(AndroidKeyCode.BACK);
//		searchStateTextField.click();
			sendText(searchStateTextField, dbUtils.ifscCodeDetails(usrData.get("BENEIFSC"), "state"));
			Log.info("IFSC state selected");
			mdriver.pressKeyCode(AndroidKeyCode.BACK);
//		searchDistrictTextField.click();
			sendText(searchDistrictTextField, dbUtils.ifscCodeDetails(usrData.get("BENEIFSC"), "district"));
			Log.info("IFSC district entered");
//		searchBranchTextField.click();
			sendText(searchBranchTextField, dbUtils.ifscCodeDetails(usrData.get("BENEIFSC"), "branch"));
			Log.info("IFSC branch entered");
		}
		searchIfscSearchButton.click();
		Log.info("Search button clicked");
		wait.until(ExpectedConditions.visibilityOf(searchIfscScreen1));
		wait.until(ExpectedConditions.elementToBeClickable(ifscCodeResult));
		ifscCodeResult.click();
		Log.info("IFSC code '" + usrData.get("BENEIFSC") + "' entered");
	}

	// Method to delete beneficiary based on user data
	public void deleteBene(Map<String, String> usrData, String OTP)
			throws ClassNotFoundException, InterruptedException, ParseException {
		wait.until(ExpectedConditions.elementToBeClickable(deleteBeneButton));
		deleteBeneButton.click();
		Log.info("Delete Bene button clicked");
		wait.until(ExpectedConditions.visibilityOf(deleteBeneTitle));
		Log.info("Delete Bene screen displayed");
		if (usrData.get("DELETEBENETYPE").equalsIgnoreCase("HARD")) {
			wait.until(ExpectedConditions.elementToBeClickable(checkBox));
			checkBox.click();
			Log.info("Hard Deleting Bene");
		} else {
			Log.info("Soft Deleting Bene");
		}
		deleteConfirmButton.click();
		Log.info("Confirm button clicked");

		otpScreen(usrData);
		if (!usrData.get("OTPSCREENBUTTON").equalsIgnoreCase("Cancel")) {
			mdriver.pressKeyCode(AndroidKeyCode.BACK);
			wait.until(ExpectedConditions.elementToBeClickable(okBtn));
			okBtn.click();
			Log.info("Home page displayed");
		}
	}

	// Provide MPIN during money transfer and do assertion on txn screen
	public void moneyTransfer(Map<String, String> usrData)
			throws ClassNotFoundException, InterruptedException, ParseException {
		wait.until(ExpectedConditions.visibilityOf(enterMPIN));
		Log.info("MPIN screen displayed");
		if (usrData.get("MPIN").equalsIgnoreCase("Valid")) {
			tapMPINBtn(getAuthfromIni("MPIN"));
		} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
			tapMPINBtn("9999");
		}

		if (!usrData.get("ASSERTION").equalsIgnoreCase("Processing")) {

			unblockQueuingConfig(usrData); // Unblock banks based on user data
			unblockBanks(usrData); // Update queuing_config table based on user data
			blackoutCheck(usrData); // Check if Blackout is to be enabled or not

			if (usrData.get("ASSERTION").equalsIgnoreCase("Insufficient Balance")) {
				dbUtils.updateWalletBalance(mobileNumFromIni(), "retailer", "0");
			}
		}

		if (usrData.get("MPINSCREENBUTTON").equalsIgnoreCase("Cancel")) {
			mdriver.pressKeyCode(AndroidKeyCode.BACK);
			wait.until(ExpectedConditions.elementToBeClickable(remitterMobileNoTextField));
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
				wait.until(ExpectedConditions.visibilityOf(remittanceTxnScreen));
				Log.info("Txn screen displayed");

				// Verify the details on transaction screen
				if (remittanceTxnScreen.getAttribute("text").equalsIgnoreCase("success!")) {
					if (remittanceTxnSuccessScreenMessage.getAttribute("text").contains("success")
							&& !usrData.get("ASSERTION").equalsIgnoreCase("Partial")) {
						assertionOnSuccessScreen(usrData);
						if (usrData.get("ASSERTION").equalsIgnoreCase("EnableQueuingCheck")) {
							Assert.assertNull(dbUtils.verifyIfQueuingIsEnabled(partner()));
							Log.info("Queuing auto-enabled");
						}
					} else if (remittanceTxnSuccessScreenMessage.getAttribute("text").contains("processing")
							|| usrData.get("ASSERTION").equalsIgnoreCase("Partial")) {
						assertionOnWarnScreen(usrData);
						if (usrData.get("ASSERTION").equalsIgnoreCase("DisableQueuingCheck")) {
							Thread.sleep(90000);
							Assert.assertEquals("Auto disabled, based on " + dbUtils.getPaymentRefCode(partner()),
									dbUtils.verifyIfQueuingIsDisabled(usrData, partner()));
							Log.info("Queuing auto-disabled");
						}
					}
					assertionOnSMS(usrData);
					if (usrData.get("TXNSCREENBUTTON").equals("Save")) {
						wait.until(ExpectedConditions.elementToBeClickable(remittanceTxnScreenSaveButton));
						remittanceTxnScreenSaveButton.click();
						Log.info("Save button clicked");
					} else if (usrData.get("TXNSCREENBUTTON").equals("Print")) {
						wait.until(ExpectedConditions.elementToBeClickable(remittanceTxnScreenPrintButton));
						remittanceTxnScreenPrintButton.click();
						Log.info("Print button clicked");
					}
					wait.until(ExpectedConditions.elementToBeClickable(remittanceTxnScreenDoneButton));
					remittanceTxnScreenDoneButton.click();
					Log.info("Done button clicked");
					if (!usrData.get("ASSERTION").equalsIgnoreCase("Processing")) {
						verifyUpdatedBalanceAfterSuccessTxn(usrData, getBalanceFromIni("Get", "retailer", ""));
					}
				} else if (remittanceTxnScreen.getAttribute("text").equalsIgnoreCase("failed!")) {
					if (usrData.get("OTP").equalsIgnoreCase("Valid") && usrData.get("MPIN").equalsIgnoreCase("Valid")) {
						assertionOnFailedScreen(usrData);
						if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
							wait.until(ExpectedConditions.elementToBeClickable(remittanceTxnScreenExitButton));
						} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
							remittanceTxnScreenRetryButton.click();
							Log.info("Retry button clicked");
							wait.until(ExpectedConditions.visibilityOf(submitBtn));
							submitBtn.click();
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
							wait.until(ExpectedConditions.visibilityOf(remittanceTxnScreen));
							Log.info("Txn screen displayed");
							assertionOnFailedScreen(usrData);
						}
						remittanceTxnScreenExitButton.click();
						Log.info("Exit button clicked");
						if (usrData.get("ASSERTION").equalsIgnoreCase("Insufficient Balance")) {
							dbUtils.updateWalletBalance(mobileNumFromIni(), "retailer", "1000000");
						} else {
							verifyUpdatedBalanceAfterFailTxn(usrData, getBalanceFromIni("Get", "retailer", ""));
						}
					} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
						wait.until(ExpectedConditions.elementToBeClickable(remittanceTxnFailScreenMessage));
						Log.info(remittanceTxnFailScreenMessage.getText());
						if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
							remittanceTxnScreenExitButton.click();
							Log.info("Exit button clicked");
						} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
							remittanceTxnScreenRetryButton.click();
							Log.info("Retry button clicked");
							wait.until(ExpectedConditions.visibilityOf(submitBtn));
							submitBtn.click();
							wait.until(ExpectedConditions.visibilityOf(enterMPIN));
							Log.info("MPIN screen displayed");
							tapMPINBtn(getAuthfromIni("MPIN"));
							wait.until(ExpectedConditions.elementToBeClickable(submitMPINBtn));
							submitMPINBtn.click();
							Log.info("Submit button clicked");
							wait.until(ExpectedConditions.visibilityOf(processingScreen));
							Log.info("Processing screen displayed");
							wait.until(ExpectedConditions.visibilityOf(remittanceTxnScreen));
							Log.info("Txn screen displayed");
							assertionOnSuccessScreen(usrData);
							remittanceTxnScreenDoneButton.click();
							Log.info("Done button clicked");
							verifyUpdatedBalanceAfterSuccessTxn(usrData, getBalanceFromIni("Get", "retailer", ""));
						}
					}
				} else if (remittanceTxnScreen.getText().equalsIgnoreCase("info!")) {
					wait.until(ExpectedConditions.visibilityOf(remittanceTxnSuccessScreenMessage));
					Log.info(remittanceTxnSuccessScreenMessage.getText());
					wait.until(ExpectedConditions.elementToBeClickable(remittanceTxnScreenCancelButton));
					remittanceTxnScreenCancelButton.click();
					Log.info("Cancel button clicked");
					dbUtils.updateBlackoutDuration("1");
				}
			}
		}
	}

	// Verify details on success screen
	public void assertionOnSuccessScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (dbUtils.verifyIfQueuingIsEnabled(partner()) == null
				&& dbUtils.verifyIfTxnIsQueued(partner()).equalsIgnoreCase("NotQueued")) {
			Log.info("Queuing for " + dbUtils.getQueuedBankName() + " is enabled");
		}
		if (usrData.get("VALIDATEBENECASE").equals("U")) {
			Assert.assertEquals(remittanceTxnSuccessScreenMessage.getText(), "funds transferred successfully to "
					+ dbUtils.beneName(usrData.get("BENEACNUM"), usrData.get("BENEIFSC")));
		} else {
			Assert.assertEquals(remittanceTxnSuccessScreenMessage.getText(),
					"funds transferred successfully to " + usrData.get("BENENAME"));
		}
		Log.info(remittanceTxnSuccessScreenMessage.getText());
		txnDetailsFromIni("StoreTxfAmount", usrData.get("AMOUNT"));
		String chrges = dbUtils.getRemittanceCharges(usrData.get("AMOUNT"),
				dbUtils.getChargeCategory(mobileNumFromIni()), partner());
		txnDetailsFromIni("StoreCharges", chrges);
		txnDetailsFromIni("StoreTxnRefNo", dbUtils.paymentRefCode(partner().toUpperCase()));
		tableAssertion();
		double amount = Double.parseDouble(usrData.get("AMOUNT"));
		double charges = Double.parseDouble(chrges);
		double totalAmount = amount + charges;
		String cashToBeCollected = df.format(totalAmount);
		Assert.assertEquals(replaceSymbols(remittanceTxnCashMessage.getText()),
				"cash to be collected:  " + cashToBeCollected);
		Log.info(remittanceTxnCashMessage.getText());
	}

	// Assertion after success or orange screen is displayed
	public void verifyUpdatedBalanceAfterSuccessTxn(Map<String, String> usrData, String oldWalletBalance)
			throws ClassNotFoundException {
		double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
		double charges = Double.parseDouble(txnDetailsFromIni("GetCharges", ""));
		double totalAmount = amount + charges;
		double commission = commissionAndTDS("comm");
		double tds = commissionAndTDS("tds");
		double newWalletBal = 0.00;
		double initialWalletBalance = Double.parseDouble(oldWalletBalance);
		if (usrData.get("VALIDATEBENECASE").equalsIgnoreCase("1")
				|| usrData.get("VALIDATEBENECASE").equalsIgnoreCase("2")
				|| usrData.get("VALIDATEBENECASE").equalsIgnoreCase("3")
				|| usrData.get("VALIDATEBENECASE").equalsIgnoreCase("4")
				|| usrData.get("VALIDATEBENECASE").equalsIgnoreCase("U")) {
			double beneAmt = Double.parseDouble(dbUtils.getBeneAmount(partner()));
			newWalletBal = initialWalletBalance - totalAmount + commission - tds - beneAmt;
		} else {
			newWalletBal = initialWalletBalance - totalAmount + commission - tds;
		}
		txnDetailsFromIni("StoreComm", String.valueOf(commission));
		String newWalletBalance = df.format(newWalletBal);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "retailer"), newWalletBalance + "0000");
		Log.info("Updated Retailer Wallet Balance: " + newWalletBalance);
	}

	// Verify details on failed screen
	public void assertionOnFailedScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (!usrData.get("ASSERTION").equalsIgnoreCase("Amount > Max")) {
			txnDetailsFromIni("StoreFailAmount", usrData.get("AMOUNT"));
			txnDetailsFromIni("StoreTxnRefNo", dbUtils.paymentRefCode(partner().toUpperCase()));
			tableAssertion();
		}
		if (usrData.get("ASSERTION").equalsIgnoreCase("Insufficient balance")) {
			Assert.assertEquals(remittanceTxnFailScreenMessage.getText(), "Insufficient balance");
			dbUtils.updateWalletBalance(mobileNumFromIni(), "retailer", "1000000");
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Bene Limit Exceed")) {
			Assert.assertEquals(remittanceTxnFailScreenMessage.getText(),
					"BENEFICIARY: Transaction amount exceeds monthly allowed limit of ₹"
							+ txnDetailsFromIni("GetTxfAmount", "") + ".00, currently utilized limit is ₹"
							+ txnDetailsFromIni("GetTxfAmount", "")
							+ ".00. Retry after correcting the transaction amount.");
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Amount > Max")) {
			Assert.assertEquals(remittanceTxnFailScreenMessage.getText(),
					"Limit validation failed. Transacion amount exceeds allowed limit of Rs. 25000.00");
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Amount < Min")) {
			Assert.assertEquals(remittanceTxnFailScreenMessage.getText(),
					"BENEFICIARY: Minimum of ₹ 100.00 should be used as transaction amount");
		} else {
			try {
				Assert.assertEquals(remittanceTxnFailScreenMessage.getText(),
						"Transaction failed due to invalid beneficiary IFSC code. Transaction Reference Number : "
								+ dbUtils.paymentRefCode(partner().toUpperCase()));
			} catch (Exception e) {
				Assert.assertEquals(remittanceTxnFailScreenMessage.getText(),
						"Transaction failed. Please try again later. Transaction Reference Number : "
								+ dbUtils.paymentRefCode(partner().toUpperCase()));
			}
//					"Money Transfer of ₹" + txnDetailsFromIni("GetFailAmount", "") + ".00 has failed for IFSC:"
//							+ usrData.get("BENEIFSC") + ", A/C:" + getAccountNumberFromIni("GetNum") + ", charges ₹"
//							+ txnDetailsFromIni("GetCharges", "") + ", reference no. "
//							+ dbUtils.paymentRefCode(partner().toUpperCase()));
		}
		Log.info(remittanceTxnFailScreenMessage.getText());
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

	// Verify details on warn screen
	public void assertionOnWarnScreen(Map<String, String> usrData) throws ClassNotFoundException {
		String txfAmount = "";
		if (dbUtils.verifyIfQueuingIsEnabled(partner()) == null
				&& dbUtils.verifyIfTxnIsQueued(partner()).equalsIgnoreCase("Queued")) {
			Assert.assertEquals(remittanceTxnSuccessScreenMessage.getText(),
					"Transactions have been accepted for processing, Beneficiary Bank switch down, please check the status after some time");
			txnDetailsFromIni("StoreTxnRefNo", dbUtils.paymentRefCode(partner()));
			txfAmount = usrData.get("AMOUNT");
		} else {
			Assert.assertEquals(remittanceTxnSuccessScreenMessage.getText(),
					"funds transferred successfully to " + usrData.get("BENENAME"));
			txnDetailsFromIni("StoreTxnRefNo", dbUtils.paymentRefCode(partner().toUpperCase()));
			txfAmount = "5000.00";
		}
		Log.info(remittanceTxnSuccessScreenMessage.getText());

		txnDetailsFromIni("StoreTxfAmount", txfAmount);
		String chrges = dbUtils.getRemittanceCharges(txfAmount, dbUtils.getChargeCategory(mobileNumFromIni()),
				partner());
		txnDetailsFromIni("StoreCharges", chrges);
		tableAssertion();
		double charges = Double.parseDouble(chrges);
		double totalAmount = Double.parseDouble(txfAmount) + charges;
		String cashToBeCollected = df.format(totalAmount);
		Assert.assertEquals(replaceSymbols(remittanceTxnCashMessage.getText()),
				"cash to be collected:  " + cashToBeCollected);
		Log.info(remittanceTxnCashMessage.getText());
	}

	// Verify details on applicable charges screen
	public void assertionOnApplicableCharges(Map<String, String> usrData) throws ClassNotFoundException {
		Log.info("Verifying charges");
		Assert.assertEquals(replaceSymbols(applicableTxnAmount.getText()), usrData.get("AMOUNT") + ".00");
		Log.info("Transaction Amount: " + replaceSymbols(applicableTxnAmount.getText()));
		String chrges = dbUtils.getRemittanceCharges(usrData.get("AMOUNT"),
				dbUtils.getChargeCategory(mobileNumFromIni()), partner().toUpperCase());
		Assert.assertEquals(replaceSymbols(applicableCharges.getText()), chrges);
		Log.info("Charges: " + replaceSymbols(applicableCharges.getText()));

		double amount = Double.parseDouble(usrData.get("AMOUNT"));
		double charges = Double.parseDouble(chrges);
		double totalAmount = amount + charges;
		String cashToBeCollected = df.format(totalAmount);
		Assert.assertEquals(replaceSymbols(applicableTotalAmount.getText()), cashToBeCollected);
		Log.info("Cash to be Collected: " + replaceSymbols(applicableTotalAmount.getText()));

		/*
		 * Assert.assertEquals(applicableText.getText(),
		 * "*customer charges collected subject to max of 1% and min charges as applicable per transaction"
		 * ); Log.info(applicableText.getText());
		 */
	}

	// Verify details on Bene Validation screen
	public void assertionOnBeneValidationScreen(Map<String, String> usrData, String oldWalletBalance)
			throws ClassNotFoundException, ParseException, InterruptedException {
		String case1 = "Account Number " + getAccountNumberFromIni("GetNum") + " in "
				+ dbUtils.getBank(usrData.get("BENEIFSC")) + " belongs to " + usrData.get("BENENAME")
				+ ". Please collect Rs. 3.00 from customer.";
		String case2 = "Account Number " + getAccountNumberFromIni("GetNum") + " in "
				+ dbUtils.getBank(usrData.get("BENEIFSC")) + " belongs to "
				+ dbUtils.beneName(getAccountNumberFromIni("GetNum"), usrData.get("BENEIFSC"))
				+ ". Please collect Rs. 3.00 from customer.";
		String case3 = "Account Number " + getAccountNumberFromIni("GetNum") + " exists in "
				+ dbUtils.getBank(usrData.get("BENEIFSC"))
				+ ". However, bank did not return the account holder name. Please collect Rs. 3.00 from customer.";
		String case4 = "Beneficiary name could not be fetched due to some error.";
		if (usrData.get("VALIDATEBENECASE").equals("1")) {
			Assert.assertEquals(beneValMessage.getText(), case1);
			Log.info("Case 1 validated");
		} else if (usrData.get("VALIDATEBENECASE").equals("2") || usrData.get("VALIDATEBENECASE").equals("U")) {
			Assert.assertEquals(beneValMessage.getText(), case2);
			Log.info("Case " + usrData.get("VALIDATEBENECASE") + " validated");
		} else if (usrData.get("VALIDATEBENECASE").equals("3")) {
			Assert.assertEquals(beneValMessage.getText(), case3);
			Log.info("Case 3 validated");
		} else if (usrData.get("VALIDATEBENECASE").equals("4")) {
			Assert.assertEquals(beneValMessage.getText(), case4);
			Log.info("Case 4 validated");
		}

		double newWalletBal = 0.00;
		String newWalletBalance = "";
		double initialWalletBalance = Double.parseDouble(oldWalletBalance);
		if (usrData.get("VALIDATEBENECASE").equalsIgnoreCase("5")) {
			newWalletBal = initialWalletBalance;
		} else {
			double beneAmt = Double.parseDouble(dbUtils.getBeneAmount(partner().toUpperCase()));
			newWalletBal = initialWalletBalance - beneAmt;
		}
		newWalletBalance = df.format(newWalletBal);
		Assert.assertEquals(dbUtils.getWalletBalance(mobileNumFromIni(), "retailer"), newWalletBalance.concat("0000"));
		Log.info("Updated Retailer Wallet Balance: " + newWalletBalance);
	}

	// Verify table details on transaction screen
	public void tableAssertion() throws ClassNotFoundException {
		int i = 0, j = 0;
		int rowCount = (mdriver
				.findElements(By.xpath("//android.widget.TextView[@resource-id='in.novopay.merchant:id/amount']"))
				.size()) / 2;
		System.out.println("	Amount	Ref No		Status");
		for (i = 1; i <= rowCount; i++) {
			System.out.print("Row " + i + ":	");
			for (j = 1; j <= 3; j++) {
				String xpathHead = "//android.widget.ListView/android.widget.LinearLayout";
				String tableElementXpath = "", tableStatusXpath = "";
				MobileElement tableElement = null;
				if (j == 1 || j == 2) {
					if (rowCount == 1) {
						tableElementXpath = xpathHead + "/android.widget.TextView[" + j + "]";
					} else {
						tableElementXpath = xpathHead + "[" + i + "]/android.widget.TextView[" + j + "]";
					}
					tableElement = mdriver.findElement(By.xpath(tableElementXpath));
				}
				if (j == 1) {
					System.out.print(replaceSymbols(tableElement.getText()) + "	");
				}
				if (j == 2) {
					String refNo = dbUtils.paymentRefCode(partner().toUpperCase());
					if (refNo.length() >= 10) {
						System.out.print(refNo + "	");
					} else {
						System.out.print(refNo + "		");
					}
				}
				if (j == 3) {
					if (rowCount == 1) {
						if (remittanceTxnScreen.getAttribute("text").equalsIgnoreCase("success!")
								&& !remittanceTxnSuccessScreenMessage.getAttribute("text").contains("processing")) {
							tableStatusXpath = xpathHead + "/android.widget.LinearLayout/android.widget.ImageView";
						} else if (remittanceTxnScreen.getAttribute("text").equalsIgnoreCase("failed!")
								|| remittanceTxnSuccessScreenMessage.getAttribute("text").contains("processing")) {
							tableStatusXpath = xpathHead + "/android.widget.LinearLayout/android.widget.ImageView";
						}
					} else {
						if (remittanceTxnScreen.getAttribute("text").equalsIgnoreCase("success!")
								&& !remittanceTxnSuccessScreenMessage.getAttribute("text").contains("processing")) {
							tableStatusXpath = xpathHead + "[" + i
									+ "]/android.widget.LinearLayout/android.widget.ImageView";
						} else if (remittanceTxnScreen.getAttribute("text").equalsIgnoreCase("failed!")
								|| remittanceTxnSuccessScreenMessage.getAttribute("text").contains("processing")) {
							tableStatusXpath = xpathHead + "[" + i
									+ "]/android.widget.LinearLayout/android.widget.ImageView";
						}
					}
					WebElement tableStatus = mdriver.findElement(By.xpath(tableStatusXpath));
					tableStatus.getText();
					if (remittanceTxnScreen.getAttribute("text").equalsIgnoreCase("success!")
							&& !remittanceTxnSuccessScreenMessage.getAttribute("text").contains("processing")) {
						System.out.print("✔");
					} else if (remittanceTxnScreen.getAttribute("text").equalsIgnoreCase("success!")
							&& remittanceTxnSuccessScreenMessage.getAttribute("text").contains("processing")) {
						System.out.print("✔");
					} else if (remittanceTxnScreen.getAttribute("text").equalsIgnoreCase("failed!")) {
						System.out.print("✖");
					}
				}
			}
			System.out.println("");
		}
	}

	// Get Partner name
	public String partner() {
		return "FINO";
	}

	// Get mobile number from Ini file
	public String mobileNumFromIni() {
		return getLoginMobileFromIni(partner().toUpperCase() + "RetailerMobNum");
	}

	// Get otp from Ini file
	public String otpFromIni() {
		return partner().toUpperCase() + "OTP";
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

	// click on WebElement forcefully
	public void clickElement(WebElement element) {
		try {
			element.click();
		} catch (Exception e) {
			clickInvisibleElement(element);
		}
	}

	// Get remitter remaining limit
	public String limitRemaining(String mobNum, String type) throws NumberFormatException, ClassNotFoundException {
		double limit = Double.parseDouble(dbUtils.getLimitRemaining(mobNum, partner())) / 100;
		String expectedLimitRem = df.format(limit);
		String actualLimitRem = replaceSymbols(limitRemaining.getText().substring(34)).replaceAll("\\)", "");
		if (type.equals("actual")) {
			return actualLimitRem;
		} else if (type.equals("expected")) {
			return expectedLimitRem;
		}
		return null;
	}

	// Check limit remaining
	public void limitCheck(Map<String, String> usrData) throws NumberFormatException, ClassNotFoundException {
		if (usrData.get("ASSERTION").equalsIgnoreCase("Check Limit")) {
			wait.until(ExpectedConditions.visibilityOf(limitRemaining));
			Assert.assertEquals(limitRemaining("", "actual"),
					limitRemaining(getCustomerDetailsFromIni("ExistingNum"), "expected"));
			Log.info(limitRemaining.getText());
			mdriver.pressKeyCode(AndroidKeyCode.BACK);
			wait.until(ExpectedConditions.elementToBeClickable(okBtn));
			okBtn.click();
			Log.info("Home page displayed");
		}
	}

	// Unblock banks based on user data
	public void unblockBanks(Map<String, String> usrData) throws InterruptedException, ClassNotFoundException {
		if (usrData.get("UNBLOCKBANKS").equalsIgnoreCase("YES")) {
			List<String> list = dbUtils.getBankCodeToUnqueue(partner()); // get banks' codes
			if (list != null) {
				for (String code : list) { // iterate the result
					String disableQueuingURL = "https://" + getEnvURLfromIni()
							+ ".novopay.in/portal/remittance/disable/queuing?bankcode=" + code;

					mdriver.pressKeyCode(AndroidKeyCode.HOME);
					wait.until(ExpectedConditions.elementToBeClickable(chromeApp));
					chromeApp.click();
					try {
						// wait.until(ExpectedConditions.elementToBeClickable(chromeURLBar));
						chromeURLBar.click();
						try {
							deleteURL.click();
						} catch (Exception e) {
						}
					} catch (Exception e) {
						googleSearch.click();
					}
					chromeURLBar.sendKeys(disableQueuingURL);
					wait.until(ExpectedConditions.elementToBeClickable(urlSuggestion));
					urlSuggestion.click();
					wait.until(ExpectedConditions.elementToBeClickable(disableText));
					Log.info(disableText.getText());
					mdriver.pressKeyCode(187);
					/*
					 * try { mdriver.findElement(By.xpath(
					 * "//android.widget.TextView[@content-desc='Novopay Retailer']/parent::android.widget.FrameLayout"
					 * )) .click(); } catch (Exception e) { mdriver.findElement(By.xpath(
					 * "//android.widget.FrameLayout[@content-desc=\"Novopay Retailer,Unlocked\"]/android.view.View"
					 * )) .click(); }
					 */
					mdriver.findElement(By.xpath("//*[contains(@content-desc,'Novopay Retailer')]")).click();
				}
			}
		}
	}

	// Update queuing_config table based on user data
	public void unblockQueuingConfig(Map<String, String> usrData) throws ClassNotFoundException {
		if (usrData.get("UPDATEQCONFIG").equalsIgnoreCase("YES")) {
			dbUtils.updateQueuingConfig(usrData.get("QTHRESHOLD"), partner(), usrData.get("QCODE"));
		} else if (usrData.get("UPDATEQCONFIG").equalsIgnoreCase("Yes(DeleteFRC)")) {
			// delete records from failed_remittance_code table
			dbUtils.deleteRecordsFromFRC(usrData.get("QCODE"), partner());
			dbUtils.updateQueuingConfig(usrData.get("QTHRESHOLD"), partner(), usrData.get("QCODE"));
		}
	}

	// Check if Blackout is to be enabled or not
	public void blackoutCheck(Map<String, String> usrData) throws ClassNotFoundException {
		if (usrData.get("BLACKOUTCHECK").equalsIgnoreCase("YES")) {
			dbUtils.updateBlackoutDuration("300");
		} else if (usrData.get("BLACKOUTCHECK").equalsIgnoreCase("No")) {
			dbUtils.updateBlackoutDuration("1");
		}
	}

	// SMS assertion
	public void assertionOnSMS(Map<String, String> usrData) throws ClassNotFoundException {
		String successSMS = "Dear customer, transfer of Rs " + usrData.get("AMOUNT") + ".00" + " has been initiated to "
				+ usrData.get("BENENAME") + " (" + dbUtils.beneAccountPAN() + ", "
				+ dbUtils.getBank(usrData.get("BENEIFSC")) + "). Txn Id: " + txnDetailsFromIni("GetTxnRefNo", "") + "";

		String partialSuccessSMS = "Dear customer, transfer of Rs " + txnDetailsFromIni("GetTxfAmount", "")
				+ " has been initiated to " + usrData.get("BENENAME") + " (" + dbUtils.beneAccountPAN() + ", "
				+ dbUtils.getBank(usrData.get("BENEIFSC")) + "). Txn Id: " + txnDetailsFromIni("GetTxnRefNo", "") + "";

		String queuedTxnSMS = "Txn for Rs. " + usrData.get("AMOUNT") + ".00" + " to " + usrData.get("BENENAME") + " ("
				+ dbUtils.beneAccountPAN() + ", " + dbUtils.getBank(usrData.get("BENEIFSC"))
				+ ") is accepted, will be processed once Bank is up. Txn Id: " + dbUtils.paymentRefCode(partner()) + "";
		if (usrData.get("ASSERTION").equalsIgnoreCase("Success SMS")) {
			Assert.assertEquals(successSMS, dbUtils.sms());
			Log.info(successSMS);
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Partial Success SMS")) {
			Assert.assertEquals(partialSuccessSMS, dbUtils.sms());
			Log.info(partialSuccessSMS);
		} else if (usrData.get("ASSERTION").equalsIgnoreCase("Queued Txn SMS")
				&& remittanceTxnSuccessScreenMessage.getAttribute("text").contains("processing")) {
			Assert.assertEquals(queuedTxnSMS, dbUtils.sms());
			Log.info(queuedTxnSMS);
		}
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

	public void enterRemitterDetails(Map<String, String> usrData) {
		wait.until(ExpectedConditions.elementToBeClickable(remitterNameTextField));
		Log.info("Customer name entered");
		sendText(remitterNameTextField, getCustomerDetailsFromIni("NewName"));
		if (usrData.get("GENDER").equalsIgnoreCase("MALE")) {
			maleGenderRadioBtn.click();
		} else if (usrData.get("GENDER").equalsIgnoreCase("FEMALE")) {
			femaleGenderRadioBtn.click();
		}
		Log.info("Gender selected");
		// wait.until(ExpectedConditions.elementToBeClickable(remitterDOBTextField));
		// remitterDOBTextField.click();
		// calSubmitBtn.click();
		// Log.info("Date of birth selected");
		remitterSubmitBtn.click();
		Log.info("Submit button clicked");
	}

	public void otpScreen(Map<String, String> usrData)
			throws ClassNotFoundException, InterruptedException, ParseException {
		wait.until(ExpectedConditions.elementToBeClickable(otpTextField));
		if (usrData.get("OTP").equalsIgnoreCase("Valid")) {
			sendText(otpTextField, getAuthfromIni(otpFromIni()));
		} else if (usrData.get("OTP").equalsIgnoreCase("Invalid")) {
			sendText(otpTextField, "111111");
		} else if (usrData.get("OTP").equalsIgnoreCase("Resend")) {
			wait.until(ExpectedConditions.elementToBeClickable(resendOTPBtn));
			resendOTPBtn.click();
			sendText(otpTextField, getAuthfromIni(otpFromIni()));
			Log.info("OTP re-sent");
		}
		Log.info("OTP entered");
		if (usrData.get("OTPSCREENBUTTON").equalsIgnoreCase("Confirm")) {
			wait.until(ExpectedConditions.elementToBeClickable(submitOTPBtn));
			submitOTPBtn.click();
			Log.info("Submit OTP button clicked");

			if (usrData.get("OTP").equalsIgnoreCase("Invalid")) {
				wait.until(ExpectedConditions.elementToBeClickable(otpTextField));
				otpTextField.click();
				sendText(otpTextField, getAuthfromIni(otpFromIni()));
				submitOTPBtn.click();
			}
			if (usrData.get("ADDBENE").equalsIgnoreCase("Indirectly")) {
				wait.until(ExpectedConditions.visibilityOf(successHeader));
				Assert.assertEquals(successMsg.getText(), "Beneficiary added succesfully");
				Log.info(successMsg.getText());
				successExit.click();
				Log.info("Done button clicked");
			} else if (usrData.get("ADDBENE").equalsIgnoreCase("Directly")) {
				moneyTransfer(usrData);
			} else if (usrData.get("DELETEBENE").equalsIgnoreCase("Yes")) {
				wait.until(ExpectedConditions.elementToBeClickable(remitterMobileNoTextField));
				Log.info("Beneficiary deleted");
			}
		} else if (usrData.get("OTPSCREENBUTTON").equalsIgnoreCase("Cancel")) {
			wait.until(ExpectedConditions.elementToBeClickable(cancelOTPBtn));
			cancelOTPBtn.click();
			Log.info("Cancel button clicked");
			if (!usrData.get("ASSERTION").equalsIgnoreCase("Refresh Button")) {
				mdriver.pressKeyCode(AndroidKeyCode.BACK);
				wait.until(ExpectedConditions.elementToBeClickable(okBtn));
				okBtn.click();
				Log.info("Home page displayed");
			}
		}
	}

	// FCM assertion
	public void assertionOnFCM(Map<String, String> usrData) throws ClassNotFoundException {
		String successSummaryFCMHeading = "Cash To Account: Success";
		String failSummaryFCMHeading = "Cash To Account: Failed";
		String successFCMHeading = "Cash To Account : SUCCESS";
		String failFCMHeading = "Cash To Account : FAIL";
		String beneSuccessFCMHeading = "Beneficiary Validation:SUCCESS";
		String beneFailFCMHeading = "Beneficiary Validation:FAIL";
		String queuingDisableFCMHeading = dbUtils.getBank(usrData.get("BENEIFSC")) + " IMPS service is working";
		String queuingEnableFCMHeading = dbUtils.getBank(usrData.get("BENEIFSC")) + " IMPS service is down";

		String balance = getBalanceFromIni("Get", "retailer", "");
		double beneAmount = Double.parseDouble(dbUtils.getBeneAmount(partner().toUpperCase()));
		String beneAmt = df.format(beneAmount);
		String successSummaryFCMContent = "Cash to account request has been processed.\r\nRs " + usrData.get("AMOUNT")
				+ ".00 transferred successfully.\r\nClick here for details";
		String failSummaryFCMContent = "Cash to account request has been processed.\r\nHowever, Rs "
				+ usrData.get("AMOUNT") + ".00 transfer failed.\r\nClick here for details";
		String successFCMContent = "Customer: " + getCustomerDetailsFromIni("ExistingNum") + " Money Transfer of INR "
				+ usrData.get("AMOUNT") + ".00" + " has been accepted for IFSC:" + usrData.get("BENEIFSC") + ", A/C:"
				+ getAccountNumberFromIni("GetNum") + ", charges INR " + txnDetailsFromIni("GetCharges", "")
				+ ", reference no. " + dbUtils.paymentRefCode(partner()) + " Agent Balance: " + balance;
		String failFCMContent = "Customer: " + getCustomerDetailsFromIni("ExistingNum")
				+ " Transaction failed due to invalid beneficiary IFSC code. Transaction Reference Number : "
				+ dbUtils.paymentRefCode(partner());
		String queuedTxnFCMContent = "Customer: " + getCustomerDetailsFromIni("ExistingNum")
				+ " Transactions have been accepted for processing, Beneficiary Bank switch down, "
				+ "please check the status after some time Agent Balance: " + balance;
		String beneSuccessFCMContent = "Customer: " + getCustomerDetailsFromIni("ExistingNum") + "  Account Number "
				+ getAccountNumberFromIni("GetNum") + " in " + dbUtils.getBank(usrData.get("BENEIFSC")) + " belongs to "
				+ usrData.get("BENENAME") + ". Please collect Rs. " + beneAmt + " from customer."
				+ "  Agent Balance: Rs. " + balance;
		String beneFailFCMContent = "Customer: " + getCustomerDetailsFromIni("ExistingNum")
				+ "  Transaction failed due to invalid beneficiary IFSC code. ";
		String queuingDisableFCMContent = dbUtils.getBank(usrData.get("BENEIFSC"))
				+ " IMPS service is working now. Pending transactions will be processed now";
		String queuingEnableFCMContent = dbUtils.getBank(usrData.get("BENEIFSC"))
				+ " IMPS service is down. Transactions are being accepted for processing. "
				+ "Processing will be done once the service is up";

		int j = 0, count = 0;
		if (usrData.get("ASSERTION").equalsIgnoreCase("Success FCM")
				|| usrData.get("ASSERTION").equalsIgnoreCase("Failed FCM")) {
			count = 2;
		} else {
			count = 1;
		}
		for (j = 1; j <= count; j++) {
			String fcmHeadingxpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[" + j
					+ "]/android.widget.TextView[1]";
			String fcmContentxpath = "//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[" + j
					+ "]/android.widget.TextView[2]";
			WebElement fcmHeading = mdriver.findElement(By.xpath(fcmHeadingxpath));
			WebElement fcmContent = mdriver.findElement(By.xpath(fcmContentxpath));

			switch (usrData.get("ASSERTION")) {
			case "Success FCM":
				if (j == 1) {
					Assert.assertEquals(fcmHeading.getText(), successSummaryFCMHeading);
					Assert.assertEquals(fcmContent.getText(), successSummaryFCMContent);
				} else if (j == 2) {
					Assert.assertEquals(fcmHeading.getText(), successFCMHeading);
					Assert.assertEquals(fcmContentString(fcmContent.getText(), 0, 20, 21, 148, 149, 173),
							successFCMContent);
				}
				break;
			case "Failed FCM":
				if (j == 1) {
					Assert.assertEquals(fcmHeading.getText(), failSummaryFCMHeading);
					Assert.assertEquals(fcmContent.getText(), failSummaryFCMContent);
				} else if (j == 2) {
					Assert.assertEquals(fcmHeading.getText(), failFCMHeading);
					Assert.assertEquals(fcmContentString(fcmContent.getText(), 0, 20, 21, 77, 78, 119), failFCMContent);
				}
				break;
			case "Queued FCM":
				if (j == 1) {
					Assert.assertEquals(fcmHeading.getText(), successSummaryFCMHeading);
					Assert.assertEquals(fcmContent.getText(), successSummaryFCMContent);
				} else if (j == 2) {
					Assert.assertEquals(fcmHeading.getText(), successFCMHeading);
					Assert.assertEquals(fcmContentString(fcmContent.getText(), 0, 20, 21, 138, 139, 163),
							queuedTxnFCMContent);
				}
				break;
			case "BeneSuccess FCM":
				if (j == 1) {
					Assert.assertEquals(fcmHeading.getText(), beneSuccessFCMHeading);
					Assert.assertEquals(fcmContentString(fcmContent.getText(), 0, 21, 22, 127, 128, 156),
							beneSuccessFCMContent);
				}
				break;
			case "BeneFailed FCM":
				if (j == 1) {
					Assert.assertEquals(fcmHeading.getText(), beneFailFCMHeading);
					Assert.assertEquals(fcmContentString(fcmContent.getText(), 0, 21, 22, 79, 0, 0),
							beneFailFCMContent);
				}
				break;
			case "EnableQueuing FCM":
				if (j == 1) {
					Assert.assertEquals(fcmHeading.getText(), queuingEnableFCMHeading);
					Assert.assertEquals(fcmContent.getText(), queuingEnableFCMContent);
				}
				break;
			case "DisableQueuing FCM":
				if (j == 1) {
					Assert.assertEquals(fcmHeading.getText(), queuingDisableFCMHeading);
					Assert.assertEquals(fcmContent.getText(), queuingDisableFCMContent);
				}
				break;
			}
			System.out.println(fcmHeading.getText());
			System.out.println(fcmContent.getText());
		}
		mdriver.pressKeyCode(AndroidKeyCode.BACK);
	}

	public String fcmContentString(String fcm, int a, int b, int c, int d, int e, int f) {
		String fcmContent = "";
		if (e == 0 && f == 0) {
			fcmContent = fcm.substring(a, b) + " " + fcm.substring(c, d);
		} else {
			fcmContent = fcm.substring(a, b) + " " + fcm.substring(c, d) + " " + fcm.substring(e, f);
		}
		return fcmContent;
	}

	public double commissionAndTDS(String commOrTds) throws NumberFormatException, ClassNotFoundException {
		int amnt = (int) Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
		int a = 0, b = 0;
		// 2250
		if (amnt > 5000) {
			a = amnt % 5000; // 250
			b = amnt / 5000; // 4
		} else {
			a = amnt;
			b = 0;
		}

		String amount = Integer.toString(a);
		String comm = dbUtils.getRemittanceComm(amount, dbUtils.getChargeCategory(mobileNumFromIni()), partner());
		double commission = 30.5 * b + Double.parseDouble(comm);
		double tds = (roundTo2Decimals(30.5 * Double.parseDouble(dbUtils.getTDSPercentage(mobileNumFromIni())) / 10000))
				* b
				+ roundTo2Decimals((commission - (30.5) * b)
						* Double.parseDouble(dbUtils.getTDSPercentage(mobileNumFromIni())) / 10000);

		if (commOrTds.equalsIgnoreCase("comm")) {
			return commission;
		} else
			return tds;
	}

	public double roundTo2Decimals(double value) {
		return Math.round(value * 100.0) / 100.0;
	}
}
