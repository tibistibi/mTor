package nl.bhit.mtor.server.timer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component(
        value = "mTorServerTimer")
public class MTorServerTimer {
	
	private static final transient Logger LOG = Logger.getLogger(MTorServerTimer.class);
    
    private static AlertSender alertSender;

    protected static String[] getConfigLocations() {
        return new String[] { "classpath:/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml" };
    }

    /**
     * is called from the timer.
     */
    public void process() {
        LOG.debug("starting up the MTorServerTimer processor.");
        if (alertSender == null) {
            LOG.trace("create a new alertSender");
            final BeanFactory factory = new ClassPathXmlApplicationContext(getConfigLocations());
            alertSender = (AlertSender)factory.getBean("alertSender");
        }
        alertSender.process();
    }
}
