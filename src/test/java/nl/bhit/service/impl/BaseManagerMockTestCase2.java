package nl.bhit.service.impl;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import nl.bhit.util.ConvertUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * A mock class for testing using JMock. This test class can be moved to the test tree.
 * 
 * @author mraible
 */
@ContextConfiguration(
		locations = { "classpath:/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml", "classpath*:/applicationContext.xml",
				"classpath:**/applicationContext*.xml" })
// @RunWith(JMock.class)
public abstract class BaseManagerMockTestCase2 extends AbstractTransactionalJUnit4SpringContextTests {
	/**
	 * A logger
	 */
	protected final Log log = LogFactory.getLog(getClass());
	/**
	 * The resourceBundle
	 */
	protected ResourceBundle rb;
	/**
	 * The junit 4 context
	 */
	protected Mockery context = new JUnit4Mockery();

	/**
	 * Default constructor will set the ResourceBundle if needed.
	 */
	public BaseManagerMockTestCase2() {
		// Since a ResourceBundle is not required for each class, just
		// do a simple check to see if one exists
		String className = this.getClass().getName();

		try {
			rb = ResourceBundle.getBundle(className);
		} catch (MissingResourceException mre) {
			// log.debug("No resource bundle found for: " + className);
		}
	}

	/**
	 * Utility method to populate a javabean-style object with values
	 * from a Properties file
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
