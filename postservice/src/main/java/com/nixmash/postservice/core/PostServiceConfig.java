package com.nixmash.postservice.core;

import com.google.inject.Singleton;
import com.nixmash.jangles.utils.JanglesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@Singleton
public class PostServiceConfig implements Serializable {

	// region properties

	private static final Logger logger = LoggerFactory.getLogger(PostServiceConfig.class);
	private static final long serialVersionUID = 6545927489552139959L;

	public String applicationId;
	public String globalPropertiesFile;
	public String applicationDescription;

    // endregion

	// region get()

	public PostServiceConfig() {

		Properties properties = new Properties();

		try {
			String propertiesFile = !JanglesUtils.isInTestingMode() ? "postservice" : "test";
			properties.load(getClass().getResourceAsStream(String.format("/%s.properties", propertiesFile)));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		String user_home = System.getProperty("user.home");
		this.applicationId = properties.getProperty("application.id");
		this.applicationDescription = properties.getProperty("application.description");
		this.globalPropertiesFile = user_home + properties.getProperty("application.global.properties.file");

	}

	// endregion

}
