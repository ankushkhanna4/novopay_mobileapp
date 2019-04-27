package in.novopay.platform_ui.pages.mobile;

import java.awt.AWTException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import in.novopay.platform_ui.utils.BasePage;
import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.Log;

public class SettingsPage extends BasePage {
	DBUtils dbUtils = new DBUtils();
	DecimalFormat df = new DecimalFormat("#.00");

	WebDriverWait wait = new WebDriverWait(wdriver, 30);
	WebDriverWait waitSave = new WebDriverWait(wdriver, 3);

	@FindBy(xpath = "//*[@class='fa fa-bars fa-lg text-white']")
	WebElement menu;

	@FindBy(xpath = "//span[contains(text(),'Settings')]")
	WebElement settings;

	@FindBy(xpath = "//i[contains(@class,'np np-refresh')]")
	WebElement refreshButton;

	@FindBy(xpath = "//i[contains(@class,'np np-sync')]")
	WebElement syncButton;

	@FindBy(xpath = "//span[contains(text(),'wallet balance')]")
	WebElement retailerWallet;

	@FindBy(xpath = "//span[contains(text(),'wallet balance')]/parent::p/following-sibling::p/span")
	WebElement retailerWalletBalance;

	@FindBy(xpath = "//span[contains(text(),'cashout balance')]")
	WebElement cashoutWallet;

	@FindBy(xpath = "//span[contains(text(),'cashout balance')]/parent::p/following-sibling::p/span")
	WebElement cashoutWalletBalance;

	@FindBy(xpath = "//span[contains(text(),'merchant balance')]")
	WebElement merchantWallet;

	@FindBy(xpath = "//span[contains(text(),'merchant balance')]/parent::p/following-sibling::p/span")
	WebElement merchantWalletBalance;

	@FindBy(xpath = "//h1[contains(text(),'Settings')]")
	WebElement pageTitle;

	@FindBy(xpath = "//span[contains(text(),'Capital First')]/parent::li")
	WebElement capitalFirstIcon;

	@FindBy(id = "capital-first-money-transfer-mobile-number")
	WebElement depositorMobNum;

	@FindBy(xpath = "//label[@for='settlement-mode-retailer-credit']")
	WebElement retailerCreditRadioButton;

	@FindBy(xpath = "//label[@for='settlement-mode-bank-account']")
	WebElement bankAccountRadioButton;

	@FindBy(xpath = "//button[contains(text(),'Save Changes')]")
	WebElement saveChangesButton;

	@FindBy(xpath = "//button[contains(text(),'Clear')]")
	WebElement clearButton;

	@FindBy(xpath = "//*[contains(@class,'retailer-account-status')]")
	WebElement accountStatus;

	@FindBy(xpath = "//i[contains(@class,'icon-32-size')]")
	WebElement blockedIcon;

	@FindBy(xpath = "//h5[contains(text(),'Enter 4 digit PIN')]")
	WebElement MPINScreen;

	@FindBy(id = "money-transfer-mpin-number")
	WebElement enterMPIN;

	@FindBy(xpath = "//h5[contains(text(),'Enter 4 digit PIN')]/parent::div/following-sibling::div/following-sibling::div/button[contains(text(),'Submit')]")
	WebElement submitMPIN;

	@FindBy(xpath = "//div//button[contains(text(),'Cancel')]")
	WebElement cancelButton;

	@FindBy(xpath = "//pin-modal/div//button[contains(text(),'Submit')]")
	WebElement mpinSubmitButton;

	@FindBy(xpath = "//div[contains(@class,'settlement-modal')]/div/div/div/h4[contains(text(),'!')]")
	WebElement settingsTxnScreen;

	@FindBy(xpath = "//div[contains(@class,'settlement-modal')]/div/div/div")
	WebElement settingsTxnScreenType;

	@FindBy(xpath = "//div[contains(@class,'settlement-modal')]/div/div/div/following-sibling::div/div[1]")
	WebElement settingsTxnScreenMessage;

