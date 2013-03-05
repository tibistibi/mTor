package nl.bhit.mtor.service.impl;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import nl.bhit.mtor.dao.MessageDao;
import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.model.soap.SoapMessage;
import nl.bhit.mtor.service.GenericManager;
import nl.bhit.mtor.service.MessageManager;

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
	private GenericManager<Project, Long> projectManager;

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
		BeanUtils.copyProperties(soapMessage, message);
		message.setProject(project);
		message.setResolved(false);
		return message;
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

	public void setProjectManager(GenericManager<Project, Long> projectManager) {
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
}