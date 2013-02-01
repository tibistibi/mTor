package nl.bhit.service.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import nl.bhit.dao.MessageDao;
import nl.bhit.model.MTorMessage;
import nl.bhit.model.Project;
import nl.bhit.model.soap.SoapMessage;
import nl.bhit.service.GenericManager;
import nl.bhit.service.MessageManager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messageManager")
@WebService(
		serviceName = "MessageService",
		endpointInterface = "nl.bhit.service.MessageManager")
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
		saveMessage(soapMessage);
	}

	protected MTorMessage soapMessageToMessage(SoapMessage soapMessage) {
		Project project = projectManager.get(soapMessage.getProjectId());
		MTorMessage message = new MTorMessage();
		BeanUtils.copyProperties(soapMessage, message);
		message.setProject(project);
		return message;
	}

	public void setProjectManager(GenericManager<Project, Long> projectManager) {
		this.projectManager = projectManager;
	}
}