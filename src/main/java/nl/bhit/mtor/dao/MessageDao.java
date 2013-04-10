package nl.bhit.mtor.dao;

import java.util.List;

import nl.bhit.mtor.model.MTorMessage;
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

}