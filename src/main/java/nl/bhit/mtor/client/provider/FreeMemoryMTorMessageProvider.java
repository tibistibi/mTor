package nl.bhit.mtor.client.provider;

import nl.bhit.mtor.client.annotation.MTorMessage;
import nl.bhit.mtor.client.annotation.MTorMessageProvider;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.soap.SoapMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@MTorMessageProvider
public class FreeMemoryMTorMessageProvider {
	
    private final static Log log = LogFactory.getLog(FreeMemoryMTorMessageProvider.class);
    
    public final static long WARN_LIMIT = 157286400L; // 150 MB in bytes
    public final static long ERROR_LIMIT = 52428800L; // 50 MB in bytes
    
    private final static String ERROR_MSG = "The free memory is less then " + ERROR_LIMIT + "!";
    private final static String WARN_MSG = "The free memory is less then " + WARN_LIMIT + "! It is running low.";

    /**
     * this method will return a warning message when the WARN_LIMIT is reached and an error message when the
     * ERROR_LIMIT is reached. Null when all is fine.
     * 
     * @return
     */
    @MTorMessage
    public static SoapMessage getVirtualMemoryMessage() {
        SoapMessage message = new SoapMessage();
        final long free = Runtime.getRuntime().freeMemory();
        log.trace("free memory is: " + free);
        if (free < ERROR_LIMIT) {
            log.trace(ERROR_MSG);
            return createMessage(message, ERROR_MSG, Status.ERROR);
        }
        if (free < WARN_LIMIT) {
            log.trace(WARN_MSG);
            return createMessage(message, WARN_MSG, Status.WARN);
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
