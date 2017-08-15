package com.nixmash.jangles.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nixmash.jangles.db.JanglesConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.List;

@Singleton
@XmlRootElement(name = "connections")
@XmlAccessorType(XmlAccessType.FIELD)
public class JanglesConnections {

	private static final Logger logger = LoggerFactory.getLogger(JanglesConnections.class);

	JanglesCache janglesCache;
	JanglesConfiguration janglesConfiguration;
	JanglesGlobals janglesGlobals;

	public JanglesConnections() {
	}

	@Inject
	public JanglesConnections(JanglesCache janglesCache, JanglesConfiguration janglesConfiguration, JanglesGlobals janglesGlobals) {
		this.janglesCache = janglesCache;
		this.janglesConfiguration = janglesConfiguration;
		this.janglesGlobals = janglesGlobals;
	}

	@XmlElement(name = "connection")
	private List<JanglesConnection> connections = null;

	public List<JanglesConnection> getConnections() {
		return connections;
	}

	public void setConnections(List<JanglesConnection> connections) {
		this.connections = connections;
	}

	public JanglesConnection getMySqlConnection() {
		return getConnection
                (janglesConfiguration.mysqlDbConnectionName);
	}

	public JanglesConnection getTestConnection() {
		return getConnection
				(janglesConfiguration.testDbConnectionName);
	}

	@SuppressWarnings({"ConstantConditions", "Duplicates"})
	private JanglesConnection getConnection(String name) {

		JanglesConnections janglesConnections = null;
		JanglesConnection currentConnection = null;
		
		String cacheKey = connectionCacheKey(name);
		currentConnection = (JanglesConnection) janglesCache.get(cacheKey);
		if (currentConnection == null) {
			try {
				JAXBContext jc = JAXBContext.newInstance(JanglesConnections.class);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				File xml = new File(janglesConfiguration.connectionXmlPath);
				janglesConnections = (JanglesConnections) unmarshaller.unmarshal(xml);
			} catch (JAXBException e) {
				e.printStackTrace();
			}

			currentConnection = janglesConnections.getConnections().stream()
					.filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().get();

			janglesCache.put(cacheKey, currentConnection);
		}
		return currentConnection;
	}
	

	private String connectionCacheKey(String name) {
		return String.format("JanglesConnection-%s-%s", janglesGlobals.cloudApplicationId, name);
	}

	public void clearOutputConnectionCache()
	{
		clearConnectionCache(janglesConfiguration.mysqlDbConnectionName);
	}
	
	public void clearInputConnectionCache()
	{
		clearConnectionCache(janglesConfiguration.mysqlDbConnectionName);
	}
	
	private void clearConnectionCache(String name) {
		try {
			janglesCache.remove(connectionCacheKey(name));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
