package nl.bhit.mTor.client;

import nl.bhit.mTor.client.annotation.MTorMessage;
import nl.bhit.mTor.client.annotation.MTorMessageProvider;
import nl.bhit.model.Status;
import nl.bhit.model.soap.SoapMessage;

@MTorMessageProvider
public class DiskSpaceMTorMessageProvider {

	@MTorMessage
	public SoapMessage getDsikSpaceMessage() {
		SoapMessage message = new SoapMessage();
		// TODO(tibi) check the hard drive and deside if the message is needed, a warning or an error.
		message.setContent("The hard drive is getting full!");
		message.setStatus(Status.WARN);
		return message;
	}
}
