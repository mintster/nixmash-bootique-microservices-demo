package com.nixmash.userservice.resource;

import com.google.inject.Inject;
import com.nixmash.jangles.json.ServiceDescription;
import com.nixmash.userservice.core.UserServiceConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GeneralResource {

    public UserServiceConfig userServiceConfig;

	@Inject
	public GeneralResource(UserServiceConfig userServiceConfig) {
		this.userServiceConfig = userServiceConfig;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceDescription getServiceInfo() {
		return new ServiceDescription(
				userServiceConfig.applicationId, userServiceConfig.applicationDescription
		);
	}
}
