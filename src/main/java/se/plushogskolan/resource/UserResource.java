package se.plushogskolan.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import se.plushogskolan.model.User;
import se.plushogskolan.model.WorkItem;
import se.plushogskolan.model.WorkItemStatus;
import se.plushogskolan.service.UserService;
import se.plushogskolan.service.WorkItemService;

@Component
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Autowired
    private UserService userService;
    
    @Autowired
    private WorkItemService workItemService;

    
    /**
     * Url: /users/123
     * Method: Put
     * Request body parameter: workItemId
     * 
     * @param stringId
     * @param reqBody
     * @return
     */
    @PUT
    @Path("{id}")
    public Response addWorkItemToUser(@PathParam("id") String stringId, String reqBody){
    	long id=Long.parseLong(stringId);
    	User user=userService.getUser(id);
    	JsonObject jobj=new Gson().fromJson(reqBody,JsonObject.class);
    	String workItemIdString=jobj.get("workItemId").toString();
    	workItemIdString=workItemIdString.substring(1, workItemIdString.length()-1);
    	long workItemId=Long.parseLong(workItemIdString);
    	WorkItem workItem=workItemService.findById(workItemId);
    	workItemService.addWorkItemToUser(workItem, user);
    	return Response.ok().build();
    }

    
}
