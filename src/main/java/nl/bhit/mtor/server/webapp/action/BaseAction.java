package nl.bhit.mtor.server.webapp.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.service.GenericManager;
import nl.bhit.mtor.service.MailEngine;
import nl.bhit.mtor.service.RoleManager;
import nl.bhit.mtor.service.UserManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.mail.SimpleMailMessage;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Implementation of <strong>ActionSupport</strong> that contains
 * convenience methods for subclasses. For example, getting the current
 * user and saving messages/errors. This class is intended to
 * be a base class for all Action classes.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseAction extends ActionSupport {
    private static final long serialVersionUID = 3525445612504421307L;

    /**
     * Constant for cancel result String
     */
    public static final String CANCEL = "cancel";

    /**
     * Transient log to prevent session synchronization issues - children can use instance for logging.
     */
    protected final transient Log log = LogFactory.getLog(getClass());

    /**
     * The UserManager
     */
    protected transient UserManager userManager;

    /**
     * The RoleManager
     */
    protected transient RoleManager roleManager;

    /**
     * Indicator if the user clicked cancel
     */
    protected String cancel;

    /**
     * Indicator for the page the user came from.
     */
    protected String from;

    /**
     * Set to "delete" when a "delete" request parameter is passed in
     */
    protected String delete;

    /**
     * Set to "save" when a "save" request parameter is passed in
     */
    protected String save;

    /**
     * MailEngine for sending e-mail
     */
    protected transient MailEngine mailEngine;

    /**
     * A message pre-populated with default data
     */
    protected SimpleMailMessage mailMessage;

    /**
     * Velocity template to use for e-mailing
     */
    protected String templateName;

    /**
     * Simple method that returns "cancel" result
     * 
     * @return "cancel"
     */
    public String cancel() {
        return CANCEL;
    }

    /**
     * Save the message in the session, appending if messages already exist
     * 
     * @param msg
     *            the message to put in the session
     */
    @SuppressWarnings("unchecked")
    protected void saveMessage(String msg) {
        List<String> messages = (List<String>)getRequest().getSession().getAttribute("messages");
        if (messages == null) {
            messages = new ArrayList<String>();
        }
        messages.add(msg);
        getRequest().getSession().setAttribute("messages", messages);
    }

    /**
     * Convenience method to get the Configuration HashMap
     * from the servlet context.
     * 
     * @return the user's populated form from the session
     */
    protected Map<String, Object> getConfiguration() {
        @SuppressWarnings("unchecked")
		Map<String, Object> config = (HashMap<String, Object>)getSession().getServletContext().getAttribute(Constants.CONFIG);
        // so unit tests don't puke when nothing's been set
        if (config == null) {
            return new HashMap<String, Object>();
        }
        return config;
    }

    /**
     * Convenience method to get the request
     * 
     * @return current request
     */
    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    /**
     * Convenience method to get the response
     * 
     * @return current response
     */
    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    /**
     * Convenience method to get the session. This will create a session if one doesn't exist.
     * 
     * @return the session from the request (request.getSession()).
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * Convenience method to send e-mail to users
     * 
     * @param user
     *            the user to send to
     * @param msg
     *            the message to send
     * @param url
     *            the URL to the application (or where ever you'd like to send them)
     */
    protected void sendUserMessage(User user, String msg, String url) {
        if (log.isDebugEnabled()) {
            log.debug("sending e-mail to user [" + user.getEmail() + "]...");
        }

        mailMessage.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", user);
        // TODO: figure out how to get bundle specified in struts.xml
        model.put("message", msg);
        model.put("applicationURL", url);
        mailEngine.sendMessage(mailMessage, templateName, model);
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    /**
     * Convenience method that wraps addElementsById call. Useful if we want to work with long ids and
     * string array values. This method convert string to long values.
     * 
     * Note that manager and setToUpdate can NOT be null. If they are the method will do anything and returns.
     * If some format exception occurs during string to long values conversion the method returns.
     * 
     * @param manager					Manager that allows us to load appropriate entities to setToUpdate.
     * @param setToUpdate				Set of elements that we want to update.
     * @param selectedIds				Array of strings with the elements selected by user.
     * @param clearSetIfEmpty			Flag that indicates if we want to clear the set when selectedIds is empty.
     * 									True will clear the set if selectedIds is empty (indicates that user doesn't want any element).
     * 									False won't clear the set (useful when the page is loaded and there is no user's selection).
     */
	protected <E extends Serializable, PK extends Serializable> void addElementsByLongId(final GenericManager<E, PK> manager, 
																						 final Set<E> setToUpdate, 
																						 final String[] selectedIds, 
																						 boolean clearSetIfEmpty) {
    	if (manager == null || setToUpdate == null) {
    		return;
    	}
    	final Set<PK> selIds = new HashSet<PK>();
    	if (selectedIds != null && selectedIds.length > 0) {
	    	try {
		    	for (String strId : selectedIds) {
		    		@SuppressWarnings("unchecked")
		    		PK id = (PK)Long.valueOf(strId);
		    		selIds.add(id);
		    	}
	    	} catch (NumberFormatException nfe) {
	    		return;
	    	}
    	}
    	addElementsById(manager, setToUpdate, selIds, clearSetIfEmpty);
    }
    
    /**
     * Updates a set of elements (entities) based on user selection.
     * 
     * @param manager					Manager that allows us to load appropriate entities to setToUpdate.
     * @param setToUpdate				Set of elements that we want to update.
     * @param selectedIds				Array of strings with the elements selected by user.
     * @param clearSetIfEmpty			Flag that indicates if we want to clear the set when selectedIds is empty.
     * 									True will clear the set if selectedIds is empty (indicates that user doesn't want any element).
     * 									False won't clear the set (useful when the page is loaded and there is no user's selection).
     */
    protected <E extends Serializable, PK extends Serializable> void addElementsById(final GenericManager<E, PK> manager, 
    																				 final Set<E> setToUpdate, 
    																				 final Set<PK> selectedIds, 
    																				 boolean clearSetIfEmpty) {
    	if (manager == null || setToUpdate == null) {
    		return;
    	}
    	
    	if (selectedIds == null || selectedIds.isEmpty()) {
    		if (clearSetIfEmpty) {
    			setToUpdate.clear();
    		}
    	} else if (setToUpdate.isEmpty()) {
    		for (PK id : selectedIds) {
    			setToUpdate.add(manager.get(id));
    		}
    	} else {
    		Set<E> selectedElements = new HashSet<E>();
    		for (PK id : selectedIds) {
    			selectedElements.add(manager.get(id));
    		}
    		setToUpdate.retainAll(selectedElements);
    		setToUpdate.addAll(selectedElements);
    	}
    }

    /**
     * Convenience method for setting a "from" parameter to indicate the previous page.
     * 
     * @param from
     *            indicator for the originating page
     */
    public void setFrom(String from) {
        this.from = from;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public List<Status> getStatusList() {
        return Status.getAsList();
    }
}
