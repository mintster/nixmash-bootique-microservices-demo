package com.nixmash.jangles.json;

import com.fasterxml.jackson.annotation.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.net.URI;
import java.sql.Timestamp;

import static org.glassfish.jersey.linking.InjectLink.Style.ABSOLUTE;

@XmlRootElement()
@JsonPropertyOrder({"userId", "userName", "displayName", "dateCreated", "isActive", "link", "users"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonUser implements Serializable {

    private static final long serialVersionUID = 6883971373572420433L;

    // region Constructors

    public JsonUser() {
    }

    // endregion

    // region properties

    @InjectLink(value = "/users", style = ABSOLUTE, condition = "${instance.showUsersLink}", rel="parent")
    @JsonProperty(value = "users")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private Link usersUri;

    @InjectLink(value = "/", style = ABSOLUTE, condition = "${instance.showUsersLink}", rel="home")
    @JsonProperty(value = "home")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private Link home;

    @InjectLink(value = "/users/{userId}", style = ABSOLUTE, bindings = {
            @Binding(name = "id", value = "${resource.userId}")})
    @JsonProperty(value = "link")
    private URI link;

    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;

    @JsonIgnore
    private String password;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    private Timestamp dateCreated;
    private Boolean isActive = true;

    @JsonIgnore
    private Boolean showUsersLink = false;

    // endregion

    // region getters/setters


    // endregion


}


	
