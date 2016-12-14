package se.plushogskolan.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.plushogskolan.model.Team;
import se.plushogskolan.service.TeamService;

@Component
@Path("teams")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
public final class TeamResource {

	@Autowired
	TeamService teamService;

	@Context
	private UriInfo uriInfo;

	@POST
	public Response addTeam(Team team) {

		teamService.createTeam(team);
		URI location = uriInfo.getAbsolutePathBuilder().path(team.getId().toString()).build();

		return Response.created(location).build();
	}

	@PUT
	@Path("{id}")
	public Response uppdateTeam(@PathParam("id") Long id, Team team) {
		teamService.uppdateTeam(id, team.getName(), team.getStatus());
		return Response.ok().build();

	}
	
	@GET
	public Iterable<Team> getAllTeams() {
		return teamService.findAllTeams();
	}
	
	

	// @GET
	// public Collection<User> getAllUsers() {
	// return users.values();
	// }
	//
	//
}