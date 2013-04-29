package nl.bhit.mtor.server.webapp.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.service.GenericManager;
import nl.bhit.mtor.service.LookupManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 * <p/>
 * <p>Keep in mind that this listener is executed outside of OpenSessionInViewFilter,
 * so if you're using Hibernate you'll have to explicitly initialize all loaded data at the
 * GenericDao or service level to avoid LazyInitializationException. Hibernate.initialize() works
 * well for doing this.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StartupListener implements ServletContextListener {
	
	private static final transient Logger LOG = Logger.getLogger(StartupListener.class);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void contextInitialized(ServletContextEvent event) {
        LOG.debug("Initializing context...");

        ServletContext context = event.getServletContext();

        // Orion starts Servlets before Listeners, so check if the config
        // object already exists
        Map<String, Object> config = (HashMap<String, Object>)context.getAttribute(Constants.CONFIG);

        if (config == null) {
            config = new HashMap<String, Object>();
        }

        ApplicationContext ctx =
                WebApplicationContextUtils.getRequiredWebApplicationContext(context);

        PasswordEncoder passwordEncoder = null;
        try {
            ProviderManager provider = (ProviderManager) ctx.getBean("org.springframework.security.authentication.ProviderManager#0");
            for (Object o : provider.getProviders()) {
                AuthenticationProvider p = (AuthenticationProvider) o;
                if (p instanceof RememberMeAuthenticationProvider) {
                    config.put("rememberMeEnabled", Boolean.TRUE);
                } else if (ctx.getBean("passwordEncoder") != null) {
                    passwordEncoder = (PasswordEncoder) ctx.getBean("passwordEncoder");
                }
            }
        } catch (NoSuchBeanDefinitionException n) {
            LOG.debug("authenticationManager bean not found, assuming test and ignoring...");
            // ignore, should only happen when testing
        }

        context.setAttribute(Constants.CONFIG, config);

        // output the retrieved values for the Init and Context Parameters
        if (LOG.isDebugEnabled()) {
            LOG.debug("Remember Me Enabled? " + config.get("rememberMeEnabled"));
            if (passwordEncoder != null) {
                LOG.debug("Password Encoder: " + passwordEncoder.getClass().getSimpleName());
            }
            LOG.debug("Populating drop-downs...");
        }

        setupContext(context);
    }

    /**
     * This method uses the LookupManager to lookup available roles from the data layer.
     *
     * @param context The servlet context
     */
    public static void setupContext(ServletContext context) {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        LookupManager mgr = (LookupManager) ctx.getBean("lookupManager");

        // get list of possible roles
        context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());
        LOG.debug("Drop-down initialization complete [OK]");

        // Any manager extending GenericManager will do:
        GenericManager manager = (GenericManager)ctx.getBean("userManager");
        doReindexing(manager);
        LOG.debug("Full text search reindexing complete [OK]");
    }

    private static void doReindexing(GenericManager manager) {
        manager.reindexAll(false);
    }

    /**
     * Shutdown servlet context (currently a no-op method).
     *
     * @param servletContextEvent The servlet context event
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //WARN: The method class org.apache.commons.logging.impl.SLF4JLogFactory#release() was invoked.
        //WARN: Please see http://www.slf4j.org/codes.html for an explanation.
    }
}
