package in.novopay.platform_ui.pages.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
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

public class ReportsPage extends BasePage {

	WebDriverWait wait = new WebDriverWait(wdriver, 30);
	DBUtils dbUtils = new DBUtils();

	@FindBy(xpath = "//span[contains(text(),'Reports')]")
	WebElement reports;

	@FindBy(xpath = "//label[contains(text(),'Select Report to View')]")
	WebElement reportsPage;

	@FindBy(xpath = "//*[@id='reportsDropDown']//span[contains(@class,'select2-selection')]")
	WebElement reportsDropdown;

	@FindBy(xpath = "//*[@type='search']")
	WebElement dropDownSearch;

	@FindBy(xpath = "//li[contains(text(),'Money Transfer - Queued Transactions')]")
	WebElement queuedTxnsDropDown;

	@FindBy(xpath = "//li[contains(text(),'Money Transfer - Banks in Queue')]")
	WebElement banksInQueueDropDown;

	@FindBy(xpath = "//li[contains(text(),'Money Transfer - Timeout')]")
	WebElement timeoutDropDown;

	@FindBy(xpath = "//li[contains(text(),'Refund Report')]")
	WebElement refundReportDropDown;

	@FindBy(xpath = "//th[contains(text(),'TXN DATE')]")
	WebElement txnDateColumn;

	@FindBy(xpath = "//th[contains(text(),'txn date')]")
	WebElement txndateColumn;

	@FindBy(xpath = "//th[contains(text(),'bank name')]")
	WebElement bankNameColumn;

	@FindBy(xpath = "//tbody/tr[1]/td[1]")
	WebElement body;

	@FindBy(xpath = "//*[@class='fa fa-bars fa-lg text-white']")
	WebElement menu;

	@FindBy(id = "money-transfer-customer-startDate")
	WebElement startDate;

	@FindBy(id = "money-transfer-customer-endDate")
	WebElement endDate;

	@FindBy(xpath = "//button[contains(text(),'Submit')]")
	WebElement submit;

	public ReportsPage(WebDriver wdriver) {
		super(wdriver);
		PageFactory.initElements(wdriver, this);
	}

	public void reports(Map<String, String> usrData) throws ClassNotFoundException, InterruptedException {
		try {
			menu.click();
			wait.until(ExpectedConditions.elementToBeClickable(reports));
			clickInvisibleElement(reports);
			Log.info("Reports option clicked");
			wait.until(ExpectedConditions.elementToBeClickable(reportsPage));
			wait.until(ExpectedConditions.elementToBeClickable(reportsDropdown));
			menu.click();

			if (usrData.get("REPORTTYPE").equalsIgnoreCase("Money Transfer - Queued Transactions")
					&& !usrData.get("STATUS").equalsIgnoreCase("INQUEUE")) {
				Thread.sleep(90000);
				if (usrData.get("STATUS").equalsIgnoreCase("UNKNOWN")
						&& usrData.get("PARTNER").equalsIgnoreCase("RBL")) {
					dbUtils.updateRBLTxnStatus(dbUtils.selectPaymentRefCode());
				}
			}
			reportsDropdown.click();
			Log.info("Drop down clicked");
			wait.until(ExpectedConditions.elementToBeClickable(dropDownSearch));
			dropDownSearch.sendKeys(usrData.get("REPORTTYPE"));
			Log.info("Typing " + usrData.get("REPORTTYPE"));

			String reportXpath = "//li[contains(text(),'" + usrData.get("REPORTTYPE") + "')]";
			WebElement reportDropDown = wdriver.findElement(By.xpath(reportXpath));
			reportDropDown.click();
			Log.info(usrData.get("REPORTTYPE") + " drop down selected");
			if (usrData.get("REPORTTYPE").equalsIgnoreCase("Money Transfer - Timeout")
					|| (usrData.get("REPORTTYPE").equalsIgnoreCase("Refund Report")
							|| (usrData.get("REPORTTYPE").equalsIgnoreCase("Account Statement")))) {
				wait.until(ExpectedConditions.visibilityOf(startDate));
				startDate.sendKeys(currentDate());
				endDate.sendKeys(todayDate());
				submit.click();
			}
			waitForSpinner();
			if (usrData.get("REPORTTYPE").equalsIgnoreCase("Money Transfer - Banks in Queue")) {
				wait.until(ExpectedConditions.elementToBeClickable(bankNameColumn));
			} else if (usrData.get("REPORTTYPE").equalsIgnoreCase("Account Statement")) {
				wait.until(ExpectedConditions.elementToBeClickable(txndateColumn));
			} else {
				wait.until(ExpectedConditions.elementToBeClickable(txnDateColumn));
			}
			if (body.getText().equalsIgnoreCase("No Record Found")) {
				Log.info(body.getText());
			} else {
				reportsData(usrData);
			}
		} catch (Exception e) {
			wdriver.navigate().refresh();
			e.printStackTrace();
			Log.info("Test Case Failed");
			Assert.fail();
		}
	}

