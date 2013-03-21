package nl.bhit.mtor.dao;

import java.util.List;

import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.User;

/**
 * An interface that provides a data management interface to the Message table.
 */
public interface MessageDao extends GenericDao<MTorMessage, Long> {

    public List<MTorMessage> getMessagesWithTimestamp(MTorMessage message);

    public List<MTorMessage> getAllByUser(User user);

    /**
     * will search for the 'alive' message of the project
     * 
     * @param projectId
     *            the id of the project
     * @return the found message
     */
    public MTorMessage getAliveByProject(Long projectId);

    /**
     * WIll find all not resolved message
     * 
     * @param authenticatedUser
     * @return found messages
     */
    public List<MTorMessage> getUnresolvedAll(User user);

    /**
     * WIll find all not resolved message
     * 
     * @param userId
     *            used to filter on
     * @return found messages
     */
    public List<MTorMessage> getUnresolvedAll(Long userId);

}