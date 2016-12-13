package se.plushogskolan.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import se.plushogskolan.model.WorkItem;
import se.plushogskolan.model.WorkItemStatus;
import se.plushogskolan.service.IssueService;
import se.plushogskolan.service.ServiceException;
import se.plushogskolan.service.WorkItemService;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

/**
 * Created by daniel on 12/12/16.
 */
@Component
@Path("workitems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class WorkItemResource {

    @Autowired
    private WorkItemService workItemService;
    
    @Autowired
    private IssueService issueService;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    /**
     * Url: /workitems/123
     * Method: Get
     * 
     * @param stringId
     * @return
     */
    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String stringId){
    	long id=Long.parseLong(stringId);
    	return Response.ok(workItemService.findById(id)).build();
    }
    
    /**
     * Url: /workitems
     * Method: Post
     * Request body parameter: workitem json string
     * 
     * @param workItem
     * @return
     */
    @POST
    public Response create(WorkItem workItem){
    	workItem=workItemService.create(workItem);
    	URI location=null;
		try {
			location = new URI("workitems/"+workItem.getId());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	return Response.created(location).build();
    }

    
    /**
     * Url: /workitems/123
     * Method: Put
     * Request body parameter: status
     * 
     * @param stringId
     * @param reqBody
     * @return
     */
    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") String stringId, String reqBody){
    	
    	long id=Long.parseLong(stringId);
    	JsonObject jobj=new Gson().fromJson(reqBody,JsonObject.class);
    	
    	if (jobj.has("status")){
    		
    		String status=jobj.get("status").toString();
	    	status=status.substring(1, status.length()-1);
	    	try{
				WorkItemStatus.valueOf(status);
	    	}catch(IllegalArgumentException e){
	    		return Response.status(400).type(MediaType.TEXT_PLAIN).entity("Status has to be either of: Started, Unstarted or Done.").build();
	    	}
	    	workItemService.updateStatus(id, WorkItemStatus.valueOf(status));
	    	return Response.ok(workItemService.findById(id)).build();
    	}
    	else if(jobj.has("issue")){
    		String issue=jobj.get("issue").toString();
	    	issue= issue.substring(1, issue.length()-1);
	    	WorkItem workItem = workItemService.findById(id);
	    	try{
    		   issueService.assignToWorkItem(issueService.getIssueByName(issue), workItem);
	    	}catch(ServiceException e){
	    		return Response.status(400)
	    				.entity("The status of workitem is not 'Done' ").build();
	    	}
	    	return Response.ok(workItem).build();
    	}else {
    		return Response.status(Status.BAD_REQUEST).build();
    	} 	    	
    }
    
    
    /**
     * Url: /workitems/123
     * Method: Delete
     * 
     * @param stringId
     * @return
     */
    @DELETE
    @Path("{id}")
    public Response updateStatus(@PathParam("id") String stringId){
    	long id=Long.parseLong(stringId);
    	return Response.ok(workItemService.delete(id)).build();
    }
}
