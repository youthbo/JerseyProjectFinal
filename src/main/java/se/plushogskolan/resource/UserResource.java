package se.plushogskolan.resource;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.plushogskolan.model.Team;
import se.plushogskolan.model.User;
import se.plushogskolan.service.TeamService;
import se.plushogskolan.service.UserService;
import com.google.gson.Gson;

@Component
@Path("users")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Consumes({ MediaType.APPLICATION_JSON })
public final class UserResource {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;

	@Context
	private UriInfo uriInfo;

	/**
	 * @param size
	 *  
	 * uri: .../users
	 * 
	 * man får skicka "teamname" i http json body:n. Om redan finns ett team med det här namnet 
	 * hämtas teamet och skickas sätts till user. Om inget team mead det här namnet finns då
	 * skapas ett team med namnet och sätts till user.
	 * 
	 */
	@POST
	public Response addUser(String body) {

		Gson gson = new Gson();
		Map m = gson.fromJson(body, Map.class);
		String teamname = (String) m.get("teamname");
		Team team = teamService.findByName(teamname);
		if (team == null) {
			team = new Team(teamname);
		}
		User user = gson.fromJson(body, User.class);
		user.generateUsernumber();
		user.setTeam(team);
		userService.createUser(user);
//		URI location = uriInfo.getAbsolutePathBuilder().path(user.getId().toString()).build();
		URI location = uriInfo.getAbsolutePathBuilder().path(UserResource.class, "getUser").build(user.getId());
		return Response.created(location).build();
	}

	/**
	 * 
	 * @param page
	 * @param size
	 * 
	 * uri: .../users
	 * 
	 * hämtar alla users
	 */
	@GET
	public Response getAllUsers(@QueryParam("page") @DefaultValue("0") int page,
								@QueryParam("size") @DefaultValue("10") int size) {

		List<User> users = userService.findAllUsers(page, size);
//		customers = customers.subList(0, Math.min(customers.size(), size));
//		customers.sort((c1, c2) -> sort.equalsIgnoreCase("desc") ? Long.compare(c1.getId(), c2.getId())
//				: Long.compare(c2.getId(), c1.getId()));

		return Response.ok(users).build();
	 }

	@GET
	@Path("{id}")
	public Response getUser(@PathParam("id") Long id) {
		User user = userService.getUser(id);
		if (user == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(user).build();
	}
}
