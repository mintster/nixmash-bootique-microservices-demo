package com.nixmash.postservice.resource;

import com.google.inject.Inject;
import com.nixmash.jangles.auth.BearerAuthenticationToken;
import com.nixmash.jangles.json.ServiceDescription;
import com.nixmash.jangles.service.UserService;
import com.nixmash.postservice.core.UserServiceConfig;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GeneralResource {

    private final UserServiceConfig userServiceConfig;
    private final UserService userService;
	private final SecurityManager securityManager;


	@Inject
	public GeneralResource(UserServiceConfig userServiceConfig,
                           UserService userService, SecurityManager securityManager) {
		this.userServiceConfig = userServiceConfig;
		this.userService = userService;
		this.securityManager = securityManager;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceDescription getServiceInfo() {
		return new ServiceDescription(
				userServiceConfig.applicationId, userServiceConfig.applicationDescription
		);
	}

	@GET
	@Path("/token/{userToken}")
	@Produces(MediaType.TEXT_PLAIN)
	public String doTokenCheck(@PathParam("userToken") BearerAuthenticationToken token) {
		if (userService.isAnonymousToken(token)) {
			return "Please login to determine strength of your token";
		} else {
			return isAdmin(token) ? "You can perform this task, Bob" :
					"You do not have permission to perform this task, Ken";
		}
	}

	private Boolean isAdmin(BearerAuthenticationToken token) {
		Boolean isAdmin = false;
		try {
			Subject subject = new Subject.Builder(securityManager).buildSubject();
			subject.login(token);
			isAdmin = subject.hasRole("admin");
			subject.logout();
			return isAdmin;
		} catch (AuthenticationException e) {
			return false;
		}
	}
}
