package com.nixmash.postservice.resource;

import com.google.inject.Inject;
import com.nixmash.jangles.json.ServiceDescription;
import com.nixmash.postservice.core.PostServiceConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GeneralResource {

    public PostServiceConfig postServiceConfig;

	@Inject
	public GeneralResource(PostServiceConfig postServiceConfig) {
		this.postServiceConfig = postServiceConfig;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceDescription getServiceInfo() {
		return new ServiceDescription(
				postServiceConfig.applicationId, postServiceConfig.applicationDescription
		);
	}
}
