package se.plushogskolan.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.xml.internal.ws.util.StringUtils;

import se.plushogskolan.model.WorkItem;
import se.plushogskolan.model.WorkItemStatus;
import se.plushogskolan.service.IssueService;
import se.plushogskolan.service.ServiceException;
import se.plushogskolan.service.WorkItemService;

/**
 * Created by daniel on 12/12/16.
 */
@Component
@Path("workitems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
public final class WorkItemResource {

    @Autowired
    private WorkItemService workItemService;
    
    @Autowired
    private IssueService issueService;

    /**
     *
     * Url: /workitems?filter=<Type of search>&criteria=<Search criteria>
     *
     *     filter refers to the values that are in case of the switch statement
     *     criteria refers to the criteria layed out in the project assignment,
     *
     *     status=started/unstarted etc
     *     team = teamname
     *     user = userId
     *     text = text in description
     *     Issue = issueId
     *
     *
     * @param filter
     * @param  criteria
     * @author Daniel
     * @return
     */
    @GET
    public Response getWorkItemsBy(@QueryParam("filter") String filter, @QueryParam("criteria")String criteria) {
        List<WorkItem> workItems=null;
        try {
            switch(filter){
                case "status": workItems = workItemService.findAllByStatus(WorkItemStatus.valueOf(StringUtils.capitalize(criteria))); break;
                case "team": workItems = workItemService.findAllByTeamName(StringUtils.capitalize(criteria)); break;
                case "user": workItems = workItemService.findAllByUser(new Long(criteria)); break;
                case "text": workItems = workItemService.findByDescriptionContaining(StringUtils.capitalize(criteria)); break;
                case "issue": workItems = issueService.getAllItemsWithIssue(issueService.getIssueById(Long.parseLong(criteria))); break;
                default: throw new Exception();
            }
        } catch (Exception e) { //This will be changed later to something more suitable for a jaxrs environment
            return Response.status(Status.BAD_REQUEST).entity("Query parameter format is wrong.").build();
        }
        return Response.ok(workItems).build();
    }

    
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
     * Request body parameter: status or issue description
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
    		
    		String status=jobj.get("status").getAsString();
	    	try{
				WorkItemStatus.valueOf(status);
	    	}catch(IllegalArgumentException e){
	    		return Response.status(400).type(MediaType.TEXT_PLAIN).entity("Status has to be either of: Started, Unstarted or Done.").build();
	    	}
	    	workItemService.updateStatus(id, WorkItemStatus.valueOf(status));
	    	return Response.ok(workItemService.findById(id)).build();
    	}
    	if(jobj.has("issue")){
    		String issue=jobj.get("issue").getAsString();
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
    public Response delete(@PathParam("id") String stringId){
    	long id=Long.parseLong(stringId);
    	return Response.ok(workItemService.delete(id)).build();
    }
}
