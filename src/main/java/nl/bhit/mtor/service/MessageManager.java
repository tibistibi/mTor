package nl.bhit.mtor.service;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.model.soap.ClientMessage;

@WebService
@Path("/messages")
public interface MessageManager extends GenericManager<MTorMessage, Long> {
	
	/**
	 * save message coming from client REST put method
	 * 
	 * @param message
	 * 			ClientMessage object (JSON object automatically converted by JAX RS)
	 */
    @PUT
    @Consumes("application/json")
    @Path("saveclientmessage")
    void saveClientMessage(ClientMessage message);

    List<MTorMessage> getMessagesWithTimestamp(MTorMessage message);

    List<MTorMessage> getAllByUser(User user);

    /**
     * get messages
     * 
     * @param user
     *            used to filter on
     * @param resolved
     *            used to filer on
     * @return messages for this user filtered on resolved
     */
    List<MTorMessage> getAllByUser(User user, boolean resolved);

    // @Path("all")
    @GET
    /**
     * will retrieve messages which belong to the logged in user.
     * @return
     */
    List<ClientMessage> getUnresolvedAllByLogedInUser();

    @GET
    @Path("{id}")
    /**
     * will retrieve messages which belong to the logged in user.
     * @return
     */
    List<ClientMessage> getUnresolvedAllByUser(@PathParam("id") Long id);
    
    /**
     * Get the number of messages of specified project and specified status (if present).
     * 
     * @param project
     * 				Project which we want to filter.
     * @param status
     * 				Status which we want to filter. Optional argument.
     * @return
     * 			Number of messages of this project with these status (if present). Null if no instantiated project is passed as parameter.
     */
    Long getMessageNumber(Project project, Status... status);
    
    /**
     * Get the last message (most new by timestamp) of the specified project and specified status (if present).
     * 
     * @param project
     * 				Project which we want to filter.
     * @param status
     * 				Status which we want to filter. Optional argument.
     * @return
     * 			Newest message of this project with these status (if present). Null if no instantiated project is passed as parameter or this project has no messages.
     */
    MTorMessage getNewestMessage(Project project, Status... status);
    
}