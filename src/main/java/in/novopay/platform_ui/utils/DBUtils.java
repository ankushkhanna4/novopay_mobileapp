package in.novopay.platform_ui.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DBUtils extends JavaUtils {
	protected Connection conn;
	protected Statement stmt;
	// private String codeId;
	private static String organizationId;

	public String getOrganizationId() {
		return DBUtils.organizationId;
	}

	public void setOrganizationId(String organizationId) {
		DBUtils.organizationId = organizationId;
	}

	public Connection createConnection(String dbSchemaName) throws ClassNotFoundException {

		String dbSchema = configProperties.get("dbUrl") + dbSchemaName;
		String jdbcDriver = configProperties.get("jdbcDriver");
		try {
			if ((null == conn) || (!conn.getCatalog().equalsIgnoreCase(dbSchemaName))) {
				Class.forName(jdbcDriver);
				conn = DriverManager.getConnection(dbSchema, configProperties.get("dbUserName"),
						configProperties.get("dbPassword"));
				stmt = conn.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void closeConnection(Connection conn) {

		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to close the connection due to below error..!");
			e.printStackTrace();
		}
	}

	public String getPanNumber(String mobileNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("master"));
			String query = "SELECT attr_value FROM master.organization_attribute "
					+ "WHERE attr_key='PAN' AND orgnization_id = (SELECT u.`organization` FROM `master`.`user` AS u "
					+ "JOIN `master`.`user_attribute` AS ua ON u.`id` = ua.`user_id` "
					+ "JOIN `master`.organization o ON u.organization = o.id WHERE ua.`attr_value`='" + mobileNum
					+ "' ORDER BY u.id DESC LIMIT 1);";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String getBank(String beneIFSC) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npMaster"));
			String query = "SELECT bank FROM np_master.`ifsc_master_new` WHERE ifsc_code = '" + beneIFSC + "';";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String updateRemitterName(String remitter) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("rblSimulator"));
			String query = "UPDATE `rblsimulator`.`beneficiary` SET `name` = 'John' WHERE remitterid = (SELECT remitterid FROM `rblsimulator`.`remitter` WHERE mobile = '"
					+ remitter + "');";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String deleteRemitterName(String remitter) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("rblSimulator"));
			String query = "UPDATE `rblsimulator`.`beneficiary` SET `name` = '' WHERE remitterid = (SELECT remitterid FROM `rblsimulator`.`remitter` WHERE mobile = '"
					+ remitter + "');";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String deleteBeneFromRBLSimulator(String remitter) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("rblSimulator"));
			String query = "DELETE FROM `rblsimulator`.`beneficiary` WHERE remitterid = (SELECT remitterid FROM `rblsimulator`.`remitter` WHERE mobile = '"
					+ remitter + "');";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String getRemittanceCharges(String amount, String category, String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("limitCharges"));
			String code = "";
			if (partner.equalsIgnoreCase("AXIS")) {
				code = "AXIS_REMIT_IMPS_CUSTOMER_CHARGE_ACTUAL";
			} else if (partner.equalsIgnoreCase("RBL")) {
				code = "RBL_REMIT_IMPS_CUSTOMER_CHARGE_DISPLAY";
			} else if (partner.equalsIgnoreCase("FINO")) {
				code = "FINO_REMIT_IMPS_CUSTOMER_CHARGE_DISPLAY";
			} else if (partner.equalsIgnoreCase("YBL")) {
				code = "YBL_REMIT_IMPS_CUSTOMER_CHARGE_DISPLAY";
			}
			if (Integer.parseInt(category) == 4) {
				code = code + "_KRO";
			}
			String query = "SELECT IF(" + amount + "*`percentage`/10000>`min_charge`/100, ROUND(" + amount
					+ "*`percentage`/10000,2), ROUND(`min_charge`/100,2)) charge FROM `limit_charges`.`charge_category_slabs` "
					+ "WHERE `category_code`='" + code + "' AND " + amount
					+ " BETWEEN `slab_from_amount`/100+1 AND `slab_to_amount`/100;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String getRemittanceComm(String amount, String category, String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("limitCharges"));
			String code = "";
			if (partner.equalsIgnoreCase("FINO")) {
				code = "FINO_REMIT_IMPS_AGENT_COMM";
			} else if (partner.equalsIgnoreCase("YBL")) {
				code = "YBL_REMIT_IMPS_AGENT_COMM";
			}
			if (Integer.parseInt(category) == 4) {
				code = code + "_KRO";
			}
			String query = "SELECT IF(" + amount + "*`percentage`/10000>`min_charge`/100, ROUND(" + amount
					+ "*`percentage`/10000,2), ROUND(`min_charge`/100,2)) comm FROM `limit_charges`.`charge_category_slabs` "
					+ "WHERE `category_code`='" + code + "' AND " + amount
					+ " BETWEEN `slab_from_amount`/100+1 AND `slab_to_amount`/100;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String getAepsComm(String amount, String txnType) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("limitCharges"));
			String code = "";
			if (txnType.equalsIgnoreCase("Deposit")) {
				code = "RBL_AEPS_DEPOSIT_AGENT_COMM";
			} else if (txnType.equalsIgnoreCase("Withdrawal")) {
				code = "RBL_AEPS_WITHDRAWAL_AGENT_COMM";
			}
			String query = "SELECT IF(" + amount + "*`percentage`/10000<`max_charge`/100, ROUND(" + amount
					+ "*`percentage`/10000,2), ROUND(`max_charge`/100,2)) charge FROM `limit_charges`.`charge_category_slabs` "
					+ "WHERE `category_code`='" + code + "' AND " + amount
					+ " BETWEEN `slab_from_amount`/100+1 AND `slab_to_amount`/100;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String getWalletBalance(String mobNum, String wallet) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("wallet"));
			String query = "";
			if (wallet.equalsIgnoreCase("retailer")) {
				query = "SELECT `account_balance_derived` FROM `wallet`.`m_savings_account` WHERE `account_no` = (SELECT attr_value FROM `master`.`organization_attribute` oa WHERE `attr_key` = 'WALLET_ACCOUNT_NUMBER' AND `orgnization_id` = (SELECT `organization` FROM `master`.`user` WHERE `id` = (SELECT `user_id` FROM `master`.`user_attribute` WHERE `attr_value` = '"
						+ mobNum + "' ORDER BY id DESC LIMIT 1)));";
			} else if (wallet.equalsIgnoreCase("cashout")) {
				query = "SELECT `account_balance_derived` FROM `wallet`.`m_savings_account` WHERE `account_no` = (SELECT attr_value FROM `master`.`organization_attribute` oa WHERE `attr_key` = 'CASH_OUT_WALLET_ACCOUNT_NUMBER' AND `orgnization_id` = (SELECT `organization` FROM `master`.`user` WHERE `id` = (SELECT `user_id` FROM `master`.`user_attribute` WHERE `attr_value` = '"
						+ mobNum + "' ORDER BY id DESC LIMIT 1)));";
			} else {
				query = "SELECT `account_balance_derived` FROM `wallet`.`m_savings_account` WHERE `account_no` = (SELECT attr_value FROM `master`.`organization_attribute` oa WHERE `attr_key` = 'OTC_MERCHANT_WALLET_ACCOUNT_NUMBER' AND `orgnization_id` = (SELECT `organization` FROM `master`.`user` WHERE `id` = (SELECT `user_id` FROM `master`.`user_attribute` WHERE `attr_value` = '"
						+ mobNum + "' ORDER BY id DESC LIMIT 1)));";
			}
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String updateWalletBalance(String mobNum, String wallet, String amount) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("wallet"));
			String query = "";
			if (wallet.equalsIgnoreCase("retailer")) {
				query = "UPDATE `wallet`.`m_savings_account` SET `account_balance_derived` = '" + amount
						+ ".000000' WHERE `account_no` = (SELECT attr_value FROM `master`.`organization_attribute` oa WHERE `attr_key` = 'WALLET_ACCOUNT_NUMBER' AND `orgnization_id` = (SELECT `organization` FROM `master`.`user` WHERE `id` = (SELECT `user_id` FROM `master`.`user_attribute` WHERE `attr_value` = '"
						+ mobNum + "' ORDER BY id DESC LIMIT 1)));";
			} else if (wallet.equalsIgnoreCase("cashout")) {
				query = "UPDATE `wallet`.`m_savings_account` SET `account_balance_derived` = '" + amount
						+ ".000000' WHERE `account_no` = (SELECT attr_value FROM `master`.`organization_attribute` oa WHERE `attr_key` = 'CASH_OUT_WALLET_ACCOUNT_NUMBER' AND `orgnization_id` = (SELECT `organization` FROM `master`.`user` WHERE `id` = (SELECT `user_id` FROM `master`.`user_attribute` WHERE `attr_value` = '"
						+ mobNum + "' ORDER BY id DESC LIMIT 1)));";
			} else {
				query = "UPDATE `wallet`.`m_savings_account` SET `account_balance_derived` = '" + amount
						+ ".000000' WHERE `account_no` = (SELECT attr_value FROM `master`.`organization_attribute` oa WHERE `attr_key` = 'OTC_MERCHANT_WALLET_ACCOUNT_NUMBER' AND `orgnization_id` = (SELECT `organization` FROM `master`.`user` WHERE `id` = (SELECT `user_id` FROM `master`.`user_attribute` WHERE `attr_value` = '"
						+ mobNum + "' ORDER BY id DESC LIMIT 1)));";
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String getBeneAmount(String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("config"));
			String query = "";
			if (partner.equalsIgnoreCase("RBL")) {
				query = "SELECT prop_value FROM config.configuration WHERE prop_key = 'rbl.bene.val.amount';";
			} else if (partner.equalsIgnoreCase("AXIS")) {
				query = "SELECT prop_value FROM config.configuration WHERE prop_key = 'axis.bene.val.amount';";
			} else if (partner.equalsIgnoreCase("YBL")) {
				query = "SELECT prop_value FROM config.configuration WHERE prop_key = 'ybl.bene.val.amount';";
			} else if (partner.equalsIgnoreCase("FINO")) {
				query = "SELECT prop_value FROM config.configuration WHERE prop_key = 'fino.bene.val.amount';";
			}
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String updateQueuingConfig(String threshold, String partner, String code) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npRemittance"));
			String query = "UPDATE np_remittance.queuing_config SET `threshold_error_count` = '" + threshold
					+ "' WHERE partner = '" + partner + "' AND error_code = '" + code + "';";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public List<String> getBankCodeToUnqueue(String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("config"));
			String query = "SELECT bank_code FROM `np_master`.`bank_master` WHERE bank_name "
					+ "IN (SELECT bank_name FROM `np_remittance`.`queued_banks` WHERE partner = '" + partner
					+ "' AND end_time IS NULL)";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			List<String> list = new ArrayList<String>();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String verifyIfQueuingIsEnabled(String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npRemittance"));
			String query = "SELECT end_time FROM `np_remittance`.`queued_banks` where partner = '" + partner
					+ "' ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String verifyIfTxnIsQueued(String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npRemittance"));
			String query = "SELECT IF(STRCMP((SELECT payment_ref_code FROM `np_remittance`.`queued_remittance` where partner = '"
					+ partner + "' ORDER BY id DESC LIMIT 1),"
					+ "(SELECT payment_ref_code FROM `np_remittance`.`remittance_outward_table` where partner = '"
					+ partner + "' ORDER BY id DESC LIMIT 1)) = 0, 'Queued', 'NotQueued') Txn;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String getQueuedBankName() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npRemittance"));
			String query = "SELECT bank_name FROM `np_remittance`.`queued_banks` WHERE end_time IS NULL ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String deleteRecordsFromFRC(String code, String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npRemittance"));
			String query = "";
			if (partner.equalsIgnoreCase("RBL")) {
				query = "DELETE FROM `np_remittance`.`failed_remittance_code` WHERE error_code = '" + code
						+ "' AND partner = '" + partner + "';";
			} else {
				query = "DELETE FROM `np_remittance`.`failed_remittance_code` WHERE error_code = '" + code
						+ "' AND partner = '" + partner + "';";
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public void updateRBLTransactionStatus(String bankRefCode, String paymentStatus) throws ClassNotFoundException {
		try {
			String sql = "UPDATE `transaction` SET `paymentstatus`='" + paymentStatus + "', `isrefundfile`='YES' "
					+ "WHERE `channelpartnerrefno`='" + bankRefCode + "';";
			conn = createConnection(configProperties.get("rblSimulator"));
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateBatchStatus(String batchName, String status) throws ClassNotFoundException {
		try {
			String sql = "UPDATE batch_master SET `last_run_status` = '" + status + "' WHERE job_name ='" + batchName
					+ "';";
			conn = createConnection(configProperties.get("npOps"));
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String selectPaymentRefCode() throws ClassNotFoundException {
		try {
			String query = "SELECT payment_ref_code FROM `np_remittance`.`remittance_outward_table` ORDER BY id DESC LIMIT 1;";
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateRBLTxnStatus(String paymentRefCode) throws ClassNotFoundException {
		try {
			String sql = "UPDATE `np_remittance`.`remittance_outward_table` SET `status`='UNKNOWN' WHERE payment_ref_code = '"
					+ paymentRefCode + "';";
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateAxisTransactionStatus(String bankRefCode) throws ClassNotFoundException {
		try {
			String sql = "UPDATE `remittance_outward_table` SET `status`='FAIL' WHERE `payment_ref_code`='"
					+ bankRefCode + "';";
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getPaymentStatusAndRRNNo(String paymentRefCode) throws ClassNotFoundException {
		try {
			String query = "SELECT `status`,`bank_ref_code` FROM `remittance_outward_table` WHERE `payment_ref_code`="
					+ paymentRefCode;
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			return new String[] { rs.getString("status"), rs.getString("bank_ref_code") };
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPAN(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("master"));
			String query = "SELECT attr_value FROM master.`organization_attribute` WHERE attr_key = 'PAN' AND `orgnization_id` = (SELECT organization FROM master.user WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '"
					+ mobNum + "' ORDER BY id DESC LIMIT 1))";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTDSPercentage(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("config"));
			String query = "";
			if (getPAN(mobNum) != null) {
				query = "SELECT prop_value FROM `config`.`configuration` WHERE prop_key = 'novopay.agent.tds.percentage.with.pan'";
			} else {
				query = "SELECT prop_value FROM `config`.`configuration` WHERE prop_key = 'novopay.agent.tds.percentage.without.pan'";
			}
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String modeOfTransfer(String ifscCode) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npMaster"));
			String query = "SELECT imps_supported FROM np_master.ifsc_master_new WHERE ifsc_code = '" + ifscCode + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String ifscCodeDetails(String ifscCode, String key) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npMaster"));
			String query = "SELECT bank, state, district, branch FROM `np_master`.`ifsc_master_new` WHERE ifsc_code = '"
					+ ifscCode + "';";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			return rs.getString(key);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getListOfRetailerMobNum() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npSales"));
			String query = "SELECT ua.attr_value, u.status FROM master.user u "
					+ "JOIN master.user_attribute ua ON u.id = ua.user_id WHERE ua.attr_key = 'MSISDN'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			List<String> list = new ArrayList<String>();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String getLimitRemaining(String mobNum, String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npMaster"));
			Date date = new Date();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int currentMonth = localDate.getMonthValue();
			String month = "";
			if (currentMonth >= 10) {
				month = Integer.toString(currentMonth);
			} else {
				month = "0" + Integer.toString(currentMonth);
			}
			String query = "SELECT (SELECT `monthly_amount_limit` FROM `limit_charges`.`limit_category_master` "
					+ "WHERE `category_code` = 'LIMITS_REMITTER_" + partner + "_NO_KYC')-(SELECT IF((SELECT "
					+ "SUM(txn_amount) FROM `limit_charges`.`entity_consumed_limits` "
					+ "WHERE category = 'LIMITS_REMITTER_" + partner + "_NO_KYC' AND entity_id = '" + mobNum
					+ "' AND is_reversed = '0' AND txn_date LIKE '2019-" + month
					+ "-%') IS NULL, 0, (SELECT SUM(txn_amount) FROM `limit_charges`.`entity_consumed_limits` "
					+ "WHERE category = 'LIMITS_REMITTER_" + partner + "_NO_KYC' AND entity_id = '" + mobNum
					+ "' AND is_reversed = '0' AND txn_date LIKE '2019-" + month + "-%')))";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateBlackoutDuration(String duration) throws ClassNotFoundException {
		try {
			String sql = "UPDATE `service_repo`.`transaction_blackout_config` SET `blackout_duration` = '" + duration
					+ "' WHERE `api_name` = 'moneyTransferBatch' AND `channel_id` = 'AGENTAPP';";
			conn = createConnection(configProperties.get("npRemittance"));
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getChargeCategory(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("master"));
			String query = "SELECT chrge_category FROM master.organization "
					+ "WHERE id = (SELECT u.`organization` FROM `master`.`user` AS u "
					+ "JOIN `master`.`user_attribute` AS ua ON u.`id`=ua.`user_id` WHERE ua.`attr_value`='" + mobNum
					+ "' ORDER BY u.id DESC LIMIT 1);";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String updateMPIN(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("master"));
			String query = "UPDATE `master`.`user_auth_mechanism` "
					+ "SET `value` = '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c' "
					+ "WHERE user_id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
					+ "' ORDER BY id DESC LIMIT 1);";
			System.out.println(query);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB!! BC Agent ID update  failed..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String sms() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("smsLog"));
			String query = "SELECT message FROM sms_log.sms_log ORDER BY id DESC LIMIT 1;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String smsFt() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("smsLog"));
			String query = "SELECT message FROM sms_log.sms_log ORDER BY id DESC LIMIT 1 OFFSET 1;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String smsNum1() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("smsLog"));
			String query = "SELECT sent_to FROM sms_log.sms_log ORDER BY id DESC LIMIT 1;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String smsNum2() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("smsLog"));
			String query = "SELECT sent_to FROM sms_log.sms_log ORDER BY id DESC LIMIT 1 OFFSET 1;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String beneAccountPAN() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT handle_value FROM `np_actor`.`fin_inst_handle` ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String paymentRefCode(String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "";
			if (partner == "all") {
				query = "SELECT `payment_ref_code` FROM `np_remittance`.`remittance_outward_table` ORDER BY id DESC LIMIT 1;";
			} else {
				query = "SELECT `payment_ref_code` FROM `np_remittance`.`remittance_outward_table` where partner = '"
						+ partner + "' ORDER BY id DESC LIMIT 1;";
			}
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String verifyIfQueuingIsDisabled(Map<String, String> usrData, String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT end_remarks FROM `np_remittance`.`queued_banks` WHERE partner = '" + partner
					+ "' AND bank_name = '" + getBank(usrData.get("BENEIFSC")) + "' ORDER BY id DESC LIMIT 1;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String getPaymentRefCode(String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT payment_ref_code FROM `np_remittance`.`queued_remittance` where partner = '"
					+ partner + "' ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String getBankRefCode(String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));

			String query = "SELECT bank_ref_code FROM `np_remittance`.`remittance_outward_table` WHERE partner = '"
					+ partner + "' ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String getBankRefCodePS(String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));

			String query = "SELECT bank_ref_code FROM `np_remittance`.`remittance_outward_table` WHERE partner = '"
					+ partner + "' ORDER BY id DESC LIMIT 1 OFFSET 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> queuedTxnReport(String mobNum, int limit) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT \r\n"
					+ "	CONCAT(DAY(DATE(rot.`created_on`)),' ',SUBSTRING(MONTHNAME(DATE(rot.`created_on`)),1,3),' ',YEAR(DATE(rot.`created_on`))) Txn_Date, \r\n"
					+ "	CONCAT(CONCAT(DAY(DATE(rot.`created_on`)),' ',SUBSTRING(MONTHNAME(DATE(rot.`created_on`)),1,3),' ',YEAR(DATE(rot.`created_on`))),', ',CONCAT(IF(IF(HOUR(TIME(rot.`created_on`))>12,HOUR(TIME(rot.`created_on`))-12,HOUR(TIME(rot.`created_on`)))<10,IF(CONCAT('0',IF(HOUR(TIME(rot.`created_on`))>12,HOUR(TIME(rot.`created_on`))-12,HOUR(TIME(rot.`created_on`))))='00',12,CONCAT('0',IF(HOUR(TIME(rot.`created_on`))>12,HOUR(TIME(rot.`created_on`))-12,HOUR(TIME(rot.`created_on`))))),IF(HOUR(TIME(rot.`created_on`))>12,HOUR(TIME(rot.`created_on`))-12,HOUR(TIME(rot.`created_on`)))),RIGHT(TIME(rot.`created_on`),6)),' ',IF(HOUR(TIME(rot.`created_on`))<12,'AM','PM')) `Txn_Time_Initiated`,\r\n"
					+ "	CONCAT(CONCAT(DAY(DATE(rot.`processed_on`)),' ',SUBSTRING(MONTHNAME(DATE(rot.`processed_on`)),1,3),' ',YEAR(DATE(rot.`processed_on`))),', ',CONCAT(IF(IF(HOUR(TIME(rot.`processed_on`))>12,HOUR(TIME(rot.`processed_on`))-12,HOUR(TIME(rot.`processed_on`)))<10,IF(CONCAT('0',IF(HOUR(TIME(rot.`processed_on`))>12,HOUR(TIME(rot.`processed_on`))-12,HOUR(TIME(rot.`processed_on`))))='00',12,CONCAT('0',IF(HOUR(TIME(rot.`processed_on`))>12,HOUR(TIME(rot.`processed_on`))-12,HOUR(TIME(rot.`processed_on`))))),IF(HOUR(TIME(rot.`processed_on`))>12,HOUR(TIME(rot.`processed_on`))-12,HOUR(TIME(rot.`processed_on`)))),RIGHT(TIME(rot.`processed_on`),6)),' ',IF(HOUR(TIME(rot.`processed_on`))<12,'AM','PM')) `Txn_Time_Processed`,\r\n"
					+ "	IF(STRCMP(rot.`remittance_type`,\"C2A\") = 0,'REMITTANCE','F') Txn_Type,\r\n"
					+ "	rot.`remitter_msisdn` `Remitter_Mobile#`,\r\n" + "	rot.`payment_ref_code` Txn_Id,\r\n"
					+ "	LEFT(rot.`amount`, LENGTH(rot.`amount`)-2) Txn_Amount,\r\n"
					+ "	rot.`beneficiary_bank` Bene_Bank,\r\n" + "	rot.`status` `Status` \r\n"
					+ "	FROM np_remittance.remittance_outward_table rot \r\n"
					+ "	JOIN np_remittance.queued_remittance qr ON rot.payment_ref_code = qr.`payment_ref_code` \r\n"
					+ "	WHERE rot.`csp_code` = (SELECT o.code FROM `master`.`user` AS u JOIN `master`.`user_attribute` AS ua ON u.`id`=ua.`user_id` JOIN `master`.organization o ON u.organization = o.id WHERE ua.`attr_value`='"
					+ mobNum
					+ "' ORDER BY u.id DESC LIMIT 1) AND qr.`created_on`>=DATE_ADD(CURDATE(), INTERVAL -1 DAY)\r\n"
					+ "	ORDER BY rot.id DESC LIMIT " + limit + ";";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> queuedBankReport() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT bank_name, partner, "
					+ "CONCAT(CONCAT(DAY(DATE(start_time)),' ',SUBSTRING(MONTHNAME(DATE(start_time)),1,3),' ',YEAR(DATE(start_time))),', ',CONCAT(IF(IF(HOUR(TIME(start_time))>12,HOUR(TIME(start_time))-12,HOUR(TIME(start_time)))<10,IF(CONCAT('0',IF(HOUR(TIME(start_time))>12,HOUR(TIME(start_time))-12,HOUR(TIME(start_time))))='00',12,CONCAT('0',IF(HOUR(TIME(start_time))>12,HOUR(TIME(start_time))-12,HOUR(TIME(start_time))))),IF(HOUR(TIME(start_time))>12,HOUR(TIME(start_time))-12,HOUR(TIME(start_time)))),RIGHT(TIME(start_time),6)),' ',IF(HOUR(TIME(start_time))<12,'AM','PM')) start_time, "
					+ "IF(end_time IS NULL, \"INQUEUE\", CONCAT(CONCAT(DAY(DATE(end_time)),' ',SUBSTRING(MONTHNAME(DATE(end_time)),1,3),' ',YEAR(DATE(end_time))),', ',CONCAT(IF(IF(HOUR(TIME(end_time))>12,HOUR(TIME(end_time))-12,HOUR(TIME(end_time)))<10,IF(CONCAT('0',IF(HOUR(TIME(end_time))>12,HOUR(TIME(end_time))-12,HOUR(TIME(end_time))))='00',12,CONCAT('0',IF(HOUR(TIME(end_time))>12,HOUR(TIME(end_time))-12,HOUR(TIME(end_time))))),IF(HOUR(TIME(end_time))>12,HOUR(TIME(end_time))-12,HOUR(TIME(end_time)))),RIGHT(TIME(end_time),6)),' ',IF(HOUR(TIME(end_time))<12,'AM','PM'))) end_time, "
					+ "IF(CONCAT(IF(HOUR(TIMEDIFF(end_time, start_time))<10,CONCAT('0',HOUR(TIMEDIFF(end_time, start_time))),HOUR(TIMEDIFF(end_time, start_time))),'h ',IF(MINUTE(TIMEDIFF(end_time, start_time))<10,CONCAT('0',MINUTE(TIMEDIFF(end_time, start_time))),MINUTE(TIMEDIFF(end_time, start_time))),'m ',IF(SECOND(TIMEDIFF(end_time, start_time))<10,CONCAT('0',SECOND(TIMEDIFF(end_time, start_time))),SECOND(TIMEDIFF(end_time, start_time))),'s') IS NULL,\"NA\",CONCAT(IF(HOUR(TIMEDIFF(end_time, start_time))<10,CONCAT('0',HOUR(TIMEDIFF(end_time, start_time))),HOUR(TIMEDIFF(end_time, start_time))),'h ',IF(MINUTE(TIMEDIFF(end_time, start_time))<10,CONCAT('0',MINUTE(TIMEDIFF(end_time, start_time))),MINUTE(TIMEDIFF(end_time, start_time))),'m ',IF(SECOND(TIMEDIFF(end_time, start_time))<10,CONCAT('0',SECOND(TIMEDIFF(end_time, start_time))),SECOND(TIMEDIFF(end_time, start_time))),'s')) duration "
					+ "FROM np_remittance.queued_banks " + "WHERE end_time IS NULL ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> timeoutReport(String mobNum, String status) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT \r\n"
					+ "CONCAT(CONCAT(DAY(DATE(created_on)),' ',SUBSTRING(MONTHNAME(DATE(created_on)),1,3),' ',YEAR(DATE(created_on))),', ',CONCAT(IF(IF(HOUR(TIME(created_on))>12,HOUR(TIME(created_on))-12,HOUR(TIME(created_on)))<10,IF(CONCAT('0',IF(HOUR(TIME(created_on))>12,HOUR(TIME(created_on))-12,HOUR(TIME(created_on))))='00',12,CONCAT('0',IF(HOUR(TIME(created_on))>12,HOUR(TIME(created_on))-12,HOUR(TIME(created_on))))),IF(HOUR(TIME(created_on))>12,HOUR(TIME(created_on))-12,HOUR(TIME(created_on)))),RIGHT(TIME(created_on),6)),' ',IF(HOUR(TIME(created_on))<12,'AM','PM')) `txn_time_initiated`, "
					+ "IF(`remittance_type`='C2A','REMITTANCE',`remittance_type`) txn_type,"
					+ "`remitter_msisdn`,`beneficiary_name`,`beneficiary_bank`,`payment_ref_code`,LEFT(`amount`,LENGTH(amount)-2) amount,`partner`,`status` "
					+ "FROM np_remittance.remittance_outward_table "
					+ "WHERE `csp_code` = (SELECT o.code FROM `master`.`user` AS u JOIN `master`.`user_attribute` AS ua ON u.`id`=ua.`user_id` "
					+ "JOIN `master`.organization o ON u.organization = o.id WHERE ua.`attr_value`='" + mobNum
					+ "' ORDER BY u.id DESC LIMIT 1) AND `status` = '" + status + "' "
					+ "AND (`npci_response_code` = '91' OR `npci_response_code` IS NULL) ORDER BY id DESC";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> refundReport(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT \r\n"
					+ "CONCAT(CONCAT(DAY(DATE(created_on)),' ',SUBSTRING(MONTHNAME(DATE(created_on)),1,3),' ',YEAR(DATE(created_on))),', ',CONCAT(IF(IF(HOUR(TIME(created_on))>12,HOUR(TIME(created_on))-12,HOUR(TIME(created_on)))<10,IF(CONCAT('0',IF(HOUR(TIME(created_on))>12,HOUR(TIME(created_on))-12,HOUR(TIME(created_on))))='00',12,CONCAT('0',IF(HOUR(TIME(created_on))>12,HOUR(TIME(created_on))-12,HOUR(TIME(created_on))))),IF(HOUR(TIME(created_on))>12,HOUR(TIME(created_on))-12,HOUR(TIME(created_on)))),RIGHT(TIME(created_on),6)),' ',IF(HOUR(TIME(created_on))<12,'AM','PM')) `txn_time_initiated`,\r\n"
					+ "IF(`remittance_type`='C2A','REMITTANCE',`remittance_type`) txn_type, "
					+ "`payment_ref_code`, `remitter_msisdn`,`beneficiary_name`,`beneficiary_bank`,`beneficiary_ifsc_code`,"
					+ "`beneficiary_acc_num`,LEFT(amount, LENGTH(amount)-2),LEFT(charge, LENGTH(charge)-2), \"NA\" comm, "
					+ "IF(`refund_completed_on` IS NULL, 'TO_BE_REFUNDED', "
					+ "CONCAT(DAY(DATE(refund_completed_on)),' ',SUBSTRING(MONTHNAME(DATE(refund_completed_on)),1,3),' ',YEAR(DATE(refund_completed_on)))) refunded_date "
					+ "FROM np_remittance.remittance_outward_table "
					+ "WHERE `csp_code` = (SELECT o.code FROM `master`.`user` AS u JOIN `master`.`user_attribute` AS ua "
					+ "ON u.`id`=ua.`user_id` JOIN `master`.organization o ON u.organization = o.id "
					+ "WHERE ua.`attr_value`='" + mobNum + "' ORDER BY u.id DESC LIMIT 1) "
					+ "AND refund_status IS NOT NULL ORDER BY id DESC";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> accountStatementMT(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));

			String query = "SELECT DATE_FORMAT(created_date, '%d-%m-%Y') txn_date, TIME(created_date) txn_time, "
					+ "SUBSTR(RIGHT(`comment`, 11),1,10) ref_no, SUBSTR(RIGHT(`comment`, 22),1,10) msisdn, "
					+ "SUBSTR(`comment`,1,LENGTH(`comment`)-24) description, "
					+ "CONCAT('-',LEFT(amount, LENGTH(amount)-7)) amount, (SELECT CONCAT('-',LEFT(amount, LENGTH(amount)-7)) "
					+ "amount FROM wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id "
					+ "FROM wallet.`m_savings_account` WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
					+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
					+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum + "' "
					+ "ORDER BY id DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1) charge, 'NA' comm "
					+ "FROM wallet.`m_savings_account_transaction` "
					+ "WHERE `savings_account_id` = (SELECT id FROM wallet.`m_savings_account` "
					+ "WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
					+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
					+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum + "' "
					+ "ORDER BY id DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1 OFFSET 1";

			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> accountStatementBV(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));

			String query = "SELECT DATE_FORMAT(created_date, '%d-%m-%Y') txn_date, TIME(created_date) txn_time, "
					+ "SUBSTR(RIGHT(`comment`, 11),1,10) ref_no, SUBSTR(RIGHT(`comment`, 22),1,10) msisdn, "
					+ "SUBSTR(`comment`,1,LENGTH(`comment`)-24) description, "
					+ "CONCAT('-',LEFT(amount, LENGTH(amount)-7)) amount, 'NA' charge, 'NA' comm "
					+ "FROM wallet.`m_savings_account_transaction` "
					+ "WHERE `savings_account_id` = (SELECT id FROM wallet.`m_savings_account` "
					+ "WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
					+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
					+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum + "' "
					+ "ORDER BY id DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1";

			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> accountStatementCMS(String mobNum, String partner) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));

			String query = "";

			if (partner.equalsIgnoreCase("CF") || partner.equalsIgnoreCase("FT")) {
				if (txnDetailsFromIni("GetComm", "").equals("0.0")) {
					query = "SELECT DATE_FORMAT(created_date, '%d-%m-%Y') txn_date, TIME(created_date) txn_time, "
							+ "'NA' ref_no, 'NA' msisdn, `comment` description, CONCAT('-',LEFT(amount, LENGTH(amount)-7)) "
							+ "amount, 'NA' charge, 'NA' comm FROM "
							+ "wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id FROM "
							+ "wallet.`m_savings_account` WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
							+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
							+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
							+ "' ORDER BY id " + "DESC LIMIT 1 OFFSET 1)))) ORDER BY id DESC LIMIT 1";
				} else if (txnDetailsFromIni("GetTds", "").equals("0.0")) {
					query = "SELECT DATE_FORMAT(created_date, '%d-%m-%Y') txn_date, TIME(created_date) txn_time, "
							+ "'NA' ref_no, 'NA' msisdn, `comment` description, CONCAT('-',LEFT(amount, LENGTH(amount)-7)) "
							+ "amount, 'NA' charge, (SELECT REPLACE(REPLACE(RTRIM(REPLACE(amount, "
							+ "'0' ,' ')), ' ','0') + ' ', '. ', '') amount FROM wallet.`m_savings_account_transaction` WHERE "
							+ "`savings_account_id` = (SELECT id FROM wallet.`m_savings_account` WHERE account_no = (SELECT attr_value "
							+ "FROM master.organization_attribute WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT "
							+ "organization FROM master.user WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = "
							+ "'" + mobNum + "' ORDER BY id DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1) comm FROM "
							+ "wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id FROM "
							+ "wallet.`m_savings_account` WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
							+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
							+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
							+ "' ORDER BY id " + "DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1 OFFSET 2";
				} else {
					query = "SELECT DATE_FORMAT(created_date, '%d-%m-%Y') txn_date, TIME(created_date) txn_time, "
							+ "'NA' ref_no, 'NA' msisdn, `comment` description, CONCAT('-',LEFT(amount, LENGTH(amount)-7)) "
							+ "amount, (SELECT CONCAT('-',REPLACE(REPLACE(RTRIM(REPLACE(amount, '0' ,' ')), ' ','0') + ' ', '. ', '')) "
							+ "amount FROM wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id FROM "
							+ "wallet.`m_savings_account` WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
							+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
							+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
							+ "' ORDER BY id "
							+ "DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1 OFFSET 1) charge, (SELECT REPLACE(REPLACE(RTRIM(REPLACE(amount, "
							+ "'0' ,' ')), ' ','0') + ' ', '. ', '') amount FROM wallet.`m_savings_account_transaction` WHERE "
							+ "`savings_account_id` = (SELECT id FROM wallet.`m_savings_account` WHERE account_no = (SELECT attr_value "
							+ "FROM master.organization_attribute WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT "
							+ "organization FROM master.user WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = "
							+ "'" + mobNum + "' ORDER BY id DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1) comm FROM "
							+ "wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id FROM "
							+ "wallet.`m_savings_account` WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
							+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
							+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
							+ "' ORDER BY id " + "DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1 OFFSET 3";
				}
			} else if (partner.equalsIgnoreCase("Swiggy")) {
				if (txnDetailsFromIni("GetComm", "").equals("0.0")) {
					query = "SELECT DATE_FORMAT(created_date, '%d-%m-%Y') txn_date, TIME(created_date) txn_time, "
							+ "'NA' ref_no, 'NA' msisdn, `comment` description, CONCAT('-',LEFT(amount, LENGTH(amount)-7)) "
							+ "amount, 'NA' charge, 'NA' comm FROM "
							+ "wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id FROM "
							+ "wallet.`m_savings_account` WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
							+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
							+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
							+ "' ORDER BY id " + "DESC LIMIT 1 OFFSET 1)))) ORDER BY id DESC LIMIT 3";
				} else if (txnDetailsFromIni("GetTds", "").equals("0.0")) {
					query = "SELECT DATE_FORMAT(created_date, '%d-%m-%Y') txn_date, TIME(created_date) txn_time, "
							+ "'NA' ref_no, 'NA' msisdn, `comment` description, CONCAT('-',LEFT(amount, LENGTH(amount)-7)) "
							+ "amount, 'NA' charge, (SELECT REPLACE(REPLACE(RTRIM(REPLACE(amount, "
							+ "'0' ,' ')), ' ','0') + ' ', '. ', '') amount FROM wallet.`m_savings_account_transaction` WHERE "
							+ "`savings_account_id` = (SELECT id FROM wallet.`m_savings_account` WHERE account_no = (SELECT attr_value "
							+ "FROM master.organization_attribute WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT "
							+ "organization FROM master.user WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = "
							+ "'" + mobNum + "' ORDER BY id DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1) comm FROM "
							+ "wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id FROM "
							+ "wallet.`m_savings_account` WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
							+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
							+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
							+ "' ORDER BY id " + "DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1 OFFSET 4";
				} else {
					query = "SELECT DATE_FORMAT(created_date, '%d-%m-%Y') txn_date, TIME(created_date) txn_time, "
							+ "'NA' ref_no, 'NA' msisdn, `comment` description, CONCAT('-',LEFT(amount, LENGTH(amount)-7)) "
							+ "amount, (SELECT CONCAT('-',REPLACE(REPLACE(RTRIM(REPLACE(amount, '0' ,' ')), ' ','0') + ' ', '. ', '')) "
							+ "amount FROM wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id FROM "
							+ "wallet.`m_savings_account` WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
							+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
							+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
							+ "' ORDER BY id "
							+ "DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1 OFFSET 1) charge, (SELECT REPLACE(REPLACE(RTRIM(REPLACE(amount, "
							+ "'0' ,' ')), ' ','0') + ' ', '. ', '') amount FROM wallet.`m_savings_account_transaction` WHERE "
							+ "`savings_account_id` = (SELECT id FROM wallet.`m_savings_account` WHERE account_no = (SELECT attr_value "
							+ "FROM master.organization_attribute WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT "
							+ "organization FROM master.user WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = "
							+ "'" + mobNum + "' ORDER BY id DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1) comm FROM "
							+ "wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id FROM "
							+ "wallet.`m_savings_account` WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
							+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
							+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
							+ "' ORDER BY id " + "DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1 OFFSET 5";
				}
			}

			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> accountStatementAEPS(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT DATE_FORMAT(created_date, '%d-%m-%Y') `date`, (SELECT TIME(created_date) "
					+ "FROM wallet.`m_savings_account_transaction` WHERE `savings_account_id` = (SELECT id "
					+ "FROM wallet.`m_savings_account` WHERE account_no = (SELECT attr_value "
					+ "FROM master.organization_attribute WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' "
					+ "AND orgnization_id = (SELECT organization FROM master.user WHERE id = (SELECT user_id "
					+ "FROM master.user_attribute WHERE attr_value = '" + mobNum
					+ "' ORDER BY id DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1) `time`, aeps.novopay_txn_ref, "
					+ getAEPSMobNum("GetAEPSMobNum")
					+ ", 'Debit Agent Wallet for Deposit transaction with identifier: XXXX XXXX "
					+ getAadhaarFromIni("GetAadhaarNum").substring(8, 12)
					+ "',CONCAT('-',LEFT(txn_amount, LENGTH(txn_amount)-2)) amount, '"
					+ (txnDetailsFromIni("GetTds", "").equalsIgnoreCase(".00") ? "NA" : txnDetailsFromIni("GetTds", ""))
					+ "' tds, '"
					+ (txnDetailsFromIni("GetComm", "").equalsIgnoreCase("0.00") ? "NA"
							: txnDetailsFromIni("GetComm", ""))
					+ "' comm FROM `np_aepstxn`.`aeps_transactions` aeps ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String closingBalance(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT LEFT(running_balance_derived, LENGTH(running_balance_derived)-7) balance "
					+ "FROM wallet.`m_savings_account_transaction` "
					+ "WHERE `savings_account_id` = (SELECT id FROM wallet.`m_savings_account` "
					+ "WHERE account_no = (SELECT attr_value FROM master.organization_attribute "
					+ "WHERE attr_key = 'WALLET_ACCOUNT_NUMBER' AND orgnization_id = (SELECT organization FROM master.user "
					+ "WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_value = '" + mobNum
					+ "' ORDER BY id DESC LIMIT 1)))) ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> aepsStatusEnquiry(String txnRefNo) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT DATE_FORMAT(updated_date, '%b %d, %Y %l:%i:%s %p') txn_date, novopay_txn_ref, "
					+ "txn_type, (SELECT `value` FROM np_actor.platform_master_data WHERE `code` = "
					+ "(SELECT bank_code FROM np_aepstxn.aeps_transactions ORDER BY id DESC LIMIT 1) "
					+ "AND data_sub_type = (SELECT txn_type FROM np_aepstxn.aeps_transactions WHERE novopay_txn_ref = '"
					+ txnRefNo + "') AND partner_code = 'RBL') bank, LEFT(txn_amount, LENGTH(txn_amount)-2) amount, "
					+ "IF(`status`='FAIL' AND refund_status='COMPLETED','REFUNDED',IF(`status`='SUSPECT','SUCCESS',"
					+ "IF(refund_status = 'ELIGIBLE','Ready For Refund',`status`))) `status` "
					+ "FROM np_aepstxn.aeps_transactions WHERE novopay_txn_ref = '" + txnRefNo + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public List<String[]> mtStatusEnquiry(String txnRefNo) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			if (txnRefNo == "-") {
				txnRefNo = paymentRefCode("all");
			}
			String query = "SELECT DATE_FORMAT(created_on, '%e %b %Y, %h:%i:%s %p') `date`, 'Remittance' `type`, "
					+ "payment_ref_code, LEFT(amount, LENGTH(amount)-2) amount, beneficiary_name, beneficiary_bank, "
					+ "beneficiary_ifsc_code, beneficiary_acc_num, DATE_FORMAT(last_updated_on, '%e %b %Y, %h:%i:%s %p') "
					+ "FROM `np_remittance`.`remittance_outward_table` WHERE payment_ref_code = '" + txnRefNo + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String getMobNumFromOrgCode() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT attr_value FROM master.user_attribute WHERE user_id = (SELECT id "
					+ "FROM master.user WHERE organization = (SELECT id FROM master.organization "
					+ "WHERE `code` = (SELECT agent_org_code FROM `np_aepstxn`.`aeps_transactions` "
					+ "ORDER BY id DESC LIMIT 1))) AND attr_key = 'MSISDN'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String aepsStatusEnquiryDate(String txnRefNo) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT DATE_FORMAT(updated_date, '%d/%m') txn_date FROM np_aepstxn.aeps_transactions WHERE novopay_txn_ref = '"
					+ txnRefNo + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String deleteAEPSTxn() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("rblSimulator"));
			String query = "DELETE FROM `limit_charges`.`entity_consumed_limits` WHERE entity_id = '0fa44628d16028a273781693d392256e502e66ccd6b6a102def50fd729457855bcb8a44fcde70ddd3d4afeeff82d1e9a6348d2de7fe5ed5b9eee3a386cdcf2e0';";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public String aepsTxnDate() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT DATE_FORMAT(updated_date, '%d-%b-%Y %h:%i %p') FROM `np_aepstxn`.`aeps_transactions` ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String aepsRefNum() throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT novopay_txn_ref FROM `np_aepstxn`.`aeps_transactions` ORDER BY id DESC LIMIT 1";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String updateAEPSTxn(String txnRef) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("master"));
			String query = "UPDATE np_aepstxn.aeps_transactions SET `status` = 'FAIL', partner_txn_status = 'FAIL',"
					+ " partner_txn_status_code = 'M3', refund_status = 'ELIGIBLE' WHERE novopay_txn_ref = '" + txnRef
					+ "';";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB!! BC Agent ID update  failed..!");
			sqe.printStackTrace();

		}
		return null;
	}

	public List<String[]> saturdayDate(String firstDate) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT DATE_ADD((" + firstDate + " + INTERVAL -(SELECT DATE_FORMAT(" + firstDate
					+ ", '%d') - 1) DAY), INTERVAL(( 7 - DAYOFWEEK(" + firstDate + " + INTERVAL -(SELECT DATE_FORMAT("
					+ firstDate + ", '%d') - 1) DAY) ) % 7 ) + 7 DAY) AS saturday_of_month\r\n" + "UNION\r\n"
					+ "SELECT DATE_ADD((" + firstDate + " + INTERVAL -(SELECT DATE_FORMAT(" + firstDate
					+ ", '%d') - 1) DAY), INTERVAL(( 7 - DAYOFWEEK(" + firstDate + " + INTERVAL -(SELECT DATE_FORMAT("
					+ firstDate + ", '%d') - 1) DAY) ) % 7 ) + 21 DAY) AS saturday_of_month";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int nCol = rs.getMetaData().getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = rs.getObject(iCol);
					row[iCol - 1] = (obj == null) ? "NA" : obj.toString();
				}
				list.add(row);
			}
			return list;
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String updateOrgSettlementInfo(String mode, String status, String enabled, String remarks, String mobNum)
			throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("master"));
			String query = "UPDATE master.`org_stlmnt_info` SET settlement_mode = '" + mode + "', `status` = '" + status
					+ "', `enabled` = '" + enabled + "', blocking_remarks = '" + remarks
					+ "' WHERE `organization_id` = (SELECT organization FROM master.user WHERE id = (SELECT user_id FROM master.user_attribute WHERE attr_key = 'MSISDN' AND attr_value = '"
					+ mobNum + "') AND `status` = 'ACTIVE') ORDER BY id DESC LIMIT 1;";
			System.out.println(query);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB!! BC Agent ID update  failed..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public String beneName(String accNum, String ifscCode) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("npActor"));
			String query = "SELECT account_holder_name FROM np_subscriber.validated_beneficiary WHERE ifsc_code = '"
					+ ifscCode + "' AND account_number = '" + accNum + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB..!");
			sqe.printStackTrace();
		}
		return null;
	}

	public void modifyContract(String contrct, String mobNum) throws ClassNotFoundException {
		String contract = contrct;
		if (contrct.equalsIgnoreCase("FINO | RBL")) {
			contract = "FINO";
		}
		try {
			conn = createConnection(configProperties.get("master"));
			String deleteQuery = "DELETE FROM master.contract WHERE organization = (SELECT u.`organization` FROM `master`.`user` u "
					+ "JOIN `master`.`user_attribute` ua ON u.`id`=ua.`user_id` WHERE ua.`attr_value`='" + mobNum + "' "
					+ "AND u.status = 'ACTIVE')";

			String insertQuery1 = "INSERT INTO `contract` (`organization`, `partner_organization`) "
					+ "VALUES((SELECT u.`organization` FROM `master`.`user` u JOIN `master`.`user_attribute` ua "
					+ "ON u.`id`=ua.`user_id` WHERE ua.`attr_value`='" + mobNum + "' "
					+ "AND u.status = 'ACTIVE'),(SELECT id " + "FROM master.organization WHERE `CODE` = 'rbl'));";
			String insertQuery2 = "INSERT INTO `contract` (`organization`, `partner_organization`) "
					+ "VALUES((SELECT u.`organization` FROM `master`.`user` u JOIN `master`.`user_attribute` ua "
					+ "ON u.`id`=ua.`user_id` WHERE ua.`attr_value`='" + mobNum + "' "
					+ "AND u.status = 'ACTIVE'),(SELECT id " + "FROM master.organization WHERE `CODE` = '" + contract
					+ "'));";

			stmt = conn.createStatement();
			stmt.executeUpdate(deleteQuery);
			System.out.println("Deleting all contracts");
			stmt.executeUpdate(insertQuery1);
			stmt.executeUpdate(insertQuery2);
			System.out.println("Inserting " + contract.toLowerCase() + " along with rbl");
		} catch (SQLException sqe) {
			System.out.println("Duplicate entry for " + contract);
		}
	}

	public void insertContract(String mobNum) throws ClassNotFoundException {
		try {
			conn = createConnection(configProperties.get("master"));
			stmt = conn.createStatement();

			List<String> org_code = new ArrayList<String>();
			org_code.add("rbl");
			org_code.add("ybl");
			org_code.add("paytm");
			org_code.add("fino");
			org_code.add("cms");
			org_code.add("billpay");
			org_code.add("recharges");
			org_code.add("wallet");
			org_code.add("pg-wallet");
			org_code.add("gold");

			for (String code : org_code) {
				String insertQuery = "INSERT INTO `contract` (`organization`, `partner_organization`) "
						+ "VALUES((SELECT u.`organization` FROM `master`.`user` u JOIN `master`.`user_attribute` ua "
						+ "ON u.`id`=ua.`user_id` WHERE ua.`attr_value`='" + mobNum + "' AND u.status "
						+ "= 'ACTIVE'),(SELECT id FROM master.organization WHERE `CODE` = '" + code + "'));";
				try {
					stmt.executeUpdate(insertQuery);
				} catch (Exception e) {
					System.out.println("Duplicate entry for " + code);
				}
			}
		} catch (SQLException sqe) {
			System.out.println("Error connecting DB!! BC Agent ID update  failed..!");
			sqe.printStackTrace();
		}
	}
}
