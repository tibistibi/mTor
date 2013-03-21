package nl.bhit.mtor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import nl.bhit.mtor.dao.MessageDao;
import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.model.soap.ClientMessage;
import nl.bhit.mtor.model.soap.SoapMessage;
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
    public MTorMessage saveMessage(SoapMessage soapMessage) {
        MTorMessage message = soapMessageToMessage(soapMessage);
        return messageDao.save(message);
    }

    @Override
    @WebMethod(
            exclude = false,
            operationName = "saveSoapMessage",
            action = "saveSoapMessage")
    // @RolesAllowed("basicUser")
    public void saveSoapMessage(@WebParam(
            name = "soapMessage") SoapMessage soapMessage) {
        log.trace("saveSoapMessage..." + soapMessage);
        saveMessage(soapMessage);
    }

    protected MTorMessage soapMessageToMessage(SoapMessage soapMessage) {
        log.trace("soapMessageToMessage...retrieving the project: " + soapMessage.getProjectId());
        Project project = projectManager.get(soapMessage.getProjectId());
        MTorMessage message = getMTorMessage(soapMessage);
        convert(soapMessage, project, message);
        return message;
    }

    protected void convert(SoapMessage soapMessage, Project project, MTorMessage message) {
        BeanUtils.copyProperties(soapMessage, message);
        message.setProject(project);
        message.setResolved(false);
    }

    private MTorMessage getMTorMessage(SoapMessage soapMessage) {
        log.trace("getMTorMessage...");
        MTorMessage message = messageDao.getAliveByProject(soapMessage.getProjectId());
        if (message == null) {
            message = new MTorMessage();
        } else {
            message.init();
        }
        return message;
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
    public List<ClientMessage> getUnresolvedAllByLogedInUser() {
        log.trace("starting getUnresolvedAllByLogedInUser");
        User authenticatedUser = UserManagementUtils.getAuthenticatedUser();
        log.trace("found user with id: " + authenticatedUser.getId());
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
        log.trace("starting getUnresolvedByuser with userId: " + userId);
        List<MTorMessage> messages = messageDao.getUnresolvedAll(userId);
        return convertToClientMessageList(messages);
    }

}