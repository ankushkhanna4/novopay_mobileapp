package in.novopay.platform_ui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.testng.Reporter;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

//import batchAutomation.CMSSettlementEODFlow;

public class ServerUtils {
	DBUtils dbUtils = new DBUtils();
	JavaUtils javaUtils = new JavaUtils();
	JSch jsch;
	Session session;
	private String usr;
	private String password;
	public static String skipCause = null;

	public Session connectServer(HashMap<String, String> configuration) {
		if ((null != session) && (session.isConnected())
				&& (session.getHost().equalsIgnoreCase(configuration.get("server.host.ip")))) {
			return session;
		} else {
			try {
				jsch = new JSch();
				usr = configuration.get("server.username");
				password = configuration.get("server.password");
				String host = configuration.get("server.host.ip");
				int port = Integer.parseInt(configuration.get("server.host.port"));
				System.out.println("User: " + usr + "\nPassword: " + password + "\nHost: " + host + "\nPort:" + port);
				session = jsch.getSession(usr, host, port);
				session.setPassword(password);
				Hashtable<String, String> config = new Hashtable<String, String>();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				Reporter.log("Connecting server...", true);
				session.connect();
				session.setServerAliveInterval(15000);
				Reporter.log("Connection established...", true);
				return session;
			} catch (JSchException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public boolean closeConnection() {
		session.disconnect();
		System.out.println("exit");
		return true;
	}

	public boolean redeployWar(Session currSession) {
		int deploySeq = 0;
		if (currSession.isConnected()) {
			try {
				ChannelExec sudoUser = (ChannelExec) session.openChannel("exec");
				String cmd = "su - tomcat;cd /apps/portal-tomcat/apache-tomcat-7.0.42/webapps;rm -rf batch-novopay; tail -f /apps/portal-tomcat/apache-tomcat-7.0.42/logs/catalina.out";
				sudoUser.setCommand(cmd);
				sudoUser.connect();
				InputStream m_in = sudoUser.getInputStream();
				sudoUser.connect();
				BufferedReader m_bufferedReader = new BufferedReader(new InputStreamReader(m_in));
				while (true) {
					if (m_bufferedReader.ready()) {
						String line = m_bufferedReader.readLine();
						System.out.println(line);
						if (line.toLowerCase().contains("undeploying")) {
							Reporter.log("\n\n\n\n\n~~~~~~Server undeloyed~~~~~~", true);
							deploySeq = 1;
							Thread.sleep(1000);
						} else if ((deploySeq == 1) && (line.toLowerCase().contains("deploying"))) {
							Reporter.log("\n\n\n\n\n~~~~~~~~Server deloying~~~~~~~~", true);
							deploySeq = 2;
							Thread.sleep(1000);
						} else if ((deploySeq == 2) && (line.toLowerCase()
								.contains("registering current configuration as safe fallback point"))) {
							Reporter.log("\n\n\n\n\n~~~~~~~~Server deloyed~~~~~~~~", true);
							deploySeq = 3;
							break;
						}
					}
				}
				sudoUser.disconnect();
				return true;
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			Reporter.log("Inactive session, please provide alive session.", true);
		}
		return false;
	}

	public String tailLogs(Session session, String batchExeSummary, String orgId, HashMap<String, String> batchConfig) {
		String line;
		if ((null != jsch) && (null != session)) {
			ChannelExec m_channelExec;
			try {
				System.out.println("Starting tailing logs.");
				m_channelExec = (ChannelExec) session.openChannel("exec");
				String cmd = "tailf /apps/applogs/batch-novopay.log";
				if (batchConfig.get("batch.type").equalsIgnoreCase("remittance")) {
					cmd = "tailf /apps/applogs/batch-remittance.log";
					System.out.println("Trailing batch remittaance log");
				} else if (batchConfig.get("batch.type").equalsIgnoreCase("platform")) {
					cmd = "tailf /apps/applogs/batch-novopay-platform.log";
					System.out.println("Trailing batch novopay platform log");
				}

				System.out.println("batchExeSummary :: " + batchExeSummary);
				m_channelExec.setCommand(cmd);
				InputStream m_in = m_channelExec.getInputStream();
				m_channelExec.connect();
				BufferedReader m_bufferedReader = new BufferedReader(new InputStreamReader(m_in));
				Pattern p = Pattern.compile(batchExeSummary);
				while (true) {
					if (m_bufferedReader.ready()) {
						line = m_bufferedReader.readLine();
						System.out.println(line);
						// CMSSettlementEODFlow.log.info(line);

						Matcher m = p.matcher(line);

						if (line.contains(orgId + " ::")) {
							skipCause = line;
						}
						if (m.find()) {
							System.out.println("--------settlement done---------");
							break;
						}
					}
				}

				System.out.println("Stoped tailing logs");
				m_channelExec.disconnect();
				session.disconnect();
				return line;
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Inactive session.");
		}
		return null;
	}

	public String getH2HFile(Session session, String path) {
		// getH2HFilePath
		if ((null != jsch) && (null != session)) {
			ChannelSftp m_channelSftp;
			try {
				System.out.println("Fetching H2H file contents.");
				m_channelSftp = (ChannelSftp) session.openChannel("sftp");
				m_channelSftp.connect();
				InputStream stream = m_channelSftp.get(path);
				String line = IOUtils.toString(stream);
				System.out.println(line);
				return line;
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (SftpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void uploadFile(HashMap<String, String> batchConfig, String status) {
		session = connectServer(JavaUtils.configProperties);
		System.out.println();
		uploadFile(session, batchConfig.get("testdata.dir") + batchConfig.get("batch.file." + status.toLowerCase()),
				batchConfig.get("batch.destination.file.name"), batchConfig.get("batch.destination.tmp.dir"),
				batchConfig.get("batch.destination.config.dir"));
	}

	public boolean uploadFile(Session session, String source, String destFileName, String tmpDir, String configDir) {
		if ((null != jsch) && (null != session)) {
			ChannelSftp m_channelSftp;
			ChannelExec m_channelExec;
			try {
				File file = new File(source);
				if (!file.exists()) {
					System.out.println("Source file doesn't exists");
				}
				FileInputStream fin = new FileInputStream(file);
				m_channelSftp = (ChannelSftp) session.openChannel("sftp");
				m_channelSftp.connect();
				m_channelSftp.put(fin, tmpDir + destFileName, ChannelSftp.OVERWRITE);
				m_channelSftp.exit();
				m_channelExec = (ChannelExec) session.openChannel("exec");
				String cmd = "sudo -H -u tomcat bash -c 'cp -rf " + tmpDir + destFileName + " /apps/appconfig/'";
				System.out.println(cmd);
				m_channelExec.setCommand(cmd);
				m_channelExec.connect();
				m_channelExec.disconnect();
				return true;
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (SftpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}


	public boolean runCommand(String command) {

		return false;
	}

}
