package nl.bhit.mtor.dao;

import java.util.List;


import nl.bhit.mtor.dao.GenericDao;
import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.User;

/**
 * An interface that provides a data management interface to the Message table.
 */
public interface MessageDao extends GenericDao<MTorMessage, Long> {
	
	public List<MTorMessage> getMessagesWithTimestamp(MTorMessage message) ;
	
	public List<MTorMessage> getAllByUser(User user);
	
}