	public void reportsData(Map<String, String> usrData) throws ClassNotFoundException {
		List<WebElement> rowsXpath = wdriver.findElements(By.xpath("//tbody/tr"));
		int rowCount = rowsXpath.size();
		Log.info("Total no. of rows are " + rowCount);

		if (!usrData.get("STATUS").equalsIgnoreCase("ALL")) {
			rowCount = 1;
		}

		List<WebElement> columnsXpath = wdriver.findElements(By.xpath("//thead/tr/th"));
		int columnCount = columnsXpath.size();
		Log.info("Total no. of columns are " + columnCount);

		String[][] dataFromUI = new String[rowCount][columnCount];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				String dataXpath = "//tbody/tr[" + (i + 1) + "]/td[" + (j + 1) + "]";
				WebElement data = wdriver.findElement(By.xpath(dataXpath));
				dataFromUI[i][j] = data.getText();
			}
		}

		// System.out.println(Arrays.deepToString(dataFromUI));
		if (usrData.get("REPORTTYPE").equalsIgnoreCase("Money Transfer - Banks in Queue")
				|| usrData.get("REPORTTYPE").equalsIgnoreCase("Account Statement")
				|| usrData.get("STATUS").equalsIgnoreCase("Date")) {
			System.out.print("");
		} else if (usrData.get("REPORTTYPE").equalsIgnoreCase("Money Transfer - Banks in Queue")
				&& usrData.get("STATUS").equalsIgnoreCase("INQUEUE")) {
			Assert.assertEquals(dataFromUI[0][columnCount - 1], "NA");
		} else if (!usrData.get("REPORTTYPE").equalsIgnoreCase("Account Statement")) {
			Assert.assertEquals(dataFromUI[0][columnCount - 1], usrData.get("STATUS").toUpperCase());
		} else {
			Assert.assertEquals(replaceSymbols(dataFromUI[0][columnCount - 1]),
					dbUtils.closingBalance(mobileNumFromIni(usrData)));
		}

		List<String> listFromUI = new ArrayList<String>();
		for (String[] array : dataFromUI) {
			listFromUI.addAll(Arrays.asList(array));
		}
		// for (String t : listFromUI) { System.out.print(t + " | "); }

		List<String[]> list = new ArrayList<String[]>();

		if (usrData.get("REPORTTYPE").equalsIgnoreCase("Money Transfer - Queued Transactions")) {
			List<String[]> queuedTxnList = dbUtils.queuedTxnReport(mobileNumFromIni(usrData), 10);
			list = queuedTxnList;
		} else if (usrData.get("REPORTTYPE").equalsIgnoreCase("Money Transfer - Banks in Queue")) {
			List<String[]> queuedBankList = dbUtils.queuedBankReport();
			list = queuedBankList;
		} else if (usrData.get("REPORTTYPE").equalsIgnoreCase("Money Transfer - Timeout")) {
			List<String[]> timeoutList = new ArrayList<String[]>();
			if (usrData.get("PARTNER").equalsIgnoreCase("RBL")) {
				timeoutList = dbUtils.timeoutReport(mobileNumFromIni(usrData), "SUCCESS");
			} else if (usrData.get("PARTNER").equalsIgnoreCase("AXIS")) {
				timeoutList = dbUtils.timeoutReport(mobileNumFromIni(usrData), "UNKNOWN");
			}
			list = timeoutList;
		} else if (usrData.get("REPORTTYPE").equalsIgnoreCase("Refund Report")) {
			List<String[]> refundList = dbUtils.refundReport(mobileNumFromIni(usrData));
			list = refundList;
		} else if (usrData.get("REPORTTYPE").equalsIgnoreCase("Account Statement")
				&& usrData.get("STATUS").equalsIgnoreCase("MT")) {
			List<String[]> accountStatementMT = dbUtils.accountStatementMT(mobileNumFromIni(usrData));
			list = accountStatementMT;
		} else if (usrData.get("REPORTTYPE").equalsIgnoreCase("Account Statement")
				&& usrData.get("STATUS").equalsIgnoreCase("BEN_VAL")) {
			List<String[]> accountStatementBV = dbUtils.accountStatementBV(mobileNumFromIni(usrData));
			list = accountStatementBV;
		} else if (usrData.get("REPORTTYPE").equalsIgnoreCase("Account Statement")
				&& usrData.get("STATUS").equalsIgnoreCase("AEPS")) {
			List<String[]> accountStatementAEPS = dbUtils.accountStatementAEPS(mobileNumFromIni(usrData));
			list = accountStatementAEPS;
		} else if (usrData.get("REPORTTYPE").equalsIgnoreCase("Account Statement")
				&& usrData.get("STATUS").equalsIgnoreCase("CMS")) {
			List<String[]> accountStatementCMS = dbUtils.accountStatementCMS(mobileNumFromIni(usrData),
					usrData.get("STATUS"));
			list = accountStatementCMS;
		}
		List<String> listFromDB = new ArrayList<String>();
		for (String[] data : list) {
			for (String s : data) {
				// System.out.print(s + " | ");
				listFromDB.add(s);
			}
		}
		// System.out.println();

		HashMap<String, String> map = new HashMap<String, String>();
		Iterator<String> iUI = listFromUI.iterator();
		Iterator<String> iDB = listFromDB.iterator();
		while (iUI.hasNext() && iDB.hasNext()) {
			map.put(iUI.next(), iDB.next());
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			Assert.assertEquals(entry.getKey(), entry.getValue());
		}
		for (int i = 0; i < listFromUI.size(); i++) {
			if (i != 0 && i % columnCount == 0) {
				System.out.println();
			}
			System.out.print(listFromUI.get(i) + " | ");
		}
		System.out.println();
	}

	public String mobileNumFromIni(Map<String, String> usrData) {
		if (usrData.get("PARTNER").equalsIgnoreCase("AXIS")) {
			return getLoginMobileFromIni("AXISRetailerMobNum");
		} else {
			return getLoginMobileFromIni("RBLRetailerMobNum");
		}
	}

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
}
