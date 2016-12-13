package se.plushogskolan.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.plushogskolan.model.WorkItem;
import se.plushogskolan.service.WorkItemService;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

}
