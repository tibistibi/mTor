package nl.bhit.mtor.client;

import junit.framework.TestCase;

import nl.bhit.mtor.client.MessageServiceSender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class MessageServiceSenderTest extends TestCase {
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());

	@Test
	public void testAddMessage() {
		log.trace("start testAddMessage...");
		MessageServiceSender client = new MessageServiceSender();
		try {
			client.sendMessages();
			log.info("sending worked");
		} catch (Exception e) {
			log.info("sending failed. this test only works with active soap service");
			log.info("error", e);
		}
	}
}
