package se.plushogskolan.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import se.plushogskolan.model.Team;
import se.plushogskolan.model.User;
import se.plushogskolan.model.WorkItem;
import se.plushogskolan.service.TeamService;
import se.plushogskolan.service.UserService;
import se.plushogskolan.service.WorkItemService;

@Component
@Path("users")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Consumes(MediaType.APPLICATION_JSON)
public final class UserResource {

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private WorkItemService workItemService;

	@Context
	private UriInfo uriInfo;

	/**
	 * uri: .../users
	 * 
	 * man får skicka "teamname" i http json body:n. Om redan finns ett team med
	 * det här namnet hämtas teamet och skickas sätts till user. Om inget team
	 * mead det här namnet finns då skapas ett team med namnet och sätts till
	 * user. Skicka en Json så här:
	 * {
	 *    	"firstname": "xxx",
	 * 		"lastname": "xxx",
	 * 		"username": "xxx", 
	 * 		"teamname": "xxx"
   	 * }
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
		URI location = uriInfo.getAbsolutePathBuilder().path(UserResource.class, "getUser").build(user.getId());
		return Response.created(location).build();
	}

	/**
	 * uri: .../users/p hämtar alla users pagenerad
	 * 
	 * @param page
	 * @param size
	 */
	@GET
	@Path("p")
	public Response getAllUsers(@QueryParam("page") @DefaultValue("0") int page,
			@QueryParam("size") @DefaultValue("10") int size) {

		List<User> users = userService.findAllUsers(page, size);
		return Response.ok(users).build();
	}

	/**
	 *
	 * Uri: /users?filter=<Type of search>&criteria=<Search criteria>
	 * if no filter specified, all users returned.
	 * 
	 * @param filter
	 * @param criteria
	 * @author Iman, Credit goes to Daniel!
	 * @return
	 */
	@GET
	public Response getUser(@QueryParam("filter") String filter, @QueryParam("criteria") String criteria) {

		List<User> users = new ArrayList<>();
		User user = null;
		
		if (filter == null) {
			users = userService.getAllUsers();
			return Response.ok(users).build();
		}
		
		switch (filter) {
		case "id":
			user = userService.getUser(Long.parseLong(criteria));
			if (user != null) users.add(user);
			break;
		case "username":
			user = userService.getUserByUsername(criteria);
			if (user != null) users.add(user);
			break;
		case "usernumber":
			user = userService.getUserByUsernumber(criteria);
			if (user != null) users.add(user);
			break;
		case "fname":
			users = userService.getUserByFirstname(criteria);
			break;
		case "lname":
			users = userService.getUserByLastname(criteria);
			break;
		case "teamname":
			Team team = teamService.findByName(criteria);
			users = userService.getAllUsersInTeam(team);
			break;
		default:
			return Response.status(Status.BAD_REQUEST).entity("Query parameter format is wrong.").build();
		}
		if (users.size() == 0)
			return Response.status(Status.NOT_FOUND).entity("User with given criteria doesn't exist.").build();
		else {
			return Response.ok(users).build();
		}
	}

	/**
	 * Url: /users/123 
	 * Method: Put 
	 * Request body parameter:  "workItemId" : 123 || 
	 * 							"update" : true ||
	 * 							"deactivate" : true ||
	 * 							"activate" : true 
	 * 
	 * @param stringId
	 * @param reqBody
	 * @return
	 */
	@PUT
	@Path("{id}")
	public Response updateUser(@PathParam("id") String stringId, String reqBody) {

		long id = Long.parseLong(stringId);
		JsonObject jobj = new Gson().fromJson(reqBody, JsonObject.class);
		User user = userService.getUser(id);

		// addWorkItemToUser
		if (jobj.has("workItemId")) {

			Long workItemId = jobj.get("workItemId").getAsLong();
			WorkItem workItem = workItemService.findById(workItemId);
			workItemService.addWorkItemToUser(workItem, user);
			return Response.ok().build();

		} else if (jobj.has("deactivate") && jobj.get("deactivate").getAsBoolean()) {
			
			userService.deactivateUser(user);
			return Response.ok().build();

		} else if (jobj.has("activate") && jobj.get("activate").getAsBoolean()) {

			userService.activateUser(user);
			return Response.ok().build();

		} else if (jobj.has("update") && jobj.get("update").getAsBoolean()) {
			
			String firstname = jobj.get("firstname").getAsString();
			String lastname = jobj.get("lastname").getAsString();
			String username = jobj.get("username").getAsString();
			user.setFirstname(firstname);
			user.setLastname(lastname);
			user.setUsername(username);
			userService.updateUser(user);
			return Response.ok().build();
			
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
}