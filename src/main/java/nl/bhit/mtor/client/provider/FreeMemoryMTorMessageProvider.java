package nl.bhit.mtor.client.provider;

import java.io.File;

import nl.bhit.mtor.client.annotation.MTorMessage;
import nl.bhit.mtor.client.annotation.MTorMessageProvider;
import nl.bhit.model.Status;
import nl.bhit.model.soap.SoapMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@MTorMessageProvider
public class FreeMemoryMTorMessageProvider {
	private static final Log log = LogFactory.getLog(FreeMemoryMTorMessageProvider.class);
	public static long WARN_LIMIT = 157286400L; //150 MB in bytes
	public static long ERROR_LIMIT = 52428800L; //50 MB in bytes

	/**
	 * this method will return a warning message when the WARN_LIMMI is reached and an error message when the ERROR_LIMIT is reached. Null when all is fine.
	 * 
	 * @return
	 */
	@MTorMessage
	public static SoapMessage getVirtualMemoryMessage() {
		SoapMessage message = new SoapMessage();
		final long free = Runtime.getRuntime().freeMemory();
		if (free < ERROR_LIMIT) {
			return createMessage(message, "The free memory is les then "+ERROR_LIMIT+"!", Status.ERROR);
		}
		if (free < WARN_LIMIT) {
			return createMessage(message, "The free memory is les then "+WARN_LIMIT+"! It is running low.", Status.WARN);
		}
		return null;
	}

	protected static SoapMessage createMessage(SoapMessage message, String errorMessage, Status status) {
		log.warn(errorMessage);
		message.setContent(errorMessage);
		message.setStatus(status);
		return message;
	}

}
