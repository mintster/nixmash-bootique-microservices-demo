package com.nixmash.postservice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.jangles.auth.BearerTokenRealm;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.db.cn.MySqlConnection;
import com.nixmash.postservice.resource.GeneralResource;
import com.nixmash.postservice.resource.UserResource;
import io.bootique.Bootique;
import io.bootique.jersey.JerseyModule;
import io.bootique.shiro.ShiroModule;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by daveburke on 6/13/17.
 */
public class Launcher implements Module  {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    //@formatter:off

    public static void main(String[] args) {

        Bootique
                .app(args)
                .autoLoadModules()
                .module(Launcher.class)
                .args("--server", "--config=classpath:bootique.yml")
				.exec();
    }

    //@formatter:on

	@Override
	public void configure(Binder binder) {

		JerseyModule.extend(binder)
				.addResource(UserResource.class)
				.addResource(GeneralResource.class)
				.addFeature(DeclarativeLinkingFeature.class);

		binder.bind(IConnection.class).to(MySqlConnection.class);
		ShiroModule.extend(binder).addRealm(BearerTokenRealm.class);

	}

}
