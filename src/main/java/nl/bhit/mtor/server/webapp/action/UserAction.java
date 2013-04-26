package nl.bhit.mtor.server.webapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.dao.SearchException;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.Role;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.server.webapp.util.RequestUtil;
import nl.bhit.mtor.service.ProjectManager;
import nl.bhit.mtor.service.UserExistsException;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.Preparable;

/**
 * Action for facilitating User Management feature.
 */
public class UserAction extends BaseAction implements Preparable {
	
    private static final long serialVersionUID = 6776558938712115191L;
    
    private transient ProjectManager projectManager;
    
    private User user;
    private String id;
    private String query;
    
    private List<User> users;
    private List<Project> projects;
    private List<String> assignedProjectsIds;
    private List<Role> roles;
    private List<String> assignedRolesIds;
    
    @Autowired
    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        // prevent failures on new
        if (getRequest().getMethod().equalsIgnoreCase("post") && !"".equals(getRequest().getParameter("user.id"))) {
            user = userManager.getUser(getRequest().getParameter("user.id"));
        }
    }

    /**
     * Holder for users to display on list screen
     * 
     * @return list of users
     */
    public List<User> getUsers() {
        return users;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQ(String q) {
        this.query = q;
    }

    /**
     * Delete the user passed in.
     * 
     * @return success
     */
    public String delete() {
        userManager.removeUser(user.getId().toString());
        List<Object> args = new ArrayList<Object>();
        args.add(user.getFullName());
        saveMessage(getText("user.deleted", args));

        return SUCCESS;
    }

    /**
     * Grab the user from the database based on the "id" passed in.
     * 
     * @return success if user found
     * @throws IOException
     *             can happen when sending a "forbidden" from response.sendError()
     */
    public String edit() throws IOException {
        HttpServletRequest request = getRequest();
        boolean editProfile = request.getRequestURI().contains("editProfile");

        // if URL is "editProfile" - make sure it's the current user
        if (editProfile && (request.getParameter("id") != null || request.getParameter("from") != null)) {
            ServletActionContext.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            LOG.warn("User '" + request.getRemoteUser() + "' is trying to edit user '" + request.getParameter("id")
                    + "'");
            return null;
        }

        // if a user's id is passed in
        if (id != null) {
            // lookup the user using that id
            user = userManager.getUser(id);
        } else if (editProfile) {
            user = userManager.getUserByUsername(request.getRemoteUser());
        } else {
            user = new User();
            user.addRole(new Role(Constants.USER_ROLE));
        }
        
        String[] selectedProjectIds = getRequest().getParameterValues("projects");
        for (int i = 0; selectedProjectIds != null && i < selectedProjectIds.length; i++) {
            user.addProject(projectManager.get(Long.valueOf(selectedProjectIds[i])));  
        }
        
        if (user.getUsername() != null) {
            user.setConfirmPassword(user.getPassword());

            // if user logged in with remember me, display a warning that they can't change passwords
            LOG.debug("checking for remember me login...");

            AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
            SecurityContext ctx = SecurityContextHolder.getContext();

            if (ctx != null) {
                Authentication auth = ctx.getAuthentication();

                if (resolver.isRememberMe(auth)) {
                    getSession().setAttribute("cookieLogin", "true");
                    saveMessage(getText("userProfile.cookieLogin"));
                }
            }
        }

        return SUCCESS;
    }

    /**
     * Default: just returns "success"
     * 
     * @return "success"
     */
    public String execute() {
        return SUCCESS;
    }

    /**
     * Sends users to "mainMenu" when !from.equals("list"). Sends everyone else to "cancel"
     * 
     * @return "mainMenu" or "cancel"
     */
    public String cancel() {
        if (!"list".equals(from)) {
            return "mainMenu";
        }
        return "cancel";
    }

    /**
     * Save user
     * 
     * @return success if everything worked, otherwise input
     * @throws IOException
     *             when setting "access denied" fails on response
     */
    public String save() throws IOException {
    	
        Integer originalVersion = user.getVersion();

        boolean isNew = "".equals(getRequest().getParameter("user.version"));
        
        // only attempt to change roles if user is admin
        // for other users, prepare() method will handle populating
        if (getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
	        try {
	            updateRoles(originalVersion);
	        } catch (DataIntegrityViolationException e) {
	            return showUserExistsException(originalVersion);
	        }
        }
        
        updateProjects();
        
        user.setEmail(user.getUsername());

        try {
            userManager.saveUser(user);
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            LOG.warn(ade.getMessage());
            getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } catch (UserExistsException e) {
            return showUserExistsException(originalVersion);
        }

        if (!"list".equals(from)) {
            // add success messages
            saveMessage(getText("user.saved"));
            return "mainMenu";
        }

        // add success messages
        List<Object> args = new ArrayList<Object>();
        args.add(user.getFullName());
        if (isNew) {
            saveMessage(getText("user.added", args));
            // Send an account information e-mail
            mailMessage.setSubject(getText("signup.email.subject"));
            try {
                sendUserMessage(user, getText("newuser.email.message", args), RequestUtil.getAppURL(getRequest()));
            } catch (MailException me) {
                addActionError(me.getCause().getLocalizedMessage());
            }
            return SUCCESS;
        }
        user.setConfirmPassword(user.getPassword());
        saveMessage(getText("user.updated.byAdmin", args));
        return INPUT;
    }

    private String showUserExistsException(Integer originalVersion) {
        List<Object> args = new ArrayList<Object>();
        args.add(user.getUsername());
        args.add(user.getEmail());
        addActionError(getText("errors.existing.user", args));

        // reset the version # to what was passed in
        user.setVersion(originalVersion);
        // redisplay the unencrypted passwords
        user.setPassword(user.getConfirmPassword());
        return INPUT;
    }
    
    /**
     * Collects the roles selected by admin and adds them.
     * 
     * @param originalVersion
     * 							Version of the current user entity
     * @throws DataIntegrityViolationException
     * 									 	Thrown when an attempt to add some role results in violation of an integrity constraint
     */
    private void updateRoles(final Integer originalVersion) throws DataIntegrityViolationException{
    	// APF-788: Removing roles from user doesn't work
        final Set<Role> selectedRoles = new HashSet<Role>();
        String[] strRoleIds = getRequest().getParameterValues("userRoles");
        for (int i = 0; strRoleIds != null && i < strRoleIds.length; i++) {
        	selectedRoles.add(roleManager.getRole(strRoleIds[i]));
        }
        user.addRoles(selectedRoles);
    }
    
    /**
     * Collects the projects selected by user and adds them.
     */
    private void updateProjects() {
    	final Set<Project> selectedProjects = new HashSet<Project>();
        String[] strProjectIds = getRequest().getParameterValues("projects");
        if (strProjectIds != null && strProjectIds.length > 0) {
        	for (String strId : strProjectIds) {
        		selectedProjects.add(projectManager.get(Long.valueOf(strId)));
        	}
        }
        user.addProjects(selectedProjects);
    }
    
    /**
     * Fetch all users from database and put into local "users" variable for retrieval in the UI.
     * 
     * @return "success" if no exceptions thrown
     */
    public String list() {
        try {
            users = userManager.search(query);
            Collection<User> usersNew = new LinkedHashSet<User>(users);
            users = new ArrayList<User>(usersNew);
        } catch (SearchException se) {
            addActionError(se.getMessage());
            users = userManager.getUsers();
        }
        return SUCCESS;
    }

    /**
     * Obtains all available projects from the database.
     * 
     * @return		All available projects.
     */
    public List<Project> getProjectList() {
    	//REVIEW: its correct return all projects from DB? Maybe only all of this projects associated with the company of the current user?
        projects = projectManager.getAllDistinct();
        return projects;
    }
    
    /**
     * Populates assignedProjectsIds attribute with all the project's ids assigned to this user.
     * It should be a string list and its used in order to make the default list selection.
     * 
     * @return		List with project's id assigned to the current user.
     */
    public List<String> getAssignedProjects() {
    	if (assignedProjectsIds == null) {
    		assignedProjectsIds = new ArrayList<String>();
    	}
    	assignedProjectsIds.clear();
    	if (user.getProjects() != null) {
	    	for (final Project p : user.getProjects()) {
	    		assignedProjectsIds.add(String.valueOf(p.getId()));
	    	}
    	}
    	return assignedProjectsIds;
    }
    
    /**
     * Obtains all available roles from the database.
     * 
     * @return		All available roles.
     */
    public List<Role> getRoleList() {
    	roles = roleManager.getAllDistinct();
    	return roles;
    }
    
    /**
     * Populates assignedRolesIds attribute with all the role's ids assigned to this user.
     * It should be a string list and its used in order to make the default list selection.
     * 
     * @return		List with role's id assigned to the current user.
     */
    public List<String> getAssignedRoles() {
    	if (assignedRolesIds == null) {
    		assignedRolesIds = new ArrayList<String>();
    	}
    	assignedRolesIds.clear();
    	if (user.getRoles() != null) {
	    	for (final Role r : user.getRoles()) {
	    		assignedRolesIds.add(String.valueOf(r.getName()));
	    	}
    	}
    	return assignedRolesIds;
    }
    
}
