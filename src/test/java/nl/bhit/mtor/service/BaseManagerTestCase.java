package nl.bhit.mtor.service;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import nl.bhit.mtor.util.ConvertUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(
        locations = { "classpath:/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml", "classpath*:/**/applicationContext.xml" })
/**
 * Test classes can extend this manager based on a spring context.
 * This test class can be moved to the test tree.
 *
 * @author mraible
 */
public abstract class BaseManagerTestCase extends AbstractTransactionalJUnit4SpringContextTests {

    /**
     * A simple logger
     */
	protected final transient Logger LOG = Logger.getLogger(getClass());
    /**
     * The resourceBundle
     */
    protected ResourceBundle rb;

    /**
     * Default constructor will set the ResourceBundle if needed.
     */
    public BaseManagerTestCase() {
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            // LOG.warn("No resource bundle found for: " + className);
        }
    }

    /**
     * Utility method to populate an object with values from a properties file
     * 
     * @param obj
     *            the model object to populate
     * @return Object populated object
     * @throws Exception
     *             if BeanUtils fails to copy properly
     */
    protected Object populate(Object obj) throws Exception {
        // loop through all the beans methods and set its properties from
        // its .properties file
        Map map = ConvertUtil.convertBundleToMap(rb);

        BeanUtils.copyProperties(obj, map);

        return obj;
    }
}
