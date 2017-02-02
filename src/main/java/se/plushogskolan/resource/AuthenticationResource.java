package se.plushogskolan.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.plushogskolan.jersey.model.Credentials;
import se.plushogskolan.jersey.model.Token;
import se.plushogskolan.model.User;
import se.plushogskolan.service.UserService;

@Component
@Path("auth")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
	
	@Autowired
	UserService userService;
	
	@POST
	public Response authentication(Credentials credentials) {
		
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		User  user = userService.getUserByUsername(username);
		if (user==null){
			return Response.status(Status.UNAUTHORIZED).entity("User doesn't exist.").build();
		}
		
		String storedSalt = user.getSalt();
		String storedPassword = user.getPassword();
		String hashedPassword = user.hashPassword(password.toCharArray(),Base64.decodeBase64(storedSalt));
		
		if(storedPassword.equals(hashedPassword)){
			
			Token token  = new Token();
			user.setToken(token.getAccess_token());
			user.setExpirationTime(token.getExpiration_time());
			userService.updateUser(user);
			return Response.ok(token).build();
		}
		
		return Response.status(Status.UNAUTHORIZED).build();
	}

}