	@FindBy(xpath = "//button[contains(text(),'Exit')]")
	WebElement exitButton;

	@FindBy(xpath = "//button[contains(text(),'Retry')]")
	WebElement retryButton;

	@FindBy(xpath = "//button[contains(text(),'Done')]")
	WebElement doneButton;

	@FindBy(xpath = "//div[contains(@class,'toast-message')]")
	WebElement toasterMsg;

	@FindBy(xpath = "//button[contains(text(),'Edit')]")
	WebElement editButton;

	@FindBy(xpath = "//button[contains(text(),'Ok')]")
	WebElement okButton;

	@FindBy(id = "settlement-mode-retailer-name")
	WebElement accHolderName;

	@FindBy(id = "settlement-mode-create-field-retailer-bank-ifsc-list")
	WebElement ifscCode;

	@FindBy(xpath = "//input[contains(@id,'settlement-mode-create-field-retailer-bank-ifsc-list')]/parent::div/following-sibling::span")
	WebElement ifscSearchIcon;

	@FindBy(id = "settlement-mode-retailer-account-number")
	WebElement accNumber;

	@FindBy(xpath = "//*[@class='custom-file-upload']")
	WebElement uploadFile;

	@FindBy(id = "settlement-mode-retailer-file-name")
	WebElement inputFile;

	@FindBy(xpath = "//button[contains(text(),'Submit')]")
	WebElement submitButton;

	@FindBy(xpath = "//h5[contains(text(),'IFSC Search')]")
	WebElement ifscSearchScreen;

	@FindBy(xpath = "//*[@id='ifsc-search-bank']//span[contains(text(),'Select...')]/parent::span")
	WebElement ifscSearchBankList;

	@FindBy(xpath = "//*[@id='ifsc-search-state']//span[contains(text(),'Select...')]/parent::span")
	WebElement ifscSearchStateList;

	@FindBy(id = "ifsc-search-district")
	WebElement ifscSearchDistrict;

	@FindBy(id = "ifsc-search-branch")
	WebElement ifscSearchBranch;

	@FindBy(xpath = "//button[contains(text(),'Search')]")
	WebElement ifscSearchButton;

	@FindBy(xpath = "//div/i[contains(@class,'fa fa-arrow-left')]")
	WebElement ifscSearchBack;

	@FindBy(id = "search-bank-ifsc")
	WebElement ifscSearch;

	@FindBy(xpath = "//div[contains(@class,'ifsc-search-modal')]//button[contains(text(),'OK')]")
	WebElement ifscSearchOK;

	@FindBy(xpath = "//span[contains(@class,'custom-ul-errormessage')]/span[contains(text(),'Branch')]")
	WebElement validateIFSC;

	@FindBy(xpath = "//li[1][contains(@class,'notifications')]//strong")
	WebElement fcmHeading;

	@FindBy(xpath = "//li[1][contains(@class,'notifications')]/span[2]")
	WebElement fcmContent;

	// Load all objects
	public SettingsPage(WebDriver wdriver) {
		super(wdriver);
		PageFactory.initElements(wdriver, this);
	}

