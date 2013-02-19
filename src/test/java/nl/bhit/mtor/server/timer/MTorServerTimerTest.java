package nl.bhit.mtor.server.timer;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class MTorServerTimerTest extends TestCase {
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());

	@Test
	public void testTimer() {
		log.trace("start testAddMessage...");
		MTorServerTimer timer = new MTorServerTimer();
		timer.process();
	}
}
