package nl.bhit.mtor.dao;

import java.util.List;

import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.User;

/**
 * An interface that provides a data management interface to the Message table.
 */
public interface MessageDao extends GenericDao<MTorMessage, Long> {

    List<MTorMessage> getMessagesWithTimestamp(MTorMessage message);

    List<MTorMessage> getAllByUser(User user);

    /**
     * will search for the 'alive' message of the project
     * 
     * @param projectId
     *            the id of the project
     * @return the found message
     */
    MTorMessage getAliveByProject(Long projectId);

    /**
     * WIll find all not resolved message
     * 
     * @param authenticatedUser
     * @return found messages
     */
    List<MTorMessage> getUnresolvedAll(User user);

    /**
     * WIll find all not resolved message
     * 
     * @param userId
     *            used to filter on
     * @return found messages
     */
    List<MTorMessage> getUnresolvedAll(Long userId);
    
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
    
    /**
     * Get the number of messages of specified project and specified status (if present).
     * 
     * @param projectId
     * 				Valid project id.
     * @param status
     * 				Status which we want to filter. Optional argument.
     * @return
     * 			Number of messages of this project with these status (if present).
     */
    Long getMessageNumberByProject(Long projectId, Status... status);
    
    /**
     * Get the lasts N (represented by numberOfMessages parameter) messages (most new by timestamp) 
     * of the specified project and specified status (if present).
     * 
     * @param projectId
     * 				Valid project id.
     * @param numberOfMessages
     * 				Number of messages that we want to get.
     * @param status
     * 				Status which we want to filter. Optional argument.
     * @return
     * 			List of N messages of this project with these status (if present). Null if this project has no messages.
     */
    List<MTorMessage> getLastNMessagesByProject(Long projectId, int numberOfMessages, Status... status);

}