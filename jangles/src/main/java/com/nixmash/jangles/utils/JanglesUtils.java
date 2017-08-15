package com.nixmash.jangles.utils;

import com.nixmash.jangles.core.JanglesConfiguration;
import com.nixmash.jangles.core.JanglesConnections;
import com.nixmash.jangles.db.JanglesConnection;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class JanglesUtils {

	public static String pluralize(String singular) {
		String plural = singular;
		int singularLength = StringUtils.length(singular);
		if (StringUtils.right(singular, 1) == "y")
			plural = StringUtils.left(singular, singularLength - 1) + "ies";
		else
			plural = singular + "s";
		return plural;
	}

	public static String lowerPluralize(String singular) {
		return StringUtils.uncapitalize(pluralize(singular));
	}

	public static boolean isInTestingMode() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		List<StackTraceElement> list = Arrays.asList(stackTrace);
		for (StackTraceElement element : list) {
			if (element.getClassName().startsWith("org.junit.")) {
				return true;
			}
		}
		return false;
	}

	public static void configureTestDb(String sql) throws FileNotFoundException, SQLException {
		Properties properties = new Properties();
		ClassLoader classLoader = JanglesUtils.class.getClassLoader();
		InputStream inputStream =
				classLoader.getResourceAsStream("test.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JanglesConnection janglesConnection = getTestConnection();
		String url = janglesConnection.getUrl();
		String dbuser = janglesConnection.getUsername();
		String dbpassword = janglesConnection.getPassword();
		Connection conn = DriverManager.getConnection(url,dbuser, dbpassword);
		Statement st = conn.createStatement();
		File script = new File(classLoader.getResource(sql).getFile());
		ScriptRunner sr = new ScriptRunner(conn);
		sr.setLogWriter(null);
		Reader reader = new BufferedReader(new FileReader(script));
		sr.runScript(reader);

	}

	@SuppressWarnings({"Duplicates", "ConstantConditions"})
	private static JanglesConnection getTestConnection() {
		JanglesConfiguration janglesConfiguration = new JanglesConfiguration();
		JanglesConnections janglesConnections = null;

			try {
				JAXBContext jc = JAXBContext.newInstance(JanglesConnections.class);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				File xml = new File(janglesConfiguration.connectionXmlPath);
				janglesConnections = (JanglesConnections) unmarshaller.unmarshal(xml);
			} catch (JAXBException e) {
				e.printStackTrace();
			}

			return janglesConnections.getConnections().stream()
					.filter(s -> s
							.getName()
							.equalsIgnoreCase(janglesConfiguration.testDbConnectionName))
					.findFirst()
					.get();

	}

	public static void showAllProperties() {
		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			System.out.format("%s=%s%n", envName, env.get(envName));
		}

		Properties systemProperties = System.getProperties();
		Enumeration<?> enuProp = systemProperties.propertyNames();
		while (enuProp.hasMoreElements()) {
			String propertyName = (String) enuProp.nextElement();
			String propertyValue = systemProperties.getProperty(propertyName);
			System.out.println(propertyName + ": " + propertyValue);
		}
	}

}
