package nl.bhit.mtor.server.timer;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

public class MTorServerTimerTest extends TestCase {
	
    /**
     * Log variable for all child classes. Uses Logger.getLogger from Log4J Logging
     */
	private static final transient Logger LOG = Logger.getLogger(MTorServerTimerTest.class);

    @Test
    public void testTimer() {
        LOG.trace("start testAddMessage...");
        MTorServerTimer timer = new MTorServerTimer();
        timer.process();
    }
}
