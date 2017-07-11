package com.nixmash.userservice.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
public class UserServiceGlobals implements java.io.Serializable {

	private static final long serialVersionUID = -5262833103399133397L;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceGlobals.class);

	public String cloudApplicationId;
	public String apiKeyErrorMessage;
	public String apiKey;

	UserServiceConfig userServiceConfig;

	@Inject
	public UserServiceGlobals(UserServiceConfig userServiceConfig) {
		this.userServiceConfig = userServiceConfig;

		Properties properties = new Properties();
		InputStream input = null;
	 
		try {

			input = new FileInputStream(userServiceConfig.globalPropertiesFile);
			properties.load(input);
			this.cloudApplicationId = properties.getProperty("application.cloud.id");
			this.apiKeyErrorMessage = properties.getProperty("api.key.error.message");
			this.apiKey= properties.getProperty("api.key");

		} catch (IOException ex) {
			logger.error(ex.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}



}
