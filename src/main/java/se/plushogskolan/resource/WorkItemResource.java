package se.plushogskolan.resource;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import com.sun.xml.internal.ws.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.plushogskolan.model.WorkItem;
import se.plushogskolan.model.WorkItemStatus;
import se.plushogskolan.service.WorkItemService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by daniel on 12/12/16.
 */
@Component
@Path("workitems")
@Produces(MediaType.APPLICATION_JSON)
public final class WorkItemResource {

    @Autowired
    private WorkItemService workItemService;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @GET
    @Path("status")
    public Response getAllByStatus(@QueryParam("criteria") @DefaultValue("") String status) {
        List<WorkItem> workItems = workItemService.findAllByStatus(WorkItemStatus.valueOf(StringUtils.capitalize(status)));
        return Response.ok(new Gson().toJson(workItems)).build();
    }

    @GET
    @Path("team")
    public Response getAllByTeam(@QueryParam("criteria") @DefaultValue("") String team) {
        List<WorkItem> workItems = workItemService.findAllByTeamName(team);
        return Response.ok(new Gson().toJson(workItems)).build();
    }

    @GET
    @Path("user")
    public Response getAllByUser(@QueryParam("criteria") Long userid) {
        List<WorkItem> workItems = workItemService.findAllByUser(userid);
        return Response.ok(new Gson().toJson(workItems)).build();
    }

    @GET
    @Path("text")
    public Response getAllByUser(@QueryParam("criteria") @DefaultValue("") String text) {
        List<WorkItem> workItems = workItemService.findByDescriptionContaining(text);
        return Response.ok(new Gson().toJson(workItems)).build();
    }
}
