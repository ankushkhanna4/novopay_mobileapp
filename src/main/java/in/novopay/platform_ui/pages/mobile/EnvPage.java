package in.novopay.platform_ui.pages.mobile;

import java.util.Map;

import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.JavaUtils;

public class EnvPage extends JavaUtils {

	DBUtils dbUtils = new DBUtils();

	public void env(Map<String, String> usrData) throws ClassNotFoundException {

		changeEnvURLfromIni(usrData.get("ENV"));

		if (usrData.get("UPDATERETAILERBALANCE").equals("YES")) {
			dbUtils.updateWalletBalance(getLoginMobileFromIni("RetailerMobNum"), "retailer",
					usrData.get("RETAILERAMOUNT"));
		}
		if (usrData.get("UPDATECASHOUTBALANCE").equals("YES")) {
			dbUtils.updateWalletBalance(getLoginMobileFromIni("RetailerMobNum"), "cashout",
					usrData.get("CASHOUTAMOUNT"));
		}
	}
}