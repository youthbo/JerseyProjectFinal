package se.plushogskolan.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.plushogskolan.model.Issue;
import se.plushogskolan.service.IssueService;
import se.plushogskolan.service.ServiceException;

@Component
@Path("issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssueResource {
	@Autowired
	private IssueService issueService;
	
	@Context
	private UriInfo uriInfo;

	/**
	 *URL:.../issues 
	 *body:
	 *  {
	 *    "description":"issue2"
     *  } 
	 */
	@POST
	@Secured
	public Response create(Issue issue){
		try{
		    issueService.createIssue(issue);
		}catch(ServiceException e){
			throw new WebApplicationException("Issue already exists!",Status.CONFLICT);
		}
		URI location = uriInfo.getAbsolutePathBuilder().path(IssueResource.class, "update").build(issue.getId());
		return Response.created(location).build();
	}
	
	/**
	 *URL:.../issues/id 
	 *body:
	 *  {
	 *    "description":"issue2"
     *  } 
	 */
	@PUT
	@Secured
	@Path("{id}")
	public Response update(Issue issue,@PathParam("id") Long id){	
		try{
		    issueService.updateIssue(issueService.getIssueById(id), issue.getDescription());
		}catch(ServiceException e){
			throw new WebApplicationException("Description already exists!",Status.CONFLICT);
		}
		return Response.ok(issue).build();
	}
	
	
}
