package nl.bhit.mtor.client.provider;

import nl.bhit.model.Status;
import nl.bhit.model.soap.SoapMessage;
import nl.bhit.mtor.client.annotation.MTorMessage;
import nl.bhit.mtor.client.annotation.MTorMessageProvider;

/**
 * This message provider has one method which will send a message with status INFO and content 'i am alive'
 * 
 */

@MTorMessageProvider
public class IAmAliveMTorMessageProvider {
	
	@MTorMessage
	public static SoapMessage sendIAmAliveMessage() {
		SoapMessage message = new SoapMessage();
		message.setContent("I am alive!");
		message.setStatus(Status.INFO);
		return message;
	}
}