	// Perform action on page based on given commands
	public void settings(Map<String, String> usrData)
			throws InterruptedException, AWTException, IOException, ClassNotFoundException {

		try {

			if (usrData.get("ASSERTION").contains("FCM")) {
				assertionOnFCM(usrData);
			} else {
				// Updating org_stlmnt_info table as per test case
				if (usrData.get("MODE").equalsIgnoreCase("Change to Bank Account")) {
					dbUtils.updateOrgSettlementInfo("TO_WALLET", "2", "1", "(NULL)",
							getLoginMobileFromIni("RBLRetailerMobNum"));
				} else if (usrData.get("MODE").equalsIgnoreCase("Change to Retailer Credit")) {
					dbUtils.updateOrgSettlementInfo("TO_BANK", "2", "1", "(NULL)",
							getLoginMobileFromIni("RBLRetailerMobNum"));
				} else if (usrData.get("MODE").equalsIgnoreCase("Keep Bank Account")) {
					dbUtils.updateOrgSettlementInfo("TO_BANK", "2", "1", "(NULL)",
							getLoginMobileFromIni("RBLRetailerMobNum"));
				} else if (usrData.get("MODE").equalsIgnoreCase("Keep Retailer Credit")) {
					dbUtils.updateOrgSettlementInfo("TO_WALLET", "2", "1", "(NULL)",
							getLoginMobileFromIni("RBLRetailerMobNum"));
				}

				if (usrData.get("ASSERTION").equalsIgnoreCase("Pending")) {
					dbUtils.updateOrgSettlementInfo("TO_WALLET", "1", "1", "(NULL)",
							getLoginMobileFromIni("RBLRetailerMobNum"));
				} else if (usrData.get("ASSERTION").equalsIgnoreCase("Rejected")) {
					dbUtils.updateOrgSettlementInfo("TO_WALLET", "3", "1", "(NULL)",
							getLoginMobileFromIni("RBLRetailerMobNum"));
				} else if (usrData.get("ASSERTION").equalsIgnoreCase("Blocked")) {
					dbUtils.updateOrgSettlementInfo("TO_WALLET", "4", "0", "Incorrect bank details",
							getLoginMobileFromIni("RBLRetailerMobNum"));
				}

				menu.click();
				wait.until(ExpectedConditions.elementToBeClickable(settings));
				clickInvisibleElement(settings);
				Log.info("Settings option clicked");
				wait.until(ExpectedConditions.elementToBeClickable(pageTitle));
				menu.click();

				if (usrData.get("ASSERTION").equalsIgnoreCase("Pending")) {
					wait.until(ExpectedConditions.elementToBeClickable(accountStatus));
					Assert.assertEquals(accountStatus.getText(), "PENDING VERIFICATION");
					Log.info("The status is " + accountStatus.getText());
				} else if (usrData.get("ASSERTION").equalsIgnoreCase("Rejected")) {
					wait.until(ExpectedConditions.elementToBeClickable(accountStatus));
					Assert.assertEquals(accountStatus.getText(), "REJECTED");
					Log.info("The status is " + accountStatus.getText());
				} else if (usrData.get("ASSERTION").equalsIgnoreCase("Blocked")) {
					wait.until(ExpectedConditions.elementToBeClickable(blockedIcon));
					Log.info("The status is blocked");

				}

				if (usrData.get("ASSERTION").equalsIgnoreCase("Pending")
						|| usrData.get("ASSERTION").equalsIgnoreCase("Blocked")) {
					Log.info("No edit is allowed on this page");
				} else {
					// Changing mode
					if (usrData.get("MODE").contains("Change")) {
						if (usrData.get("MODE").equalsIgnoreCase("Change to Bank Account")) {
							waitSave.until(ExpectedConditions.elementToBeClickable(bankAccountRadioButton));
							Thread.sleep(1000);
							clickElement(bankAccountRadioButton);
						} else if (usrData.get("MODE").equalsIgnoreCase("Change to Retailer Credit")) {
							waitSave.until(ExpectedConditions.elementToBeClickable(retailerCreditRadioButton));
							Thread.sleep(1000);
							clickElement(retailerCreditRadioButton);
						}
						Log.info("Radio button clicked");
						waitSave.until(ExpectedConditions.elementToBeClickable(saveChangesButton));
						Thread.sleep(1000);
						saveChangesButton.click();
						Log.info("Save changes button clicked");
					}

					if (usrData.get("MODE").contains("Keep")) {
						if (usrData.get("MODE").equalsIgnoreCase("Keep Bank Account")) {
							wait.until(ExpectedConditions.elementToBeClickable(editButton));
							clickElement(editButton);
							wait.until(ExpectedConditions.visibilityOf(settingsTxnScreen));
							Log.info("Txn screen displayed");
							assertionOnInfoScreen(usrData);
							wait.until(ExpectedConditions.elementToBeClickable(okButton));
							okButton.click();
							Log.info("Ok button clicked");
						} else if (usrData.get("MODE").equalsIgnoreCase("Keep Retailer Credit")) {
							wait.until(ExpectedConditions.elementToBeClickable(editButton));
							clickElement(editButton);

							if (!usrData.get("ACHOLDERNAME").equalsIgnoreCase("SKIP")) {
								wait.until(ExpectedConditions.elementToBeClickable(accHolderName));
								accHolderName.clear();
								accHolderName.click();
								accHolderName.sendKeys(getBeneNameFromIni(usrData.get("ACHOLDERNAME")));
								Log.info("Account holder name '" + usrData.get("ACHOLDERNAME") + "' entered");

								if (usrData.get("IFSCTYPE").equalsIgnoreCase("Manual")) {
									wait.until(ExpectedConditions.elementToBeClickable(ifscCode));
									ifscCode.clear();
									ifscCode.click();
									ifscCode.sendKeys(usrData.get("IFSCCODE"));
									Log.info("IFSC code '" + usrData.get("IFSCCODE") + "' entered");
								} else if (usrData.get("IFSCTYPE").equalsIgnoreCase("Search Screen")) {
									wait.until(ExpectedConditions.elementToBeClickable(ifscSearchIcon));
									ifscSearchIcon.click();
									Log.info("IFSC search icon clicked");
									wait.until(ExpectedConditions.visibilityOf(ifscSearchScreen));
									wait.until(ExpectedConditions.visibilityOf(ifscSearchBankList));
									ifscSearchBankList.click();
									Log.info("IFSC bank drop down clicked");
									String ifscBank = "//li[contains(text(),'"
											+ dbUtils.ifscCodeDetails(usrData.get("IFSCCODE"), "bank") + "')]";
									WebElement ifscSearchBank = wdriver.findElement(By.xpath(ifscBank));
									ifscSearchBank.click();
									Log.info("IFSC bank selected");
									ifscSearchStateList.click();
									Log.info("IFSC state drop down clicked");
									String stateFromDB = dbUtils.ifscCodeDetails(usrData.get("IFSCCODE"), "state");
									String stateFirstLetterCapitalized = stateFromDB.substring(0, 1).toUpperCase()
											+ stateFromDB.substring(1).toLowerCase();
									String ifscState = "//li[contains(text(),'" + stateFirstLetterCapitalized + "')]";
									WebElement ifscSearchState = wdriver.findElement(By.xpath(ifscState));
									ifscSearchState.click();
									Log.info("IFSC state selected");
									ifscSearchDistrict
											.sendKeys(dbUtils.ifscCodeDetails(usrData.get("IFSCCODE"), "district"));
									Log.info("IFSC district entered");
									ifscSearchBranch
											.sendKeys(dbUtils.ifscCodeDetails(usrData.get("IFSCCODE"), "branch"));
									Log.info("IFSC branch entered");
									ifscSearchButton.click();
									Log.info("Search button clicked");
									waitForSpinner();
									wait.until(ExpectedConditions.visibilityOf(ifscSearchBack));
									String searchCode = "//span[contains(@class,'add-beneficiary-list')][contains(text(),'"
											+ usrData.get("IFSCCODE") + "')]/parent::li";
									WebElement ifscSearchCode = wdriver.findElement(By.xpath(searchCode));
									wait.until(ExpectedConditions.elementToBeClickable(ifscSearchCode));
									ifscSearchCode.click();
									Log.info("IFSC code '" + usrData.get("IFSCCODE") + "' entered");
									ifscSearchOK.click();
									Log.info("OK button clicked");
								} else if (usrData.get("IFSCTYPE").equalsIgnoreCase("Drop Down")) {
									wait.until(ExpectedConditions.elementToBeClickable(ifscCode));
									ifscCode.clear();
									ifscCode.click();
									String searchCode = "//span[contains(@class,'add-beneficiary-sublist')][contains(text(),'"
											+ usrData.get("IFSCCODE") + "')]/parent::li";
									WebElement ifscSearchCode = wdriver.findElement(By.xpath(searchCode));
									wait.until(ExpectedConditions.elementToBeClickable(ifscSearchCode));
									ifscSearchCode.click();
									Log.info("IFSC code '" + usrData.get("IFSCCODE") + "' entered");
								}
								getBankNameFromIni(dbUtils.ifscCodeDetails(usrData.get("IFSCCODE"), "bank"));
								wait.until(ExpectedConditions.visibilityOf(validateIFSC)); // wait for Branch name to be
																							// displayed
								Log.info(validateIFSC.getText());

								wait.until(ExpectedConditions.elementToBeClickable(accNumber));
								accNumber.clear();
								accNumber.click();
								accNumber.sendKeys(getAccountNumberFromIni(usrData.get("ACNUMBER")));
								Log.info("Bene account number '" + getAccountNumberFromIni("GetNum") + "' entered");
							}
							// uploadFile.sendKeys("C:/Users/ANKUSH/Pictures/Sample Pictures/Testing.jpg");
							// JavascriptExecutor jse = (JavascriptExecutor) wdriver;
							// jse.executeScript("document.getElementById('settlement-mode-retailer-file-name').value
							// = 'Testing.jpg';");
							uploadFile(uploadFile);
							Log.info("Image selected");

							String buttonName = usrData.get("SETTINGSBUTTON");
							String buttonXpath = "//button[contains(text(),'" + buttonName + "')]";
							WebElement button = wdriver.findElement(By.xpath(buttonXpath));
							wait.until(ExpectedConditions.elementToBeClickable(button));
							Thread.sleep(3000);
							clickElement(button);
							if (buttonName.equalsIgnoreCase("Clear")) {
								Thread.sleep(2000);
								Log.info("Clear button clicked");
							} else if (buttonName.equalsIgnoreCase("Submit")) {
								Log.info("Submit button clicked");
							}
						}
					}
					if (usrData.get("ASSERTION").equalsIgnoreCase("Same Data")) {
						Assert.assertEquals(toasterMsg.getText(),
								"The Bank detail provided already exists in our system, kindly check and update");
						Log.info(toasterMsg.getText());
					} else if (!usrData.get("MODE").equalsIgnoreCase("Keep Bank Account")
							&& !usrData.get("SETTINGSBUTTON").equalsIgnoreCase("Clear")) {
						wait.until(ExpectedConditions.visibilityOf(MPINScreen));
						Log.info("MPIN screen displayed");
						wait.until(ExpectedConditions.elementToBeClickable(enterMPIN));
						if (usrData.get("MPIN").equalsIgnoreCase("Valid")) {
							enterMPIN.sendKeys(getAuthfromIni("MPIN"));
						} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
							enterMPIN.sendKeys("9999");
						}
						Log.info("MPIN entered");

						String mpinButtonName = usrData.get("MPINSCREENBUTTON");
						String mpinScreenButtonXpath = "//h5[contains(text(),'Enter 4 digit PIN')]/parent::div/"
								+ "following-sibling::div/following-sibling::div/button[contains(text(),'"
								+ mpinButtonName + "')]";
						WebElement mpinScreenButton = wdriver.findElement(By.xpath(mpinScreenButtonXpath));
						wait.until(ExpectedConditions.elementToBeClickable(mpinScreenButton));
						mpinScreenButton.click();
						Log.info(mpinButtonName + " button clicked");
						if (mpinButtonName.equalsIgnoreCase("Cancel")) {
							Log.info("Cancel button clicked");
						} else if (mpinButtonName.equalsIgnoreCase("Submit")) {
							waitForSpinner();
							wait.until(ExpectedConditions.visibilityOf(settingsTxnScreen));
							Log.info("Txn screen displayed");

							// Verify the details on transaction screen
							if (settingsTxnScreen.getText().equalsIgnoreCase("Success!")) {
								assertionOnSuccessScreen(usrData);
								wait.until(ExpectedConditions.elementToBeClickable(doneButton));
								doneButton.click();
								Log.info("Done button clicked");
							} else if (settingsTxnScreen.getText().equalsIgnoreCase("Failed!")) {
								if (usrData.get("MPIN").equalsIgnoreCase("Valid")) {
									assertionOnFailedScreen(usrData);
									if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
										Log.info("Clicking exit button");
									} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
										retryButton.click();
										wait.until(ExpectedConditions.visibilityOf(MPINScreen));
										Log.info("MPIN screen displayed");
										wait.until(ExpectedConditions.elementToBeClickable(enterMPIN));
										enterMPIN.click();
										enterMPIN.sendKeys(getAuthfromIni("MPIN"));
										Log.info("MPIN entered");
										wait.until(ExpectedConditions.elementToBeClickable(submitMPIN));
										submitMPIN.click();
										Log.info("Submit button clicked");
										waitForSpinner();
										wait.until(ExpectedConditions.visibilityOf(settingsTxnScreen));
										Log.info("Txn screen displayed");
										assertionOnFailedScreen(usrData);
									}
									wait.until(ExpectedConditions.elementToBeClickable(exitButton));
									exitButton.click();
									Log.info("Exit button clicked");
								} else if (usrData.get("MPIN").equalsIgnoreCase("Invalid")) {
									wait.until(ExpectedConditions.elementToBeClickable(settingsTxnScreenMessage));
									Log.info(settingsTxnScreenMessage.getText());
									if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Exit")) {
										exitButton.click();
										Log.info("Exit button clicked");
									} else if (usrData.get("TXNSCREENBUTTON").equalsIgnoreCase("Retry")) {
										retryButton.click();
										wait.until(ExpectedConditions.visibilityOf(MPINScreen));
										Log.info("MPIN screen displayed");
										wait.until(ExpectedConditions.elementToBeClickable(enterMPIN));
										enterMPIN.click();
										enterMPIN.sendKeys(getAuthfromIni("MPIN"));
										Log.info("MPIN entered");
										wait.until(ExpectedConditions.elementToBeClickable(submitMPIN));
										submitMPIN.click();
										Log.info("Submit button clicked");
										waitForSpinner();
										wait.until(ExpectedConditions.visibilityOf(settingsTxnScreen));
										Log.info("Txn screen displayed");
										assertionOnSuccessScreen(usrData);
										wait.until(ExpectedConditions.elementToBeClickable(doneButton));
										doneButton.click();
										Log.info("Done button clicked");
									}
								}
							}
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

	// Show balances in console
	public void displayInitialBalance(Map<String, String> usrData, String wallet) throws ClassNotFoundException {
		String walletBalance = dbUtils.getWalletBalance(mobileNumFromIni(), "retailer");
		String walletBal = walletBalance.substring(0, walletBalance.length() - 4);
		String cashoutBalance = dbUtils.getWalletBalance(mobileNumFromIni(), "cashout");
		String cashoutBal = cashoutBalance.substring(0, cashoutBalance.length() - 4);
		String merchantBalance = dbUtils.getWalletBalance(mobileNumFromIni(), "merchant");
		String merchantBal = merchantBalance.substring(0, merchantBalance.length() - 4);

		String initialWalletBal = replaceSymbols(retailerWalletBalance.getText());
		String initialCashoutBal = replaceSymbols(cashoutWalletBalance.getText());
		String initialMerchantBal = replaceSymbols(merchantWalletBalance.getText());

		// Compare wallet balance shown in WebApp to DB
		if (usrData.get("ASSERTION").equals("Initial Balance")) {
			Assert.assertEquals(walletBal, initialWalletBal);
			Assert.assertEquals(cashoutBal, initialCashoutBal);
			Assert.assertEquals(merchantBal, initialMerchantBal);
		}

		if (wallet.equalsIgnoreCase("retailer")) {
			Log.info("Retailer Balance: " + initialWalletBal);
		} else if (wallet.equalsIgnoreCase("cashout")) {
			Log.info("Cashout Balance: " + initialCashoutBal);
		} else if (wallet.equalsIgnoreCase("merchant")) {
			Log.info("Merchant Balance: " + initialMerchantBal);
		}
	}

	// Get wallet(s) balance
	@SuppressWarnings("null")
	public double getInitialBalance(String wallet) throws ClassNotFoundException {
		String initialWalletBal = replaceSymbols(retailerWalletBalance.getText());
		String initialCashoutBal = replaceSymbols(cashoutWalletBalance.getText());
		String initialMerchantBal = replaceSymbols(merchantWalletBalance.getText());

		// Converting balance from String to Double and returning the same
		if (wallet.equalsIgnoreCase("retailer")) {
			return Double.parseDouble(initialWalletBal);
		} else if (wallet.equalsIgnoreCase("cashout")) {
			return Double.parseDouble(initialCashoutBal);
		} else if (wallet.equalsIgnoreCase("merchant")) {
			return Double.parseDouble(initialMerchantBal);
		}
		return (Double) null;
	}

	// To refresh the wallet balance
	public void refreshBalance() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(refreshButton));
		clickInvisibleElement(refreshButton);
		wait.until(ExpectedConditions.elementToBeClickable(syncButton));
		wait.until(ExpectedConditions.elementToBeClickable(refreshButton));
		Log.info("Balance refreshed successfully");
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

	// Get mobile number from Ini file
	public String mobileNumFromIni() {
		String url = wdriver.getCurrentUrl();
		if (url.endsWith("rbl-transfer")) {
			return getLoginMobileFromIni("RBLRetailerMobNum");
		} else if (url.endsWith("axis-transfer")) {
			return getLoginMobileFromIni("AXISRetailerMobNum");
		}
		return null;
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

	// Verify details on success screen
	public void assertionOnSuccessScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("MODE").equalsIgnoreCase("Bank Account")) {
			Assert.assertEquals(settingsTxnScreenMessage.getText(),
					"Your Cashout Balance and Merchant Balance will now be credited to your verified bank account on End of Day basis.");
			Log.info(settingsTxnScreenMessage.getText());
		} else if (usrData.get("MODE").equalsIgnoreCase("Retailer Credit")) {
			Assert.assertEquals(settingsTxnScreenMessage.getText(),
					"Your Cashout Balance and Merchant Balance will now be credited to your retailer wallet on End of Day basis.");
			Log.info(settingsTxnScreenMessage.getText());
		}
	}

