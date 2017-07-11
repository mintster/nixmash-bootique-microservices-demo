package com.nixmash.jangles.core;

import com.google.inject.Singleton;
import com.nixmash.jangles.utils.JanglesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

@Singleton
public class JanglesConfiguration implements java.io.Serializable {


	// region properties

	private static final Logger logger = LoggerFactory.getLogger(JanglesConfiguration.class);
	private static final long serialVersionUID = -720348471534321068L;

	public String applicationId;
	public String mysqlDbConnectionName;
	public String testDbConnectionName;
	public String connectionXmlPath;
	public String globalPropertiesFile;
	public String currentLocale;

    // endregion

	public JanglesConfiguration() {

		Properties properties = new Properties();

		try {
			String propertiesFile = !JanglesUtils.isInTestingMode() ? "jangles" : "test";
			properties.load(getClass().getResourceAsStream(String.format("/%s.properties", propertiesFile)));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		String user_home = System.getProperty("user.home");
		this.applicationId = properties.getProperty("application.id");
		this.mysqlDbConnectionName = properties.getProperty("mysql.db.connection");
		this.testDbConnectionName = properties.getProperty("testing.db.connection");
		this.connectionXmlPath = user_home + properties.getProperty("connection.xml.path");
		this.globalPropertiesFile = user_home + properties.getProperty("application.global.properties.file");
		this.currentLocale = properties.getProperty("application.currentLocale");

	}

	// endregion
}
