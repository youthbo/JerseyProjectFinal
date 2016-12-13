package se.plushogskolan.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.plushogskolan.resource.UserResource;
import se.plushogskolan.resource.WorkItemResource;

@Component
public class JerseyConfig extends ResourceConfig{
        public JerseyConfig() {

			register(UserResource.class);
			register(WorkItemResource.class);
		}
}