	// Verify details on failed screen
	public void assertionOnFailedScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("ASSERTION").equalsIgnoreCase("Invalid MPIN")) {
			Assert.assertEquals(settingsTxnScreenMessage.getText(), "Authentication Failed Invalid MPIN");
		}
		Log.info(settingsTxnScreenMessage.getText());
	}

	// Verify details on info screen
	public void assertionOnInfoScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		Assert.assertEquals(settingsTxnScreenMessage.getText(),
				"Please change settlement mode to Retailer Credit before updating bank details");
		Log.info(settingsTxnScreenMessage.getText());
	}

	public void uploadFile(WebElement cancelledCheque) throws InterruptedException, IOException {
		wait.until(ExpectedConditions.elementToBeClickable(cancelledCheque));
		Log.info("selecting cancelled cheque image");
		Thread.sleep(2000);
		cancelledCheque.click();
		Thread.sleep(500);
		String uploadFile = "C:\\Users\\Ankush\\Documents\\AutoIt Scripts\\UploadFile.exe";
		Runtime.getRuntime().exec(uploadFile);
		Thread.sleep(500);
	}

	// FCM assertion
	public void assertionOnFCM(Map<String, String> usrData) throws ClassNotFoundException {
		String successFCMHeading = "You have updated your account details with ";
		String successFCMContent = "The bank account details have been submitted for verification.";

		fcmMethod(successFCMHeading, successFCMContent);
	}

	public void fcmMethod(String heading, String content) {
		Assert.assertEquals(fcmHeading.getText().substring(0,43), heading);
		Assert.assertEquals(fcmContent.getText().substring(0,62), content);
		Log.info(fcmHeading.getText());
		Log.info(fcmContent.getText());
	}
}
