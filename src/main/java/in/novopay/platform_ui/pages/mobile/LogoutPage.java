package in.novopay.platform_ui.pages.mobile;

import java.awt.AWTException;
import java.util.Map;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import in.novopay.platform_ui.utils.BasePage;
import in.novopay.platform_ui.utils.Log;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class LogoutPage extends BasePage {
	WebDriverWait wait = new WebDriverWait(mdriver, 15);

	public LogoutPage(AndroidDriver<MobileElement> mdriver) {
		super(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver), this);
	}

	@AndroidFindBy(accessibility = "navigation_drawer_open")
	MobileElement hamburgerMenu;

	@AndroidFindBy(xpath = "//*[contains(@text,'Logout')]")
	MobileElement logout;
	
	@AndroidFindBy(xpath = "//*[contains(@text,'OK')]")
	MobileElement okLogout;

	@AndroidFindBy(id = "in.novopay.merchant:id/signInMobileNumber")
	MobileElement mobNumTextField;

	public void logout(Map<String, String> usrData) throws AWTException, InterruptedException {

		if (usrData.get("WAIT").equalsIgnoreCase("Yes")) {
			Thread.sleep(5000);
		}
		wait.until(ExpectedConditions.elementToBeClickable(hamburgerMenu));
		Log.info("Clicking hamburger menu");
		hamburgerMenu.click();
		wait.until(ExpectedConditions.elementToBeClickable(logout));
		Log.info("clicking logout");
		logout.click();
		wait.until(ExpectedConditions.elementToBeClickable(okLogout));
		Log.info("logging out");
		okLogout.click();
		wait.until(ExpectedConditions.visibilityOf(mobNumTextField));
		Log.info("Logged out successfully");
	}
}
