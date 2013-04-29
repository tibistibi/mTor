package nl.bhit.mtor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import nl.bhit.mtor.dao.MessageDao;
import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.model.soap.ClientMessage;
import nl.bhit.mtor.server.webapp.util.UserManagementUtils;
import nl.bhit.mtor.service.MessageManager;
import nl.bhit.mtor.service.ProjectManager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messageManager")
@WebService(
        serviceName = "MessageService",
        endpointInterface = "nl.bhit.mtor.service.MessageManager")
public class MessageManagerImpl extends GenericManagerImpl<MTorMessage, Long> implements MessageManager {
	
    MessageDao messageDao;
    @Autowired
    private ProjectManager projectManager;

    @Autowired
    public MessageManagerImpl(MessageDao messageDao) {
        super(messageDao);
        this.messageDao = messageDao;
    }
    
    @Override
    @WebMethod(
            exclude = true)
    // @RolesAllowed("basicUser")
    public void saveClientMessage(ClientMessage clientMessage) {
    	Project project = projectManager.get(clientMessage.getProjectId());
        MTorMessage message = messageDao.getAliveByProject(clientMessage.getProjectId());
        if (message == null) {
            message = new MTorMessage();
        } else {
            message.init();
        }
        BeanUtils.copyProperties(clientMessage, message);
        message.setProject(project);
        message.setResolved(false);
        
    	messageDao.save(message);
    }
    
    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    @Override
    @WebMethod(
            exclude = true)
    public List<MTorMessage> getMessagesWithTimestamp(MTorMessage message) {
        return messageDao.getMessagesWithTimestamp(message);
    }

    @Override
    @WebMethod(
            exclude = true)
    public List<MTorMessage> getAllByUser(User user) {
        return messageDao.getAllByUser(user);
    }

    @Override
    @WebMethod(
            exclude = true)
    public List<MTorMessage> getAllByUser(User user, boolean resolved) {
        return messageDao.getAllByUser(user, resolved);
    }

    @Override
    public List<ClientMessage> getUnresolvedAllByLogedInUser() {
        LOG.trace("starting getUnresolvedAllByLogedInUser");
        User authenticatedUser = UserManagementUtils.getAuthenticatedUser();
        LOG.trace("found user with id: " + authenticatedUser.getId());
        List<MTorMessage> messages = messageDao.getUnresolvedAll(authenticatedUser);
        return convertToClientMessageList(messages);
    }

    protected List<ClientMessage> convertToClientMessageList(List<MTorMessage> messages) {
        List<ClientMessage> clientMessages = new ArrayList<ClientMessage>(messages.size());
        for (MTorMessage mTorMessage : messages) {
            clientMessages.add(new ClientMessage(mTorMessage));
        }
        return clientMessages;
    }

    @Override
    public List<ClientMessage> getUnresolvedAllByUser(Long userId) {
        LOG.trace("starting getUnresolvedByuser with userId: " + userId);
        List<MTorMessage> messages = messageDao.getUnresolvedAll(userId);
        return convertToClientMessageList(messages);
    }

	@Override
	public Long getMessageNumber(Project project, Status... status) {
		if (project == null || project.getId() == null) {
			return null;
		}
		return messageDao.getMessageNumberByProject(project.getId(), status);
	}

	@Override
	public MTorMessage getNewestMessage(Project project, Status... status) {
		if (project == null || project.getId() == null) {
			return null;
		}
		List<MTorMessage> lstAux = messageDao.getLastNMessagesByProject(project.getId(), 1, status);
		return lstAux == null ? null : lstAux.get(0);
	}
    
}