package nl.bhit.mtor.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.model.soap.SoapMessage;

@WebService
public interface MessageManager extends GenericManager<MTorMessage, Long> {
	MTorMessage saveMessage(SoapMessage message);

	@WebMethod(
			exclude = false,
			operationName = "saveSoapMessage",
			action = "saveSoapMessage")
	void saveSoapMessage(SoapMessage message);
	public List<MTorMessage> getMessagesWithTimestamp(MTorMessage message);
	public List<MTorMessage> getAllByUser(User user);

}