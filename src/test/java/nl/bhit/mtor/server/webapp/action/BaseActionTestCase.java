package nl.bhit.mtor.server.webapp.action;

import java.util.HashMap;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.service.UserManager;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.providers.XWorkConfigurationProvider;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;

/**
 * Base class for running Struts 2 Action tests.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@ContextConfiguration(
        locations = { "classpath:/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml", "classpath*:/applicationContext.xml",
                "classpath:**/applicationContext*.xml" })
public abstract class BaseActionTestCase extends AbstractTransactionalJUnit4SpringContextTests {
	
    /**
     * Transient log to prevent session synchronization issues - children can use instance for logging.
     */
	protected final transient Logger LOG = Logger.getLogger(getClass());
    
    private int smtpPort = 25250;
    @Autowired
    public UserManager userManager;

    @Before
    public void onSetUp() {
        smtpPort = smtpPort + (int) (Math.random() * 100);

        LocalizedTextUtil.addDefaultResourceBundle(Constants.BUNDLE_KEY);

        // Initialize ActionContext
        ConfigurationManager configurationManager = new ConfigurationManager();
        configurationManager.addContainerProvider(new XWorkConfigurationProvider());
        Configuration config = configurationManager.getConfiguration();
        Container container = config.getContainer();

        ValueStack stack = container.getInstance(ValueStackFactory.class).createValueStack();
        stack.getContext().put(ActionContext.CONTAINER, container);
        ActionContext.setContext(new ActionContext(stack.getContext()));

        ActionContext.getContext().setSession(new HashMap<String, Object>());

        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");

        // populate the request so getRequest().getSession() doesn't fail in BaseAction.java
        ServletActionContext.setRequest(new MockHttpServletRequest());
    }

    protected int getSmtpPort() {
        return smtpPort;
    }

    @After
    public void onTearDown() {
        ActionContext.getContext().setSession(null);
    }

    public void logUserIntoSession(final Long userId) {
        logUserIntoSession(userManager.get(userId));

    }

    public void logUserIntoSession(final User user) {
        // log user in automatically
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(),
                user.getConfirmPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

}
