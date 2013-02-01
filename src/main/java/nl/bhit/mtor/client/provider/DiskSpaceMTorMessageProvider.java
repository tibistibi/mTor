package nl.bhit.mtor.client.provider;

import java.io.File;

import nl.bhit.model.Status;
import nl.bhit.model.soap.SoapMessage;
import nl.bhit.mtor.client.annotation.MTorMessage;
import nl.bhit.mtor.client.annotation.MTorMessageProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This message provider has one method which will give a soapMessage about the diskSpace.
 * 
 * @author tibi
 */
@MTorMessageProvider
public class DiskSpaceMTorMessageProvider {
	private static final Log log = LogFactory.getLog(DiskSpaceMTorMessageProvider.class);
	public static long ERROR_LIMIT = 1000000L;
	public static long WARN_LIMIT = 10000000L;

	/**
	 * this method will return a warning message when the WARN_LIMMI is reached and an error message when the ERROR_LIMIT is reached. Null when all is fine.
	 * 
	 * @return
	 */
	@MTorMessage
	public static SoapMessage getDiskSpaceMessage() {
		SoapMessage message = new SoapMessage();
		long free = getFreeDiskSpace();
		if (free < ERROR_LIMIT) {
			return createMessage(message, "The hard drive is almost full!", Status.ERROR);
		}
		if (free < WARN_LIMIT) {
			return createMessage(message, "The hard drive is getting full!", Status.WARN);
		}
		return null;
	}

	protected static SoapMessage createMessage(SoapMessage message, String errorMessage, Status status) {
		log.warn(errorMessage);
		message.setContent(errorMessage);
		message.setStatus(status);
		return message;
	}

	protected static long getFreeDiskSpace() {
		File tmp = new File("/");
		long free = tmp.getFreeSpace();
		log.trace("free disk space is: " + free);
		return free;
	}

}
