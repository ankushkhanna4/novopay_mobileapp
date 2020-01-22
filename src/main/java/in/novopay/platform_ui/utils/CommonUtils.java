package in.novopay.platform_ui.utils;

import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class CommonUtils extends BasePage {
	DBUtils dbUtils = new DBUtils();

	public Map<String, String> usrData;

	@FindBy(xpath = "//h4[contains(text(),'Welcome')]")
	WebElement welcomeMessage;

	@FindBy(xpath = "//h4[contains(text(),'Welcome')]/parent::div/following-sibling::div[2]/button")
	WebElement welcomeOKButton;

	@FindBy(xpath = "//button[contains(text(),'I Will Check Later')]")
	WebElement banner;

	@FindBy(xpath = "//*[@class='fa fa-bars fa-lg text-white']")
	WebElement menu;

	@FindBy(xpath = "//*[@class='slimScrollBar']")
	WebElement scrollBar;

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

	@FindBy(xpath = "//div[contains(@class,'spinner')]/parent::div")
	WebElement spinner;

	@FindBy(xpath = "//button[contains(text(),'OK. Got it!')]")
	WebElement welcomeButton;

	@FindBy(xpath = "//*[contains(@text,'Choose a wallet')]")
	WebElement chooseWalletScreen;

	@FindBy(id = "in.novopay.merchant:id/main_wallet")
	WebElement mainWalletRadioButton;

	@FindBy(id = "in.novopay.merchant:id/cashout_wallet")
	WebElement cashoutWalletRadioButton;

	@FindBy(id = "in.novopay.merchant:id/main_wallet_text")
	WebElement mainWalletScreenBalance;

	@FindBy(id = "in.novopay.merchant:id/cashout_wallet_text")
	WebElement cashoutWalletScreenBalance;

	@FindBy(id = "in.novopay.merchant:id/submit_button")
	WebElement chooseWalletProceedButton;

	@FindBy(id = "in.novopay.merchant:id/submit_cancel")
	WebElement chooseWalletCancelButton;

	@FindBy(id = "in.novopay.merchant:id/text_warning_main_wallet")
	WebElement chooseWalletMainErrorMsg;

	@FindBy(id = "in.novopay.merchant:id/text_warning")
	WebElement chooseWalletCashoutErrorMsg;

	@FindBy(xpath = "//table//tr[contains(@class,'table-row')][1]")
	WebElement firstTxnInList;

	@FindBy(xpath = "//h4[contains(text(),'Processing...')]")
	WebElement processingScreen;

	public CommonUtils(WebDriver wdriver) {
		super(wdriver);
		PageFactory.initElements(wdriver, this);
	}

	// Click OK on Welcome pop-up (whenever displayed)
	public void welcomePopup() {
		if (usrData.get("WELCOMEPOPUP").equalsIgnoreCase("YES")) {
			try {
				waitUntilElementIsVisible(welcomeMessage);
				System.out.println("Welcome pop-up displayed");
				waitUntilElementIsClickableAndClickTheElement(welcomeOKButton);
				System.out.println("OK button clicked");
				waitUntilElementIsInvisible("//button[contains(text(),'OK. Got it!')]");
				System.out.println("Pop-up disappeared");
			} catch (Exception e) {
				System.out.println("No pop-up displayed");
			}
		}
	}

	public void selectFeatureFromMenu1(WebElement feature, WebElement pageTitle) throws InterruptedException {
		clickElement(menu);
		scrollElementDown(scrollBar, feature);
		System.out.println(feature.getText() + " option clicked");
		waitUntilElementIsVisible(pageTitle);
		System.out.println(pageTitle.getText() + " page displayed");
		clickElement(menu);
	}

	public void selectFeatureFromMenu2(WebElement feature, WebElement pageTitle) throws InterruptedException {
		clickElement(menu);
		refreshBalance();
		scrollElementDown(scrollBar, feature);
		System.out.println(feature.getText() + " option clicked");
		waitUntilElementIsVisible(pageTitle);
		System.out.println(pageTitle.getText() + " page displayed");
		clickElement(menu);
	}

	public void selectFeatureFromMenu3(WebElement feature1, WebElement feature2, WebElement pageTitle)
			throws InterruptedException {
		clickElement(menu);
		scrollElementDown(scrollBar, feature1);
		System.out.println(feature1.getText() + " option clicked");
		scrollElementDown(scrollBar, feature2);
		System.out.println(feature2.getText() + " option clicked");
		waitUntilElementIsVisible(pageTitle);
		System.out.println(pageTitle.getText() + " page displayed");
		clickElement(menu);
	}

	// Wait for screen to complete loading
	public void waitForSpinner() {
		waitUntilElementIsInvisible("//div[contains(@class,'spinner')]/parent::div");
		System.out.println("Please wait...");
	}

	// Wait for screen to complete loading
	public void waitForLoader() {
		waitUntilElementIsInvisible("//i[contains(@class,'processing-loader')]");
		System.out.println("Please wait...");
	}

	// To refresh the wallet balance
	public void refreshBalance() throws InterruptedException {
		waitUntilElementIsClickableAndClickTheElement(refreshButton);
		waitUntilElementIsVisible(syncButton);
		waitUntilElementIsVisible(refreshButton);
		System.out.println("Balance refreshed successfully");
	}

	// Get wallet(s) balance
	@SuppressWarnings("null")
	public double getInitialBalance(String wallet) throws ClassNotFoundException {
		String initialWalletBal = replaceSymbols(retailerWalletBalance.getText());
		String initialCashoutBal = replaceSymbols(cashoutWalletBalance.getText());

		// Converting balance from String to Double and returning the same
		if (wallet.equalsIgnoreCase("retailer")) {
			return Double.parseDouble(initialWalletBal);
		} else if (wallet.equalsIgnoreCase("cashout")) {
			return Double.parseDouble(initialCashoutBal);
		}
		return (Double) null;
	}

	// Show balances in console
	public void displayInitialBalance(String wallet) throws ClassNotFoundException {
		String walletBalance = dbUtils.getWalletBalance(mobileNumFromIni(), "retailer");
		String walletBal = walletBalance.substring(0, walletBalance.length() - 4);
		String cashoutBalance = dbUtils.getWalletBalance(mobileNumFromIni(), "cashout");
		String cashoutBal = cashoutBalance.substring(0, cashoutBalance.length() - 4);

		waitUntilElementIsVisible(retailerWalletBalance);
		String initialWalletBal = replaceSymbols(retailerWalletBalance.getText());
		String initialCashoutBal = replaceSymbols(cashoutWalletBalance.getText());

		// Compare wallet balance shown in WebApp to DB
		Assert.assertEquals(walletBal, initialWalletBal);
		Assert.assertEquals(cashoutBal, initialCashoutBal);

		if (wallet.equalsIgnoreCase("retailer")) {
			System.out.println("Retailer Balance: " + initialWalletBal);
			getWalletBalanceFromIni(wallet.toLowerCase(), replaceSymbols(retailerWalletBalance.getText()));
		} else if (wallet.equalsIgnoreCase("cashout")) {
			System.out.println("Cashout Balance: " + initialCashoutBal);
			getWalletBalanceFromIni(wallet.toLowerCase(), replaceSymbols(cashoutWalletBalance.getText()));
		}
	}

	public double commissionAndTDS(String commOrTds, double minComm, double minCharge, double commPer)
			throws NumberFormatException, ClassNotFoundException {
		int i = 0;
		double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
		double commission = 0.0;

		if (amount <= 5000.0) {
			i = 0;
		} else if (amount > 5000.0 && amount <= 10000.0) {
			i = 1;
		} else if (amount > 10000.0 && amount <= 15000.0) {
			i = 2;
		} else if (amount > 15000.0 && amount <= 20000.0) {
			i = 3;
		} else if (amount > 20000.0 && amount <= 25000.0) {
			i = 4;
		}

		commission = minComm * i + Math.max(minCharge, (amount - 5000 * i) * commPer / 10000);

		double tds = roundTo2Decimals(
				(roundTo2Decimals(minComm * Double.parseDouble(dbUtils.getTDSPercentage(mobileNumFromIni())) / 10000))
						* i
						+ roundTo2Decimals((commission - (minComm) * i)
								* Double.parseDouble(dbUtils.getTDSPercentage(mobileNumFromIni())) / 10000));

		if (commOrTds.equalsIgnoreCase("comm")) {
			return commission;
		} else
			return tds;
	}

	// Choose Wallet screen
	public void chooseWalletScreen(Map<String, String> usrData) throws InterruptedException, ClassNotFoundException {
		waitUntilElementIsVisible(chooseWalletScreen);
		System.out.println("Choose a Wallet screen displayed");

		// display wallet balances in console
		getWalletBalanceFromIni("retailer",dbUtils.getWalletBalance(mobileNumFromIni(), "retailer"));
		getWalletBalanceFromIni("cashout",dbUtils.getWalletBalance(mobileNumFromIni(), "cashout"));

		Assert.assertEquals(replaceSymbols(mainWalletScreenBalance.getText() + "0000"),
				getWalletBalanceFromIni("GetRetailer", ""));
		System.out.println("Main Wallet balance: " + mainWalletScreenBalance.getText());
		if (usrData.get("ASSERTION").equalsIgnoreCase("Main < Amount")
				|| usrData.get("ASSERTION").equalsIgnoreCase("Main=0 Cashout!=0")
				|| usrData.get("ASSERTION").equalsIgnoreCase("Amount > Both Wallets")) {
			Assert.assertEquals(chooseWalletMainErrorMsg.getText(), "Balance is low!");
			System.out.println("Main wallet balance is low");
			dbUtils.updateWalletBalance(mobileNumFromIni(), "retailer", "1000000");
		}
		Assert.assertEquals(replaceSymbols(cashoutWalletScreenBalance.getText() + "0000"),
				getWalletBalanceFromIni("GetCashout", ""));
		System.out.println("Cashout Wallet balance: " + cashoutWalletScreenBalance.getText());
		if (usrData.get("ASSERTION").equalsIgnoreCase("Cashout < Amount")
				|| usrData.get("ASSERTION").equalsIgnoreCase("Amount > Both Wallets")) {
			Assert.assertEquals(chooseWalletCashoutErrorMsg.getText(), "Balance is low!");
			System.out.println("Cashout wallet balance is low");
			dbUtils.updateWalletBalance(mobileNumFromIni(), "cashout", "1000000");
		}
		if (getWalletFromIni("GetWallet", "").equalsIgnoreCase("Main")) {
			mainWalletRadioButton.click();
			System.out.println("Main wallet radio button clicked");
		} else if (getWalletFromIni("GetWallet", "").equalsIgnoreCase("Cashout")) {
			cashoutWalletRadioButton.click();
			System.out.println("Cashout wallet radio button clicked");
		}
		if (!getWalletFromIni("GetWallet", "").equalsIgnoreCase("-")) {
			waitUntilElementIsVisible(chooseWalletProceedButton);
			chooseWalletProceedButton.click();
			System.out.println("Proceed button clicked");
		} else {
			waitUntilElementIsVisible(chooseWalletCancelButton);
			chooseWalletCancelButton.click();
			System.out.println("Cancel button clicked");
		}
	}

	public void selectTxn() throws ClassNotFoundException {
		waitUntilElementIsClickableAndClickTheElement(firstTxnInList);
		System.out.println("Transaction selected");
		waitForSpinner();
	}

	public void processingScreen() {
		try {
			waitUntilElementIsVisible(processingScreen);
			System.out.println("Processing screen displayed");
		} catch (Exception e) {
			System.out.println("Processing screen skipped");
		}
	}

	public void uploadFile(WebElement image) throws InterruptedException, IOException {
		System.out.println("selecting an image");
		Thread.sleep(2000);
		waitUntilElementIsClickableAndClickTheElement(image);
		Thread.sleep(2000);
		String uploadFile = "./test-data/UploadFile.exe";
		Runtime.getRuntime().exec(uploadFile);
		Thread.sleep(500);
	}
}
