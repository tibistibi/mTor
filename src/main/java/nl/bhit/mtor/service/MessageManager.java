package nl.bhit.mtor.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.model.soap.ClientMessage;
import nl.bhit.mtor.model.soap.SoapMessage;

@WebService
@Path("/messages")
public interface MessageManager extends GenericManager<MTorMessage, Long> {
    MTorMessage saveMessage(SoapMessage message);

    @WebMethod(
            exclude = false,
            operationName = "saveSoapMessage",
            action = "saveSoapMessage")
    void saveSoapMessage(SoapMessage message);

    List<MTorMessage> getMessagesWithTimestamp(MTorMessage message);

    List<MTorMessage> getAllByUser(User user);

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


}