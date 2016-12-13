package se.plushogskolan.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import se.plushogskolan.model.WorkItem;
import se.plushogskolan.model.WorkItemStatus;
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

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String stringId){
    	long id=Long.parseLong(stringId);
    	return Response.ok(workItemService.findById(id)).build();
    }
    
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

    @PUT
    @Path("{id}")
    public Response updateStatus(@PathParam("id") String stringId, String reqBody){
    	long id=Long.parseLong(stringId);
    	JsonObject jobj=new Gson().fromJson(reqBody,JsonObject.class);
    	String status=jobj.get("status").toString();
    	status=status.substring(1, status.length()-1);
    	try{
			WorkItemStatus.valueOf(status);
    	}catch(IllegalArgumentException e){
    		return Response.status(400).type(MediaType.TEXT_PLAIN).entity("Status has to be either of: Started, Unstarted or Done.").build();
    	}
    	workItemService.updateStatus(id, WorkItemStatus.valueOf(status));
    	return Response.ok().build();
    }
    
    @DELETE
    @Path("{id}")
    public Response updateStatus(@PathParam("id") String stringId){
    	long id=Long.parseLong(stringId);
    	return Response.ok(workItemService.delete(id)).build();
    }
}
