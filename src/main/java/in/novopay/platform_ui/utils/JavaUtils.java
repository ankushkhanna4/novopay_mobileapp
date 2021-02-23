package in.novopay.platform_ui.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Reporter;
import org.testng.SkipException;

@SuppressWarnings("rawtypes")
public class JavaUtils extends LoadableComponent {
	String stri;
	String fileName;
	static String failureReason;
	public static HashMap<String, String> configProperties = new HashMap<String, String>();
	String assertionMessage;
	Properties velocityProps;
	public static String buildNo;
	public static String session;
	public static HashMap<byte[], String> imageByte = new HashMap<byte[], String>();
	public static Map<String, String> testExecutionTime = new HashMap<String, String>();

	/* Read the properties file and returns a 'Value' for a particular 'Key' */
	public HashMap<String, String> readConfigProperties() {
		String sectionName = null;
		Set<Entry<String, String>> dataSet;
		Ini ini;
		try {
			ini = new Ini(new File("./config.ini"));

			Ini.Section section = ini.get("Common");
			dataSet = section.entrySet();

			sectionName = section.get("configName");
			section = ini.get(sectionName);

			dataSet.addAll(section.entrySet());
			for (Map.Entry<String, String> set : dataSet) {
				configProperties.put(set.getKey().toString(), set.getValue().toString());
			}
			return configProperties;
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getSessionFromIni(String userName) {
		Ini ini;
		try {
			ini = new Ini(new File("./testData.ini"));
			return ini.get("Common", userName + "session");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void changeEnvURLfromIni(String env) {
		Ini ini;
		try {
			ini = new Ini(new File("./config.ini"));
			if (env.equalsIgnoreCase("QA1")) {
				ini.put("Common", "env", "qa1");
				ini.put("Common", "webAppUrl", "https://qa1-retailer.novopay.in/");
				ini.put("Common", "finOpsUrl", "https://qa1-finance-portal.novopay.in");
				ini.put("Common", "dbUrl", "jdbc:mysql://192.168.150.7:3306/");
				ini.put("Common", "dbUserName", "akhanna");
				ini.put("Common", "dbPassword", "akhanna123$");
				ini.put("Common", "mongoDbUrl", "192.168.150.7:37017/");
				ini.put("Common", "mongoDbUserNameCms", "nodecms");
				ini.put("Common", "mongoDbPasswordCms", "Novopay#987");
				ini.put("Common", "mongoDbUserNameBillpay", "akhanna");
				ini.put("Common", "mongoDbPasswordBillpay", "akhanna123$");
				ini.put("Common", "server.host.name", "qa1.novopay.in");
				ini.put("Common", "server.host.ip", "192.168.150.7");
				ini.put("Common", "server.host.port", "22");
				ini.put("Common", "server.username", "akhanna");
				ini.put("Common", "server.password", "qaonenovopay");
				configProperties.put("env", "qa1");
				configProperties.put("webAppUrl", "https://qa1-retailer.novopay.in/");
				configProperties.put("finOpsUrl", "https://qa1-finance-portal.novopay.in");
				configProperties.put("dbUrl", "jdbc:mysql://192.168.150.7:3306/");
				configProperties.put("dbUserName", "akhanna");
				configProperties.put("dbPassword", "akhanna123$");
				configProperties.put("mongoDbUrl", "192.168.150.7:37017/");
				configProperties.put("mongoDbUserNameCms", "nodecms");
				configProperties.put("mongoDbPasswordCms", "Novopay#987");
				configProperties.put("mongoDbUserNameBillpay", "akhanna");
				configProperties.put("mongoDbPasswordBillpay", "akhanna123$");
				configProperties.put("server.host.name", "qa1.novopay.in");
				configProperties.put("server.host.ip", "192.168.150.7");
				configProperties.put("server.host.port", "22");
				configProperties.put("server.username", "akhanna");
				configProperties.put("server.password", "qaonenovopay");
			} else if (env.equalsIgnoreCase("QA2")) {
				ini.put("Common", "env", "qa2");
				ini.put("Common", "webAppUrl", "https://qa2-retailer.novopay.in/");
				ini.put("Common", "finOpsUrl", "https://qa2-finance-portal.novopay.in");
				ini.put("Common", "dbUrl", "jdbc:mysql://192.168.150.24:3306/");
				ini.put("Common", "dbUserName", "akhanna");
				ini.put("Common", "dbPassword", "akhanna123$");
				ini.put("Common", "mongoDbUrl", "192.168.150.24:37017/");
				ini.put("Common", "mongoDbUserNameCms", "nodecms");
				ini.put("Common", "mongoDbPasswordCms", "Novopay#987");
				ini.put("Common", "mongoDbUserNameBillpay", "akhanna");
				ini.put("Common", "mongoDbPasswordBillpay", "akhanna123$");
				ini.put("Common", "server.host.name", "qa2.novopay.in");
				ini.put("Common", "server.host.ip", "192.168.150.24");
				ini.put("Common", "server.host.port", "22");
				ini.put("Common", "server.username", "akhanna");
				ini.put("Common", "server.password", "qatwonovopay");
				configProperties.put("env", "qa2");
				configProperties.put("webAppUrl", "https://qa2-retailer.novopay.in/");
				configProperties.put("finOpsUrl", "https://qa2-finance-portal.novopay.in");
				configProperties.put("dbUrl", "jdbc:mysql://192.168.150.24:3306/");
				configProperties.put("dbUserName", "akhanna");
				configProperties.put("dbPassword", "akhanna123$");
				configProperties.put("mongoDbUrl", "192.168.150.24:37017/");
				configProperties.put("mongoDbUserNameCms", "nodecms");
				configProperties.put("mongoDbPasswordCms", "Novopay#987");
				configProperties.put("mongoDbUserNameBillpay", "akhanna");
				configProperties.put("mongoDbPasswordBillpay", "akhanna123$");
				configProperties.put("server.host.name", "qa2.novopay.in");
				configProperties.put("server.host.ip", "192.168.150.24");
				configProperties.put("server.host.port", "22");
				configProperties.put("server.username", "akhanna");
				configProperties.put("server.password", "qatwonovopay");
			} else if (env.equalsIgnoreCase("DEV1")) {
				ini.put("Common", "env", "dev1");
				ini.put("Common", "webAppUrl", "https://dev1-retailer.novopay.in/");
				ini.put("Common", "finOpsUrl", "https://dev1-finance-portal.novopay.in");
				ini.put("Common", "dbUrl", "jdbc:mysql://192.168.150.27:3306/");
				ini.put("Common", "dbUserName", "akhanna");
				ini.put("Common", "dbPassword", "AnkushThe@Grete123");
				ini.put("Common", "mongoDbUrl", "192.168.150.27:37017/");
				ini.put("Common", "mongoDbUserNameCms", "nodecms");
				ini.put("Common", "mongoDbPasswordCms", "Novopay#987");
				ini.put("Common", "mongoDbUserNameBillpay", "novopaybillpay");
				ini.put("Common", "mongoDbPasswordBillpay", "novopaybillpay123$");
				ini.put("Common", "server.host.name", "dev1.novopay.in");
				ini.put("Common", "server.host.ip", "192.168.150.27");
				ini.put("Common", "server.host.port", "22");
				ini.put("Common", "server.username", "akhanna");
				ini.put("Common", "server.password", "devonenovopay");
				configProperties.put("env", "dev1");
				configProperties.put("webAppUrl", "https://dev1-retailer.novopay.in/");
				configProperties.put("finOpsUrl", "https://dev1-finance-portal.novopay.in");
				configProperties.put("dbUrl", "jdbc:mysql://192.168.150.27:3306/");
				configProperties.put("dbUserName", "akhanna");
				configProperties.put("dbPassword", "AnkushThe@Grete123");
				configProperties.put("mongoDbUrl", "192.168.150.27:37017/");
				configProperties.put("mongoDbUserNameCms", "nodecms");
				configProperties.put("mongoDbPasswordCms", "Novopay#987");
				configProperties.put("mongoDbUserNameBillpay", "novopaybillpay");
				configProperties.put("mongoDbPasswordBillpay", "novopaybillpay123$");
				configProperties.put("server.host.name", "dev1.novopay.in");
				configProperties.put("server.host.ip", "192.168.150.27");
				configProperties.put("server.host.port", "22");
				configProperties.put("server.username", "akhanna");
				configProperties.put("server.password", "devonenovopay");
			} else if (env.equalsIgnoreCase("PP")) {
				ini.put("Common", "env", "pre-prod");
				ini.put("Common", "webAppUrl", "https://preretailer.novopay.in/");
				ini.put("Common", "finOpsUrl", "https://preprod-finance-portal.novopay.in");
				ini.put("Common", "dbUrl", "jdbc:mysql://192.168.150.2:3306/");
				ini.put("Common", "dbUserName", "akhanna");
				ini.put("Common", "dbPassword", "akhanna123$");
				ini.put("Common", "mongoDbUrl", "192.168.150.2:37017/");
				ini.put("Common", "mongoDbUserNameCms", "akhanna");
				ini.put("Common", "mongoDbPasswordCms", "akhanna123");
				ini.put("Common", "mongoDbUserNameBillpay", "nodebillpay");
				ini.put("Common", "mongoDbPasswordBillpay", "Zv~B7C_DA");
				ini.put("Common", "server.host.name", "pre-prod.novopay.in");
				ini.put("Common", "server.host.ip", "192.168.150.2");
				ini.put("Common", "server.host.port", "1024");
				ini.put("Common", "server.username", "akhanna");
				ini.put("Common", "server.password", "akhanna123$");
				configProperties.put("env", "pre-prod");
				configProperties.put("webAppUrl", "https://preretailer.novopay.in/");
				configProperties.put("finOpsUrl", "https://preprod-finance-portal.novopay.in");
				configProperties.put("dbUrl", "jdbc:mysql://192.168.150.2:3306/");
				configProperties.put("dbUserName", "akhanna");
				configProperties.put("dbPassword", "akhanna123$");
				configProperties.put("mongoDbUrl", "192.168.150.2:37017/");
				configProperties.put("mongoDbUserNameCms", "akhanna");
				configProperties.put("mongoDbPasswordCms", "akhanna123");
				configProperties.put("mongoDbUserNameBillpay", "nodebillpay");
				configProperties.put("mongoDbPasswordBillpay", "Zv~B7C_DA");
				configProperties.put("server.host.name", "pre-prod.novopay.in");
				configProperties.put("server.host.ip", "192.168.150.2");
				configProperties.put("server.host.port", "1024");
				configProperties.put("server.username", "akhanna");
				configProperties.put("server.password", "akhanna123$");
			} else if (env.equalsIgnoreCase("QA1AWS")) {
				ini.put("Common", "env", "aws-qa1");
				ini.put("Common", "webAppUrl", "https://aws-qa1-retailer.novopay.in/");
				ini.put("Common", "finOpsUrl", "https://aws-qa1-finance-portal.novopay.in");
				ini.put("Common", "dbUrl", "jdbc:mysql://localhost:3306/");
				ini.put("Common", "dbUserName", "akhanna");
				ini.put("Common", "dbPassword", "akhanna123$");
				ini.put("Common", "mongoDbUrl", "192.168.150.7:37017/");
				ini.put("Common", "mongoDbUserNameCms", "nodecms");
				ini.put("Common", "mongoDbPasswordCms", "Novopay#987");
				ini.put("Common", "mongoDbUserNameBillpay", "akhanna");
				ini.put("Common", "mongoDbPasswordBillpay", "akhanna123$");
				ini.put("Common", "server.host.name", "qa1.novopay.in");
				ini.put("Common", "server.host.ip", "192.168.150.7");
				ini.put("Common", "server.host.port", "22");
				ini.put("Common", "server.username", "akhanna");
				ini.put("Common", "server.password", "qazxsw123");
				configProperties.put("env", "aws-qa1");
				configProperties.put("webAppUrl", "https://aws-qa1-retailer.novopay.in/");
				configProperties.put("finOpsUrl", "https://aws-qa1-finance-portal.novopay.in");
				configProperties.put("dbUrl", "jdbc:mysql://localhost:3306/");
				configProperties.put("dbUserName", "akhanna");
				configProperties.put("dbPassword", "akhanna123$");
				configProperties.put("mongoDbUrl", "192.168.150.7:37017/");
				configProperties.put("mongoDbUserNameCms", "nodecms");
				configProperties.put("mongoDbPasswordCms", "Novopay#987");
				configProperties.put("mongoDbUserNameBillpay", "akhanna");
				configProperties.put("mongoDbPasswordBillpay", "akhanna123$");
				configProperties.put("server.host.name", "qa1.novopay.in");
				configProperties.put("server.host.ip", "192.168.150.7");
				configProperties.put("server.host.port", "22");
				configProperties.put("server.username", "akhanna");
				configProperties.put("server.password", "qazxsw123");
			}
			ini.store();
			BasePage.wdriver = null;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getEnvURLfromIni() {
		Ini ini;
		try {
			ini = new Ini(new File("./config.ini"));
			return ini.get("Common", "env");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Get mobile number from Ini file
	public String mobileNumFromIni() {
		return getLoginMobileFromIni("RetailerMobNum");
	}

	public String getAEPSMobNum(String mobileNumber) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (mobileNumber.equalsIgnoreCase("Random")) {
				ini.put("CustomerData", "AEPSMobNum", generateRandomMobileNumber());
				ini.store();
				return ini.get("CustomerData", "AEPSMobNum");
			} else if (mobileNumber.equalsIgnoreCase("GetAEPSMobNum")) {
				return ini.get("CustomerData", "AEPSMobNum");
			} else {
				ini.put("CustomerData", "AEPSMobNum", mobileNumber);
				ini.store();
				return ini.get("CustomerData", "AEPSMobNum");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAadhaarFromIni(String aadhaarNum) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (aadhaarNum.equalsIgnoreCase("Random")) {
				ini.put("CustomerData", "AadhaarNum", generateRandomMobileNumber());
				ini.store();
				return ini.get("CustomerData", "AadhaarNum");
			} else if (aadhaarNum.equalsIgnoreCase("GetAadhaarNum")) {
				return ini.get("CustomerData", "AadhaarNum");
			} else {
				ini.put("CustomerData", "AadhaarNum", aadhaarNum);
				ini.store();
				return ini.get("CustomerData", "AadhaarNum");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getMobileToDeactivateFromIni(String mobileNumber) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			ini.put("MobileNumber", "RetailerMobNum", ini.get("MobileNumber", mobileNumber));
			ini.store();
			return ini.get("MobileNumber", "RetailerMobNum");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCustomerDetailsFromIni(String data) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (data.equalsIgnoreCase("NewNum")) {
				if (ini.get("CustomerData", "CustomerMobNum").equalsIgnoreCase("Random")) {
					ini.put("CustomerData", "GetCustomerMobNum", generateRandomMobileNumber());
				} else {
					ini.put("CustomerData", "GetCustomerMobNum", ini.get("CustomerData", "CustomerMobNum"));
				}
				ini.store();
				return ini.get("CustomerData", "GetCustomerMobNum");
			} else if (data.equalsIgnoreCase("NewName")) {
				if (ini.get("CustomerData", "CustomerName").equalsIgnoreCase("Random")) {
					ini.put("CustomerData", "GetCustomerName", generateRandomCustomerName());
				} else {
					ini.put("CustomerData", "GetCustomerName", ini.get("CustomerData", "CustomerName"));
				}
				ini.store();
				return ini.get("CustomerData", "GetCustomerName");
			} else if (data.equalsIgnoreCase("ExistingNum")) {
				return ini.get("CustomerData", "GetCustomerMobNum");
			} else if (data.equalsIgnoreCase("GetBeneNum")) {
				return ini.get("BeneData", "BeneNum");
			} else if (data.equalsIgnoreCase("Skip") || data.equalsIgnoreCase("ExistingName")) {
				return ini.get("CustomerData", "GetCustomerName");
			} else {
				System.out.println("Skipping money transfer flow");
				return "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getLoginMobileFromIni(String mobileNum) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (mobileNum.equalsIgnoreCase("RetailerMobNum")) {
				ini.put("MobileNumber", "GetRetailerMobNum", ini.get("MobileNumber", mobileNum));
				ini.store();
			}
			return ini.get("MobileNumber", "GetRetailerMobNum");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAccountNumberFromIni(String num) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (num.equalsIgnoreCase("Random")) {
				ini.put("BeneData", "AccountNum", generateRandomNumber(99999999, 11111111));
				ini.store();
				return ini.get("BeneData", "AccountNum");
			} else if (num.equalsIgnoreCase("GetNum")) {
				return ini.get("BeneData", "AccountNum");
			} else {
				ini.put("BeneData", "AccountNum", num);
				ini.store();
				return ini.get("BeneData", "AccountNum");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getBeneNumberFromIni(String num) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (num.equalsIgnoreCase("Random")) {
				ini.put("BeneData", "BeneNum", generateRandomMobileNumber());
				ini.store();
				return ini.get("BeneData", "BeneNum");
			} else if (num.equalsIgnoreCase("GetNum")) {
				return ini.get("BeneData", "BeneNum");
			} else {
				ini.put("BeneData", "BeneNum", num);
				ini.store();
				return ini.get("BeneData", "BeneNum");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getBeneNameFromIni(String name) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (name.equalsIgnoreCase("Random")) {
				ini.put("BeneData", "BeneName", generateRandomBeneName());
				ini.store();
				return ini.get("BeneData", "BeneName");
			} else if (name.equalsIgnoreCase("GetBeneName")) {
				return ini.get("BeneData", "BeneName");
			} else {
				ini.put("BeneData", "BeneName", name);
				ini.store();
				return ini.get("BeneData", "BeneName");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getBeneNameFromBank(String key, String beneName) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (key.equalsIgnoreCase("StoreBeneName")) {
				ini.put("BeneData", "BeneNameFromBank", beneName);
				ini.store();
				return ini.get("BeneData", "BeneNameFromBank");
			} else if (key.equalsIgnoreCase("GetBeneName")) {
				return ini.get("BeneData", "BeneNameFromBank");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getBankNameFromIni(String name) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (name.equalsIgnoreCase("GetBankName")) {
				return ini.get("BeneData", "BankName");
			} else {
				ini.put("BeneData", "BankName", name);
				ini.store();
				return ini.get("BeneData", "BankName");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getEmailIdFromIni(String name) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (name.equalsIgnoreCase("Random")) {
				ini.put("SelfLoadRequestData", "EmailId", generateRandomEmailId());
				ini.store();
				return ini.get("SelfLoadRequestData", "EmailId");
			} else if (name.equalsIgnoreCase("GetEmailId")) {
				return ini.get("SelfLoadRequestData", "EmailId");
			} else {
				ini.put("SelfLoadRequestData", "EmailId", name);
				ini.store();
				return ini.get("SelfLoadRequestData", "EmailId");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTermNumberFromIni(String name) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (name.equalsIgnoreCase("Random")) {
				ini.put("SelfLoadRequestData", "Term", generateRandomTermNumber());
				ini.store();
				return ini.get("SelfLoadRequestData", "Term");
			} else if (name.equalsIgnoreCase("GetTerm")) {
				return ini.get("SelfLoadRequestData", "Term");
			} else {
				ini.put("SelfLoadRequestData", "Term", name);
				ini.store();
				return ini.get("SelfLoadRequestData", "Term");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTxnNumberFromIni(String name) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (name.equalsIgnoreCase("Random")) {
				ini.put("SelfLoadRequestData", "Txn", generateRandomTxnNumber());
				ini.store();
				return ini.get("SelfLoadRequestData", "Txn");
			} else if (name.equalsIgnoreCase("GetTxn")) {
				return ini.get("SelfLoadRequestData", "Txn");
			} else {
				ini.put("SelfLoadRequestData", "Txn", name);
				ini.store();
				return ini.get("SelfLoadRequestData", "Txn");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPartner(String name) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (name.equalsIgnoreCase("GetPartner")) {
				return ini.get("RetailerData", "Partner");
			} else {
				ini.put("RetailerData", "Partner", name);
				ini.store();
				return ini.get("RetailerData", "Partner");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getWalletBalanceFromIni(String wallet, String balance) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (wallet.equalsIgnoreCase("retailer")) {
				ini.put("RetailerData", "RetailerWalletBalance", balance);
				ini.store();
			} else if (wallet.equalsIgnoreCase("cashout")) {
				ini.put("RetailerData", "CashoutWalletBalance", balance);
				ini.store();
			} else if (wallet.equalsIgnoreCase("GetRetailer")) {
				return ini.get("RetailerData", "RetailerWalletBalance");
			} else if (wallet.equalsIgnoreCase("GetCashout")) {
				return ini.get("RetailerData", "CashoutWalletBalance");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String txnDetailsFromIni(String key, String value) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (key.equalsIgnoreCase("StoreTxfAmount")) {
				ini.put("TxnDetails", "TxfAmount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetTxfAmount")) {
				return ini.get("TxnDetails", "TxfAmount");
			} else if (key.equalsIgnoreCase("StoreFailAmount")) {
				ini.put("TxnDetails", "FailAmount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetFailAmount")) {
				return ini.get("TxnDetails", "FailAmount");
			} else if (key.equalsIgnoreCase("StoreCharges")) {
				ini.put("TxnDetails", "Charges", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetCharges")) {
				return ini.get("TxnDetails", "Charges");
			} else if (key.equalsIgnoreCase("StoreComm")) {
				ini.put("TxnDetails", "Comm", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetComm")) {
				return ini.get("TxnDetails", "Comm");
			} else if (key.equalsIgnoreCase("StoreTds")) {
				ini.put("TxnDetails", "Tds", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetTds")) {
				return ini.get("TxnDetails", "Tds");
			} else if (key.equalsIgnoreCase("StoreTxnRefNo")) {
				ini.put("TxnDetails", "TxnRefNo", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetTxnRefNo")) {
				return ini.get("TxnDetails", "TxnRefNo");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String cmsDetailsFromIni(String key, String value) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (key.equalsIgnoreCase("StoreCfBatchId")) {
				ini.put("CmsData", "CfBatchId", value);
				ini.store();
			} else if (key.equalsIgnoreCase("CfBatchId")) {
				return ini.get("CmsData", "CfBatchId");
			} else if (key.equalsIgnoreCase("StoreCfAmount")) {
				ini.put("CmsData", "CfAmount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("CfAmount")) {
				return ini.get("CmsData", "CfAmount");
			} else if (key.equalsIgnoreCase("StoreSwiggyAmount")) {
				ini.put("CmsData", "SwiggyAmount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("SwiggyAmount")) {
				return ini.get("CmsData", "SwiggyAmount");
			} else if (key.equalsIgnoreCase("StoreSwiggyDueAmount")) {
				ini.put("CmsData", "SwiggyDueAmount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("SwiggyDueAmount")) {
				return ini.get("CmsData", "SwiggyDueAmount");
			} else if (key.equalsIgnoreCase("StoreFtAmount")) {
				ini.put("CmsData", "FtAmount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("FtAmount")) {
				return ini.get("CmsData", "FtAmount");
			} else if (key.equalsIgnoreCase("StoreFtDueAmount")) {
				ini.put("CmsData", "FtDueAmount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("FtDueAmount")) {
				return ini.get("CmsData", "FtDueAmount");
			} else if (key.equalsIgnoreCase("StoreFinoCMSAmount")) {
				ini.put("CmsData", "FinoCMSAmount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("FinoCMSAmount")) {
				return ini.get("CmsData", "FinoCMSAmount");
			} else if (key.equalsIgnoreCase("StoreFinoCMSMobNum")) {
				ini.put("CmsData", "FinoCMSMobNum", value);
				ini.store();
			} else if (key.equalsIgnoreCase("FinoCMSMobNum")) {
				return ini.get("CmsData", "FinoCMSMobNum");
			} else if (key.equalsIgnoreCase("StoreCmsBiller")) {
				ini.put("CmsData", "Biller", value);
				ini.store();
			} else if (key.equalsIgnoreCase("CmsBiller")) {
				return ini.get("CmsData", "Biller");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String rechargeDataFromIni(String key, String value) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (key.equalsIgnoreCase("StoreMobNum")) {
				ini.put("RechargeData", "MobNum", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetMobNum")) {
				return ini.get("RechargeData", "MobNum");
			} else if (key.equalsIgnoreCase("StoreCustomerId")) {
				ini.put("RechargeData", "CustomerId", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetCustomerId")) {
				return ini.get("RechargeData", "CustomerId");
			} else if (key.equalsIgnoreCase("StoreDatacardNum")) {
				ini.put("RechargeData", "DatacardNum", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetDatacardNum")) {
				return ini.get("RechargeData", "DatacardNum");
			} else if (key.equalsIgnoreCase("StoreAmount")) {
				ini.put("RechargeData", "Amount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetAmount")) {
				return ini.get("RechargeData", "Amount");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String loadMoneyNowDataFromIni(String key, String value) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (key.equalsIgnoreCase("StoreAmount")) {
				ini.put("LoadMoneyNowData", "Amount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetAmount")) {
				return ini.get("LoadMoneyNowData", "Amount");
			} else if (key.equalsIgnoreCase("StoreMethod")) {
				ini.put("LoadMoneyNowData", "Method", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetMethod")) {
				return ini.get("LoadMoneyNowData", "Method");
			} else if (key.equalsIgnoreCase("StoreOrderId")) {
				ini.put("LoadMoneyNowData", "OrderId", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetOrderId")) {
				return ini.get("LoadMoneyNowData", "OrderId");
			} else if (key.equalsIgnoreCase("StoreLoadAmount")) {
				ini.put("LoadMoneyNowData", "LoadAmount", value);
				ini.store();
			} else if (key.equalsIgnoreCase("GetLoadAmount")) {
				return ini.get("LoadMoneyNowData", "LoadAmount");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAuthfromIni(String type) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			return ini.get("Authentication", type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getWalletFromIni(String type, String wallet) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (type.equalsIgnoreCase("StoreWallet")) {
				ini.put("Wallet", "Wallet", wallet);
				ini.store();
			} else if (type.equalsIgnoreCase("GetWallet")) {
				return ini.get("Wallet", "Wallet");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getValueFromIni(String key) {
		Ini ini;
		try {
			ini = new Ini(new File("./config.ini"));
			return ini.get("Common", key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String setValueToIni(String user, String value) {
		Ini ini;
		try {
			ini = new Ini(new File("./config.ini"));
			ini.put("Common", user + "Id", value);
			ini.store();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addConfigToIni(String key, String value) {
		try {
			Ini ini = new Ini(new File("./config.ini"));
			ini.put("Common", key, value);
			ini.store();
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addTestDataIni(String key, String value) {
		try {
			File file = new File("./testData.ini");
			if (!(file.exists())) {
				file.createNewFile();
			}
			Ini ini = new Ini(file);
			ini.put("Common", key, value);
			ini.store();
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addMobConfigToIni(String sheetName, String sectionName)

			throws EncryptedDocumentException, InvalidFormatException, IOException {
		Ini ini = new Ini(new File("./data.ini"));
		FileInputStream file = new FileInputStream(configProperties.get("testData"));
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetName);
		Iterator<Row> it = sheet.rowIterator();
		while (it.hasNext()) {
			Row record = it.next();
			if (record.getCell(2).toString().equalsIgnoreCase("RANDOM")) {
				ini.put(sectionName, record.getCell(0).toString(), generateRandomNo(10));

			} else {
				record.getCell(2).toString();
				ini.put(sectionName, record.getCell(0).toString(), record.getCell(2).toString());

			}
		}
		ini.store();
	}

	public String checkExecutionStatus(String workbook, String sheetName, String testCaseID) {

		HashMap<String, String> testRow = readExcelData(workbook, sheetName, testCaseID);

		/*
		 * Checks the execution status of the current testCaseID which is set in the
		 * Excel - TestData sheet if marked 'Yes' testCase would execute , else testCase
		 * would skip
		 */
		if (testRow.get("Execution Status").toLowerCase().equalsIgnoreCase("no")) {
			throw new SkipException(
					"Skipping the test flow with ID " + testCaseID + " as it marked 'NO' in the Execution Excel Sheet");
		}

		Reporter.log("\nExecuting the " + testRow.get("Test Description") + " : " + testCaseID, true);
		return testCaseID;
	}

	/* Returns the values in column1 of the TestData in an ArrayList */
	public ArrayList<String> returnRowsUniqueValueBasedOnClassName(String sheetName, Class<?> className) {

		String[] clsParts = className.getName().split("\\.");
		String clsName = clsParts[(clsParts.length) - 1];
		// String[] allValues = null;
		ArrayList<String> allValues = new ArrayList<String>();
		try {
			FileInputStream file = new FileInputStream("./test-data/SLIUITestData.xlsx");
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet(sheetName);
			Iterator<Row> it = sheet.rowIterator();

			while (it.hasNext()) {

				Row record = it.next();
				String cellValue = record.getCell(1).toString() + "".trim();
				if (cellValue.equalsIgnoreCase(clsName)) {
					allValues.add(record.getCell(0).toString() + "".trim());
				}
			}
			return allValues;
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new NullPointerException("Failed due to NullPointerException" + e);
		} catch (EncryptedDocumentException e) {
			throw new EncryptedDocumentException("Failed due to EncryptedDocumentException" + e);
		} catch (IOException e) {
			throw new NullPointerException("Failed due to IOException" + e);
		}
	}

	/*
	 * return List of HashMap with data read from excel sheet
	 */
	public List<HashMap<String, String>> returnRowsUniqueValueBasedOnTestTypeList(String workbookName, String sheetName,
			String testType) {

		HashMap<String, String> dataMap = new HashMap<String, String>();
		List<HashMap<String, String>> allValues = new ArrayList<HashMap<String, String>>();
		try {
			FileInputStream file = new FileInputStream(configProperties.get(workbookName));
			if (file != null) {
				System.out.println();
			}
			System.out.println(configProperties.get(workbookName));
			String key, value;
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet(sheetName);
			boolean flag = false;
			Iterator<Row> it = sheet.rowIterator();
			int i = 0;
			Row headers = it.next();
			while (it.hasNext()) {

				Row record = it.next();

				if ((record.getCell(3).toString().trim() + "").equalsIgnoreCase("yes")) {
					if (testType.equalsIgnoreCase("no-check")) {
						flag = true;
					} else if ((record.getCell(1).toString().trim() + "").equalsIgnoreCase(testType)) {
						flag = true;
					}

				}
				if (flag == true) {
					for (i = 0; i < headers.getLastCellNum(); i++) {
						record.getCell(i);
						if ((null != record.getCell(i)) && (record.getCell(i).getCellType() == CellType.NUMERIC)) {
							if (DateUtil.isCellDateFormatted(record.getCell(i))) {

								DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

								value = dateFormat.format(record.getCell(i).getDateCellValue()).trim();

							} else {
								record.getCell(i).toString();

								value = record.getCell(i).toString().trim();
							}
							key = headers.getCell(i).toString().trim();

						} else {

							key = (headers.getCell(i) + "".toString()).trim() + "";
							value = (null != record.getCell(i)) ? (record.getCell(i) + "".toString()).trim() + "" : "";
						}
						dataMap.put(key, value);
						// System.out.println(key+" : "+value);
					}
					allValues.add(dataMap);
					dataMap = new HashMap<String, String>();

				}
				flag = false;
			}

			return allValues;

		} catch (NullPointerException e) {
			throw new NullPointerException("Failed due to NullPointerException" + e);
		} catch (EncryptedDocumentException e) {
			throw new EncryptedDocumentException("Failed due to EncryptedDocumentException" + e);
		} catch (IOException e) {
			throw new NullPointerException("Failed due to IOException" + e);
		}

	}

	//

	public HashMap<Integer, String[]> returnRowsUniqueValueBasedOnClassNameList(String sheetName, Class<?> className) {

		String[] clsParts = className.getName().split("\\.");
		String clsName = clsParts[(clsParts.length) - 1];
		// String[] allValues = null;

		HashMap<Integer, String[]> allValues = new HashMap<Integer, String[]>();
		try {
			FileInputStream file = new FileInputStream("./test-data/TestData.xlsx");
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet(sheetName);
			Iterator<Row> it = sheet.rowIterator();
			int i = 0;
			while (it.hasNext()) {

				Row record = it.next();
				String cellValue = record.getCell(1).toString() + "";
				if (cellValue.equalsIgnoreCase(clsName)) {
					allValues.put(i, new String[] { record.getCell(0).toString(), record.getCell(5).toString(),
							record.getCell(6).toString(), record.getCell(7).toString() });
					i++;
				}
			}
			return allValues;
		} catch (NullPointerException e) {
			throw new NullPointerException("Failed due to NullPointerException" + e);
		} catch (EncryptedDocumentException e) {
			throw new EncryptedDocumentException("Failed due to EncryptedDocumentException" + e);
		} catch (IOException e) {
			throw new NullPointerException("Failed due to IOException" + e);
		}
	}
	//

	/*
	 * Returns the ArrayList to Two-Dimensional Object array for dataProvider
	 * Iteration
	 */
	public Object[][] returnAllUniqueValues(String sheetName, Class<?> className) {

		ArrayList<String> listValues = returnRowsUniqueValueBasedOnClassName(sheetName, className);

		Object[][] allValues = new Object[listValues.size()][1];
		for (int i = 0; i < listValues.size(); i++) {
			allValues[i][0] = listValues.get(i);
		}
		return allValues;
	}

	public Object[][] returnAllUniqueValuesInArray(String sheetName, Class<?> className) {

		HashMap<Integer, String[]> listValues = returnRowsUniqueValueBasedOnClassNameList(sheetName, className);

		Object[][] allValues = new Object[listValues.size()][];

		for (int i = 0; i < listValues.size(); i++) {
			allValues[i] = new Object[listValues.get(i).length];
			allValues[i] = listValues.get(i);
		}

		return allValues;
	}

	public Object[][] returnAllUniqueValuesInMap(String workbookName, String sheetName, String testType) {

		List<HashMap<String, String>> listValues = returnRowsUniqueValueBasedOnTestTypeList(workbookName, sheetName,
				testType);

		Object[][] allValues = new Object[listValues.size()][1];

		for (int i = 0; i < listValues.size(); i++) {
			allValues[i][0] = listValues.get(i);
		}
		return allValues;
	}

	/*
	 * Puts all the excels rows from startRowValue to endRowValue and returns
	 * Two-Dimensional Object array for dataProvider Iteration
	 */
	public Object[][] returnRowsUniqueValueInArray(String sheetName, String startRowValue, String endRowValue) {

		Object[][] values = new String[3][1];
		try {
			FileInputStream file = new FileInputStream("./test-data/TestData.xlsx");
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet(sheetName);
			Iterator<Row> it = sheet.rowIterator();

			while (it.hasNext()) {

				Row record = it.next();
				String cellValue = record.getCell(0).toString();
				if (cellValue.equalsIgnoreCase(startRowValue)) {
					int j = 0;

					while (!(record.getCell(0).toString().equalsIgnoreCase(endRowValue))) {
						values[j][0] = record.getCell(0).toString();
						j++;
						record = it.next();
					}
					values[j][0] = record.getCell(0).toString();
					break;
				}
				break;
			}
		} catch (NullPointerException e) {
			throw new NullPointerException("Failed due to NullPointerException" + e);
		} catch (EncryptedDocumentException e) {
			throw new EncryptedDocumentException("Failed due to EncryptedDocumentException" + e);
		} catch (IOException e) {
			throw new NullPointerException("Failed due to IOException" + e);
		}

		return values;
	}

	public HashMap<String, String> readExcelData(String workbook, String sheetname, String uniqueValue) {
		try {
			String key, value;
			FileInputStream file = new FileInputStream(configProperties.get(workbook));
			HashMap<String, String> dataMap = new HashMap<String, String>();
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet(sheetname);
			Iterator<Row> it = sheet.rowIterator();

			Row headers = it.next();
			while (it.hasNext()) {
				Row record = it.next();
				String cellValue = record.getCell(0).toString().trim();
				if (cellValue.equalsIgnoreCase(uniqueValue)) {
					for (int i = 0; i < headers.getLastCellNum(); i++) {
						record.getCell(i);
						if ((null != record.getCell(i)) && (record.getCell(i).getCellType() == CellType.NUMERIC)) {
							if (DateUtil.isCellDateFormatted(record.getCell(i))) {
								DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								value = dateFormat.format(record.getCell(i).getDateCellValue()).trim();
							} else {
								String cellText = record.getCell(i).toString();
								if (cellText.endsWith(".0")) {
									value = cellText.substring(0, cellText.length() - 2).trim();
								} else {
									value = cellText.trim();
								}
							}
							key = headers.getCell(i).toString().trim();
						} else {
							key = (headers.getCell(i) + "".toString()).trim() + "";
							value = (null != record.getCell(i)) ? (record.getCell(i) + "".toString()).trim() + "" : "";
						}
						dataMap.put(key, value);
					}
					break;
				}
			}
			return dataMap;
		} catch (NullPointerException e) {
			throw new NullPointerException("Failed due to NullPointerException" + e);
		} catch (EncryptedDocumentException e) {
			throw new EncryptedDocumentException("Failed due to EncryptedDocumentException" + e);
		} catch (IOException e) {
			throw new NullPointerException("Failed due to IOException" + e);
		}
	}

	public HashMap<String, String> readExcelDataHeaders(String sheetname) {
		try {
			String key, value;
			FileInputStream file = new FileInputStream("./test-data/TestData.xlsx");
			HashMap<String, String> dataMap = new HashMap<String, String>();
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet(sheetname);
			Iterator<Row> it = sheet.rowIterator();

			while (it.hasNext()) {

				Row headers = it.next();
				for (int i = 0; i < headers.getLastCellNum(); i++) {
					key = headers.getCell(i).toString();
					value = headers.getCell(i).toString();
					dataMap.put(key, value);
				}
				break;
			}
			return dataMap;
		} catch (NullPointerException e) {
			throw new NullPointerException("Failed due to NullPointerException" + e);
		} catch (EncryptedDocumentException e) {
			throw new EncryptedDocumentException("Failed due to EncryptedDocumentException" + e);
		} catch (IOException e) {
			throw new NullPointerException("Failed due to IOException" + e);
		}
	}
	
	public String readValue(String key) {
		// if key is a number then use the as is
		if (java.util.regex.Pattern.matches("\\d+", key)) {
			return key;
		} else if (key.equalsIgnoreCase("Random")) {
			return generateRandomNo(10);
		} else {
			return readIniValue("CONFIG", key);
		}
	}
	
	public String readIniValue(String sectionName, String key) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			Ini.Section section = ini.get(sectionName);
			String value = section.get(key);
			return value;
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getBalanceFromIni(String balance, String walletType, String amount) {
		Ini ini;
		try {
			ini = new Ini(new File("./data.ini"));
			if (balance.equalsIgnoreCase("Store") && walletType.equalsIgnoreCase("retailer")) {
				ini.put("CustomerData", "Retailer Wallet Balance", amount);
				ini.store();
			} else if (balance.equalsIgnoreCase("Get") && walletType.equalsIgnoreCase("retailer")) {
				return ini.get("CustomerData", "Retailer Wallet Balance");
			} else if (balance.equalsIgnoreCase("Store") && walletType.equalsIgnoreCase("cashout")) {
				ini.put("CustomerData", "Cashout Wallet Balance", amount);
				ini.store();
			} else if (balance.equalsIgnoreCase("Get") && walletType.equalsIgnoreCase("cashout")) {
				return ini.get("CustomerData", "Cashout Wallet Balance");
			} else if (balance.equalsIgnoreCase("Store") && walletType.equalsIgnoreCase("merchant")) {
				ini.put("CustomerData", "Merchant Wallet Balance", amount);
				ini.store();
			} else if (balance.equalsIgnoreCase("Get") && walletType.equalsIgnoreCase("merchant")) {
				return ini.get("CustomerData", "Merchant Wallet Balance");
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTodaysDate(String format) {
		Format formatter = new SimpleDateFormat(format);
		String todaysDate = formatter.format(new Date());
		return todaysDate;
	}

	public String getTodaysDateOfMonth(int add) {
		int day = Calendar.getInstance(TimeZone.getTimeZone("India")).get(Calendar.DAY_OF_MONTH) + add;
		String dayOfMonth = "";
		if (day < 10) {
			dayOfMonth = String.valueOf("0" + day);
		} else {
			dayOfMonth = String.valueOf(day);
		}
		return dayOfMonth;
	}

	public String getTodaysMonth() {
		int month = Calendar.getInstance(TimeZone.getTimeZone("India")).get(Calendar.MONTH) + 1;
		String currentMonth = "";
		if (month < 10) {
			currentMonth = String.valueOf("0" + month);
		} else {
			currentMonth = String.valueOf(month);
		}
		return currentMonth;
	}

	public String generateRandomNumber(int number) {

		Random ran = new Random();
		int x = ran.nextInt(number);
		String randomNo = "1528900" + String.valueOf(x);
		return randomNo;
	}

	public String getTodaysDateAndTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar cal = Calendar.getInstance();
		Date tdy = cal.getTime();
		String today = df.format(tdy);

		return today;
	}

	public String getRequiredDateandTime(int daysToAdd) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DATE, daysToAdd);
		Date day = cal1.getTime();
		String reqDate = df.format(day);

		return reqDate;
	}

	public void printHeaders(Map<String, String> headers) {

		Reporter.log("\nHeaders used are : ", true);
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			Reporter.log(entry.getKey() + " : " + entry.getValue(), true);
		}
	}

	public Map<String, String> readHeadersFromExcel(String sheetname, String headersToCall) {

		Workbook wb;
		try {

			HashMap<String, String> headers = new HashMap<String, String>();

			String key, value;
			FileInputStream file = new FileInputStream("./test-data/TestData.xlsx");

			wb = WorkbookFactory.create(file);

			Sheet sheet = wb.getSheet(sheetname);

			for (Row currentRow : sheet) {
				if (currentRow.getCell(0).getStringCellValue().toLowerCase().equals(headersToCall)) {
					Row headerKeyRow = sheet.getRow(currentRow.getRowNum() + 1);
					Row headerValueRow = sheet.getRow(currentRow.getRowNum() + 2);
					for (int i = 0; i < (headerKeyRow.getLastCellNum() - headerKeyRow.getFirstCellNum()); i++) {
						key = headerKeyRow.getCell(i).getStringCellValue();
						headerValueRow.getCell(i);
						if (headerValueRow.getCell(i).getCellType() == CellType.NUMERIC) {
							headerValueRow.getCell(i).toString();
						}
						value = headerValueRow.getCell(i).getStringCellValue();
						headers.put(key, value);
					}
					return headers;
				}
			}
			return headers;

		} catch (NullPointerException e) {
			Reporter.log("Unable to load headers from the excelsheet..!");
			throw new NullPointerException("Failed due to NullPointerException" + e);
		} catch (EncryptedDocumentException e) {
			Reporter.log("Unable to load headers from the excelsheet..!");
			throw new EncryptedDocumentException("Failed due to EncryptedDocumentException" + e);
		} catch (IOException e) {
			Reporter.log("Unable to load headers from the excelsheet..!");
			throw new NullPointerException("Failed due to IOException" + e);
		}
	}

	/*
	 * Returns a random number for mobile number using utils from apache commons
	 */
	public String generateRandomStan() {

		return RandomStringUtils.randomAlphanumeric(10);

	}

	/*
	 * Returns a random number for stan using utils from apache commons
	 */
	public String generateRandomClientRefNumber() {

		return RandomStringUtils.randomNumeric(12);

	}

	public String generateRandomAlphaString(int count) {
		return RandomStringUtils.randomAlphabetic(count);
	}

	/*
	 * Returns a random number for stan
	 */
	public String generateRandomNo(int count) {
		return "8" + RandomStringUtils.randomNumeric(count - 1);
	}

	/*
	 * Returns a random number for stan
	 */
	public String returnRandomNumber() {

		Random rand = new Random();
		BigInteger upperLimit = new BigInteger("999999999999999");
		BigInteger result;
		do {
			result = new BigInteger(upperLimit.bitLength(), rand); // (2^4-1) =
																	// 15 is the
																	// maximum
																	// value
		} while (result.compareTo(upperLimit) >= 0); // exclusive of 13

		return result.toString();
	}

	/*
	 * Writes the API Execution details by creating new sheet for every run to Excel
	 * Report File, Iterates through the cells for a particular testcaseID and
	 * populates the data
	 */
	public void writeExecutionStatusToExcel(String[] APIExecutionDetails) throws InvalidFormatException, IOException {

		try {
			int rowToUpdate = 0;
			File file = new File(configProperties.get("testReport"));
			if (!(file.exists())) {
				file.createNewFile();
				Workbook workbook = new HSSFWorkbook();
				Sheet worksheet = workbook.createSheet(configProperties.get("reportSheetName"));
				Row headers = worksheet.createRow(0);

				headers.createCell(0).setCellValue("SERVER BUILD NUMBER");
				headers.createCell(1).setCellValue("CLIENT BUILD NUMBER");
				headers.createCell(2).setCellValue("FLOW NAME");
				headers.createCell(3).setCellValue("TCID");
				headers.createCell(4).setCellValue("TEST DESCRIPTION");
				headers.createCell(5).setCellValue("RESULT");
				headers.createCell(6).setCellValue("(WARNING) REASON OF FAILURE");
				headers.createCell(7).setCellValue("TEST START TIME");
				headers.createCell(8).setCellValue("TEST END TIME");
				FileOutputStream fileOut = new FileOutputStream(file);
				workbook.write(fileOut);
				workbook.close();
				fileOut.close();
			}
			FileInputStream fileIn = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(fileIn);
			Sheet worksheet = workbook.getSheet(configProperties.get("reportSheetName"));
			rowToUpdate = worksheet.getLastRowNum() + 1;
			int i;
			Row record = worksheet.createRow(rowToUpdate);
			Cell cell = null;
			for (i = 0; i < APIExecutionDetails.length; i++) {
				cell = record.createCell(i);
				cell.setCellValue(APIExecutionDetails[i]);
			}

			FileOutputStream fileOut = new FileOutputStream(new File(configProperties.get("testReport")));
			workbook.write(fileOut);
			workbook.close();
			fileOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Returns the test case execution status based on its execution status code
	 */
	public String getExecutionResultStatus(int statusCode) {

		String testStatus = null;
		if (statusCode == 1) {
			testStatus = "PASS";
		} else if (statusCode == 2) {
			testStatus = "FAIL";
		} else if (statusCode == 3) {
			testStatus = "SKIP";
		}

		return testStatus;
	}

	/*
	 * Returns the set of all API's executed, as per its excel data
	 */
	public Set<String> returnAllAPINames(String excelFileName, String sheetName)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		Set<String> allAPI = new HashSet<String>();

		FileInputStream file = new FileInputStream(excelFileName);
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetName);
		Iterator<Row> it = sheet.rowIterator();

		while (it.hasNext()) {

			Row record = it.next();
			String cellValue = record.getCell(2).toString();
			allAPI.add(cellValue);
		}
		return allAPI;
	}

	/*
	 * Returns the total, passed, failed and skipped tests for a particular API from
	 * its Excel data
	 */
	public int[] returnTestCountPerAPI(String excelFileName, String sheetName, String API)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		FileInputStream file = new FileInputStream(excelFileName);
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetName);
		Iterator<Row> it = sheet.rowIterator();
		int total = 0, pass = 0, fail = 0, skip = 0;

		while (it.hasNext()) {

			Row record = it.next();
			String cellValue = record.getCell(2).toString().trim();
			if (cellValue.equalsIgnoreCase(API)) {
				String status = record.getCell(4).toString().trim();
				if (status.equalsIgnoreCase("PASS")) {
					pass++;
					total++;
				} else if (status.equalsIgnoreCase("FAIL")) {
					fail++;
					total++;
				} else if (status.equalsIgnoreCase("SKIP")) {
					skip++;
					total++;
				}
			}
		}
		return new int[] { total, pass, fail, skip };
	}

	/*
	 * Puts the values to the velocity template by iterating through all the values
	 * in the variable array
	 */

	public String report() throws EncryptedDocumentException, InvalidFormatException, IOException {
		StringBuilder form = new StringBuilder();
		HashMap<String, int[]> result = consolidatedReport();
		int totalTestExecuted = 0, totalPassed = 0, totalFailed = 0;
		form.append("<html>"
				+ "<table style='border-spacing: 0px; padding:5px; font-family: monospace; font-size: 1em;'>"
				+ "<tr style='background-color:#a3a3f5;font-weight: bold;font-family: monospace;font-size: 1.1em;'> "
				+ "<td style='border:1px solid;padding:5px'>DATE OF EXECUTION</td>"
				+ "<td style='border:1px solid;padding:5px'>FLOW NAME</td>"
				+ "<td style='border:1px solid;padding:5px'>ITERATION</td>"
				+ "<td style='border:1px solid;padding:5px'>TOTAL FLOWS EXECUTED</td>"
				+ "<td style='border:1px solid;padding:5px'>TOTAL PASSED</td>"
				+ "<td style='border:1px solid;padding:5px'>TOTAL FAILED</td>" + "</tr>");
		for (Map.Entry<String, int[]> data : result.entrySet()) {
			form.append("<tr style='font-family: monospace;font-size: 1em'>"
					+ "<td style='border:1px solid;text-align: center;padding:5px'>" + getTodaysDate("dd-MM-yyyy")
					+ "</td>" + "<td style='border:1px solid;padding:5px'>" + data.getKey() + "</td>"
					+ "<td style='border:1px solid;text-align: center;padding:5px'>"
					+ configProperties.get("buildNumber") + "</td>"
					+ "<td style='border:1px solid;text-align: center;padding:5px'>" + data.getValue()[2] + "</td>"
					+ "<td style='border:1px solid;text-align: center;padding:5px'>" + data.getValue()[0] + "</td>"
					+ "<td style='border:1px solid;text-align: center;padding:5px'>" + data.getValue()[1] + "</td>"
					+ "</tr>");
			totalTestExecuted += data.getValue()[2];
			totalPassed += data.getValue()[0];
			totalFailed += data.getValue()[1];

		}
		form.append("<tr style='font-family: monospace;font-size: 1em'>"
				+ "<td style='border:1px solid;text-align: center;padding:5px'></td>"
				+ "<td style='border:1px solid;padding:5px'></td>"
				+ "<td style='border:1px solid;text-align: center;padding:5px'></td>"
				+ "<td style='border:1px solid;text-align: center;padding:5px'>" + totalTestExecuted + "</td>"
				+ "<td style='border:1px solid;text-align: center;padding:5px'>"
				+ (totalPassed * 100 / totalTestExecuted) + " %</td>"
				+ "<td style='border:1px solid;text-align: center;padding:5px'>"
				+ (totalFailed * 100 / totalTestExecuted) + " %</td>" + "</tr>");
		form.append("</table></html>");
		return form.toString();
	}

	public HashMap<String, int[]> consolidatedReport()
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		String sheetname = configProperties.get("reportSheetName");
		FileInputStream file = new FileInputStream(configProperties.get("testReport"));
		HashMap<String, int[]> executionResult = new HashMap<String, int[]>();
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetname);
		Iterator<Row> it = sheet.rowIterator();
		while (it.hasNext()) {
			Row record = it.next();
			String api = record.getCell(1).toString();
			String result = record.getCell(4).toString();
			if (null != executionResult.get(api)) {
				if (result.equalsIgnoreCase("PASS")) {
					++executionResult.get(api)[0];
				} else if (result.equalsIgnoreCase("FAIL")) {
					++executionResult.get(api)[1];
				}
				++executionResult.get(api)[2];
			} else {
				if (result.equalsIgnoreCase("PASS")) {
					executionResult.put(api, new int[] { 1, 0, 1 });
				} else if (result.equalsIgnoreCase("FAIL")) {
					executionResult.put(api, new int[] { 0, 1, 1 });
				}

			}

		}

		/*
		 * for(Map.Entry<String, int[]> value : executionResult.entrySet()){ String key
		 * = value.getKey(); int [] arr = value.getValue(); System.out.println(key+"  "
		 * +Arrays.toString(arr)); }
		 */

		return executionResult;
	}

	public HashMap<String, String> readDeviceConfig(String sectionName) throws InvalidFileFormatException, IOException {
		Set<Entry<String, String>> dataSet = null;
		HashMap<String, String> deviceConf = new HashMap<String, String>();
		Ini ini = new Ini(new File("./config.ini"));
		Ini.Section section = ini.get(sectionName);
		dataSet = section.entrySet();
		for (Map.Entry<String, String> set : dataSet) {
			deviceConf.put(set.getKey().toString(), set.getValue().toString());
		}
		return deviceConf;

	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String msg) {
		failureReason = msg;
	}

	public void startAppiumServer(String server, String port) throws IOException {

		// prop = loadPropertyFile();

		CommandLine command = new CommandLine(configProperties.get("nodePath"));
		command.addArgument(configProperties.get("appiumPath"), false);
		String appiumServer = configProperties.get("appiumServer");
		command.addArgument("--address", true);
		command.addArgument(server);
		command.addArgument("--port", true);
		command.addArgument(port);
		command.addArgument("--session-override", true);
		command.addArgument("--no-reset");
		command.addArgument("--log-level", false);
		command.addArgument("error");
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		ExecuteWatchdog watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.setWatchdog(watchdog);
		String url = null;
		try {
			System.out.println("Command to be executed : " + command.toString());
			executor.execute(command, resultHandler);
			// AppiumServiceBuilder builder = new AppiumServiceBuilder();
			// builder.withArgument(GeneralServerFlag.LOG_LEVEL, "info");
			url = appiumServer + ":" + port;
			Thread.sleep(5000);
			System.out.println("Appium Server Started on URL : " + url);
		} catch (IOException e) {
			System.out.println("Unable to start appium on " + url);
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Unable to start appium on " + url);
			e.printStackTrace();
		}
	}

	public void stopAppiumServer() {
		// Add different arguments In command line which requires to stop appium
		// server.
		Reporter.log("Stopping Appium server", true);
		try {
			Thread.sleep(2000);

			CommandLine command = new CommandLine(
					"c:\\windows\\system32\\cmd.exe /c c:\\windows\\system32\\TASKKILL.exe /F /IM node.exe");
			// Execute command line arguments to stop appium server.
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1);

			executor.execute(command, resultHandler);

			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Reporter.log("Appium server stopped successfully", true);
	}

	/*
	 * Returns the test case execution time
	 */
	public String getTestExcutionTime(long millisec) {
		String dateFormat = "dd-MM-yyyy hh:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millisec);
		String executionTime = simpleDateFormat.format(calendar.getTime());
		return executionTime;
	}

	public String generateRandomMobileNumber() {
		Random rand = new Random();
		int maxF = 6, minF = 6;
		int firstDigit = rand.nextInt((maxF - minF) + 1) + minF;
		int maxR = 999999999, minR = 100000000;
		int restDigits = rand.nextInt((maxR - minR) + 1) + minR;
		String mobileNum = Integer.toString(firstDigit) + Integer.toString(restDigits);
		return mobileNum;
	}

	public String generateRandomBeneName() {
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 4) { // length of the random string
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return "Bene " + saltStr;
	}

	public String generateRandomShopName() {
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder salt = new StringBuilder();
		StringBuilder salt1 = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 4) { // length of the random string
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		while (salt1.length() < 1) { // length of the random string
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt1.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		String saltStrUpper = salt1.toString();
		return saltStrUpper.toUpperCase() + saltStr + " Shop";
	}

	public String generateRandomCustomerName() {
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 6) { // length of the random string
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	public String generateRandomEmailId() {
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 8) { // length of the random string
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr + "@gmail.com";
	}

	public String generateRandomTermNumber() {
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 10) { // length of the random string
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	public String generateRandomTxnNumber() {
		String SALTCHARS = "1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 4) { // length of the random string
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	public String generateRandomReferenceNumber() {
		Random rand = new Random();
		int maxF = 9, minF = 1;
		int firstDigit = rand.nextInt((maxF - minF) + 1) + minF;
		int maxR = 999999999, minR = 100000000;
		int restDigits = rand.nextInt((maxR - minR) + 1) + minR;
		String refNum = Integer.toString(firstDigit) + Integer.toString(restDigits);
		return refNum;
	}

	public String currentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		return dtf.format(localDate);
	}

	public String todayDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd");
		LocalDate localDate = LocalDate.now();
		return dtf.format(localDate);
	}

	public void pressEnter() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	public void pressTab() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
	}

	public void pageDown() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_PAGE_DOWN);
		robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
	}

	public void pageUp() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_PAGE_UP);
		robot.keyRelease(KeyEvent.VK_PAGE_UP);
	}

	public String generateRandomNumber(int max, int min) {
		Random rand = new Random();
		int maxF = 9, minF = 1;
		int firstDigit = rand.nextInt((maxF - minF) + 1) + minF;
		int maxR = max, minR = min;
		int restDigits = rand.nextInt((maxR - minR) + 1) + minR;
		String mobileNum = Integer.toString(firstDigit) + Integer.toString(restDigits);
		return mobileNum;
	}

	public void enter() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	public HashMap<String, String> readSectionFromIni(String sectionName) {
		HashMap<String, String> conf = new HashMap<String, String>();
		Set<Entry<String, String>> dataSet;
		Ini ini;
		try {
			ini = new Ini(new File("./config.ini"));

			Ini.Section section = ini.get(sectionName);
			dataSet = section.entrySet();

			for (Map.Entry<String, String> set : dataSet) {
				conf.put(set.getKey().toString(), set.getValue().toString());
			}
			return conf;
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Remove rupee symbol and comma from the string
	public String replaceSymbols(String value) {
		String editedElement = value.replaceAll("₹", "").replaceAll(",", "").trim();
		return editedElement;
	}

	public double roundTo2Decimals(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

	@Override
	protected void isLoaded() throws Error {

	}

	@Override
	protected void load() {

	}
}