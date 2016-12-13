package se.plushogskolan.resource;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
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
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.plushogskolan.model.Team;
import se.plushogskolan.model.User;
import se.plushogskolan.model.WorkItem;
import se.plushogskolan.service.TeamService;
import se.plushogskolan.service.UserService;
import se.plushogskolan.service.WorkItemService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
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
	 * user.
	 * 
	 * @param size
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
		// URI location =
		// uriInfo.getAbsolutePathBuilder().path(user.getId().toString()).build();
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
	 * uri: .../users?key=value
	 * 
	 * om man skickar flera parametrar metoden tar den första som matchar och struntar i 
	 * de andra! notera att ordning är viktigt, dvs om ni skickar id=1&name=iman metoden 
	 * tar id, men om ni skickar name=iman&id=1 den tar namnet och skiter i id:n! 
	 * 
	 * @param id (id här är BaseEntitys autogenererad id, d.v.s location som
	 * skickas tillbaka med Respond från POST)
	 * 
	 * @param fname
	 * @param lname
	 * @param username
	 * @param usernumber
	 * @param teamname
	 * @return
	 */
	@GET
	public Response getUser(@QueryParam("id") Long id, @QueryParam("fname") String fname,
			@QueryParam("lname") String lname, @QueryParam("username") String username,
			@QueryParam("usernumber") String usernumber, @QueryParam("team") String teamname) {

		if (id != null) {
			
			User user = userService.getUser(id);
			if (user == null) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok(user).build();

		} else if (fname != null) {
			
			List<User> users = userService.getUserByFirstname(fname);
			if (users.size() == 0) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok(users).build();

		} else if (lname != null) {
			
			List<User> users = userService.getUserByLastname(lname);
			if (users.size() == 0) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok(users).build();

		} else if (username != null) {
			
			User user = userService.getUserByUsername(username);
			if (user == null) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok(user).build();

		} else if (usernumber != null) {
			
			User user = userService.getUserByUsernumber(usernumber);
			if (user == null) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok(user).build();

		} else if (teamname != null) {
			
			Team team = teamService.findByName(teamname);
			List<User> users = userService.getAllUsersInTeam(team);
			if (users.size() == 0) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok(users).build();
			
		} else {
			
			List<User> users = userService.getAllUsers();
			if (users.size() == 0) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok(users).build();
		}
	}

	/**
	 * Url: /users/123 Method: Put Request body parameter: workItemId
	 * 
	 * @param stringId
	 * @param reqBody
	 * @return
	 */
	@PUT
	@Path("{id}")
	public Response addWorkItemToUser(@PathParam("id") String stringId, String reqBody) {
		long id = Long.parseLong(stringId);
		User user = userService.getUser(id);
		JsonObject jobj = new Gson().fromJson(reqBody, JsonObject.class);
		String workItemIdString = jobj.get("workItemId").toString();
		workItemIdString = workItemIdString.substring(1, workItemIdString.length() - 1);
		long workItemId = Long.parseLong(workItemIdString);
		WorkItem workItem = workItemService.findById(workItemId);
		workItemService.addWorkItemToUser(workItem, user);
		return Response.ok().build();
	}
}