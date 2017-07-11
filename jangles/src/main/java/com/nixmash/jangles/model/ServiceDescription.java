package com.nixmash.jangles.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.glassfish.jersey.linking.InjectLink;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.sql.Timestamp;

import static org.glassfish.jersey.linking.InjectLink.Style.ABSOLUTE;

@XmlRootElement()
@JsonPropertyOrder({ "applicationId", "serviceName", "lastUpdated", "users"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceDescription implements Serializable {

    // region Constructors

    public ServiceDescription() {
    }

    public ServiceDescription(String applicationId, String serviceName) {
        this.applicationId = applicationId;
        this.serviceName = serviceName;
    }

    // endregion

    // region properties

    private String applicationId;
    private String serviceName;

    @InjectLink(value = "/users", style = ABSOLUTE, rel="users")
    @JsonProperty(value = "users")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private Link usersUri;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    private Timestamp lastUpdated;

    // endregion

    // region getters/setters

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Link getUsersUri() {
        return usersUri;
    }

    public void setUsersUri(Link usersUri) {
        this.usersUri = usersUri;
    }


    // endregion

}


	
