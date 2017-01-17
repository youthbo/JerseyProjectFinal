package se.plushogskolan.resource;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;

import se.plushogskolan.model.User;
import se.plushogskolan.service.UserService;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class CustomerRequestFilter implements ContainerRequestFilter {
	@Autowired
	UserService userService;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		final Logger log = Logger.getLogger(CustomerRequestFilter.class.getName());
		log.info("in request filter");
		if (!requestContext.getHeaders().containsKey("Authorization")) {
			log.warning("Authentication token misses.");
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Användaren behöver autentisera sig igen.").build());
			return;
		}

		String auth_token = requestContext.getHeaderString("Authorization");
		if (!auth_token.contains("Bearer")){
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Användaren behöver autentisera sig igen.").build());
			return;
		}
		
		auth_token = auth_token.substring("Bearer".length()).trim();	
		User user = userService.getUserByToken(auth_token);
		
		if (user == null) {
			log.info("Authentication token is: " + auth_token);
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Användaren behöver autentisera sig igen.").build());
			return;
		}
		String expirationTime = user.getExpirationTime();
		if (Long.parseLong(expirationTime) < System.currentTimeMillis()){
			log.info("Token is expired.");
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Användaren behöver autentisera sig igen.").build());
			return;
		}

	}

}
