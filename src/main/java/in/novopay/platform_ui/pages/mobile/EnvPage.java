package in.novopay.platform_ui.pages.mobile;

import java.util.Map;

import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.JavaUtils;

public class EnvPage extends JavaUtils {

	DBUtils dbUtils = new DBUtils();
	public void env(Map<String, String> usrData) throws ClassNotFoundException {

		changeEnvURLfromIni(usrData.get("ENV"));
		
		if (usrData.get("UPDATERETAILERBALANCE").equals("YES")) {
			if (usrData.get("PARTNER").equalsIgnoreCase("RBL")) {
				dbUtils.updateWalletBalance(getLoginMobileFromIni("RBLRetailerMobNum"),"retailer",usrData.get("AMOUNT"));
			} else if (usrData.get("PARTNER").equalsIgnoreCase("AXIS")) {
				dbUtils.updateWalletBalance(getLoginMobileFromIni("AXISRetailerMobNum"),"retailer",usrData.get("AMOUNT"));
			} else {
				dbUtils.updateWalletBalance(getLoginMobileFromIni("RBLRetailerMobNum"),"retailer",usrData.get("AMOUNT"));
				dbUtils.updateWalletBalance(getLoginMobileFromIni("AXISRetailerMobNum"),"retailer",usrData.get("AMOUNT"));
			}
		}
		if (usrData.get("UPDATECASHOUTBALANCE").equals("YES")) {
			if (usrData.get("PARTNER").equalsIgnoreCase("RBL")) {
				dbUtils.updateWalletBalance(getLoginMobileFromIni("RBLRetailerMobNum"),"cashout",usrData.get("AMOUNT"));
			} else if (usrData.get("PARTNER").equalsIgnoreCase("AXIS")) {
				dbUtils.updateWalletBalance(getLoginMobileFromIni("AXISRetailerMobNum"),"cashout",usrData.get("AMOUNT"));
			} else {
				dbUtils.updateWalletBalance(getLoginMobileFromIni("RBLRetailerMobNum"),"cashout",usrData.get("AMOUNT"));
				dbUtils.updateWalletBalance(getLoginMobileFromIni("AXISRetailerMobNum"),"cashout",usrData.get("AMOUNT"));
			}
		}
		if (usrData.get("UPDATEMERCHANTBALANCE").equals("YES")) {
			if (usrData.get("PARTNER").equalsIgnoreCase("RBL")) {
				dbUtils.updateWalletBalance(getLoginMobileFromIni("RBLRetailerMobNum"),"merchant",usrData.get("AMOUNT"));
			} else if (usrData.get("PARTNER").equalsIgnoreCase("AXIS")) {
				dbUtils.updateWalletBalance(getLoginMobileFromIni("AXISRetailerMobNum"),"merchant",usrData.get("AMOUNT"));
			} else {
				dbUtils.updateWalletBalance(getLoginMobileFromIni("RBLRetailerMobNum"),"merchant",usrData.get("AMOUNT"));
				dbUtils.updateWalletBalance(getLoginMobileFromIni("AXISRetailerMobNum"),"merchant",usrData.get("AMOUNT"));
			}
		}
	}
}