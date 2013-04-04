package nl.bhit.mtor.server.webapp.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.dao.SearchException;
import nl.bhit.mtor.model.Company;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.server.webapp.util.UserManagementUtils;
import nl.bhit.mtor.service.CompanyManager;
import nl.bhit.mtor.service.ProjectManager;

import com.opensymphony.xwork2.Preparable;

public class ProjectAction extends BaseAction implements Preparable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7012940279849835576L;
	
    /**
     * Enumeration of all parameter names used by this action.
     * 
     * @author admindes
     *
     */
    private enum REQUEST_PARAMS {
    	
    	USERS_IDS("projectUsers");
    	
    	private String name;
    	
    	private REQUEST_PARAMS(String name) {
    		this.name = name;
    	}
    	
    	public String getParamName() {
    		return name;
    	}
    }
	
	private CompanyManager companyManager;
    private ProjectManager projectManager;
    
    private Project project;
    private Long id;
    private String query;
    
    private List<Project> projects;
    private List<Company> companies;
    private List<User> users;
    private List<String> assignedUsersIds;

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public void setCompanyManager(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    public List<Project> getProjects() {
        return projects;
    }
    
    /**
     * Populates assignedUsersIds attribute with all the user's ids assigned to this project.
     * It should be a string list and its used in order to make the default list selection.
     * 
     * @return		List with user's id assigned to the current project.
     */
    public List<String> getAssignedUsers() {
    	if (assignedUsersIds == null) {
    		assignedUsersIds = new ArrayList<String>();
    	}
    	assignedUsersIds.clear();
    	if (project.getUsers() != null) {
	    	for (final User u : project.getUsers()) {
	    		assignedUsersIds.add(String.valueOf(u.getId()));
	    	}
    	}
    	return assignedUsersIds;
    }

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String projectId = getRequest().getParameter("project.id");
            if (projectId != null && !projectId.equals("")) {
                project = projectManager.get(new Long(projectId));
            }
        }
    }

    public void setQ(String q) {
        this.query = q;
    }

    public String list() {
        try {
            if (!getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
                Collection<Project> projectsNew = new LinkedHashSet<Project>(projectManager.search(query, Project.class));
                List<Project> tempProjects = new ArrayList<Project>(projectsNew);
                String loggedInUser = UserManagementUtils.getAuthenticatedUser().getFullName();
                projects = new ArrayList<Project>();
                for (Project tempProject : tempProjects) {
                    Set<User> projectUsers = tempProject.getUsers();
                    for (User projectUser : projectUsers) {
                        if (projectUser.getFullName().equalsIgnoreCase(loggedInUser)) {
                            projects.add(tempProject);
                        }
                    }
                }
            } else {
                projects = projectManager.getAllDistinct();
            }
        } catch (SearchException se) {
            addActionError(se.getMessage());
            projects = projectManager.getAllDistinct();
        }
        return SUCCESS;
    }

    public List<Company> getCompanyList() {
        if (!getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
            List<Project> tempProjects = projectManager.getAllDistinct();
            String loggedInUser = UserManagementUtils.getAuthenticatedUser().getFullName();
            List<Project> projects = new ArrayList<Project>();
            for (Project tempProject : tempProjects) {
                Set<User> projectUsers = tempProject.getUsers();
                for (User projectUser : projectUsers) {
                    if (projectUser.getFullName().equalsIgnoreCase(loggedInUser)) {
                        projects.add(tempProject);
                    }
                }
            }
            List<Company> tempCompanies = new ArrayList<Company>();
            for (Project project : projects) {
                tempCompanies.add(project.getCompany());
            }
            Collection<Company> companiesNew = new LinkedHashSet<Company>(tempCompanies);
            companies = new ArrayList<Company>(companiesNew);
        } else {
            companies = companyManager.getAllDistinct();
        }
        return companies;
    }

    public List<User> getUserList() {
        users = userManager.getAllDistinct();
        return users;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String delete() {
        projectManager.remove(project.getId());
        saveMessage(getText("project.deleted"));

        return SUCCESS;
    }

    public String edit() {
        if (id != null) {
            project = projectManager.get(id);
        } else {
            project = new Project();
        }
        
        if (project.getUsers() == null) {
        	project.setUsers(new HashSet<User>());
        }
        addElementsByLongId(userManager, project.getUsers(), 
        					getRequest().getParameterValues(REQUEST_PARAMS.USERS_IDS.getParamName()), 
        					false);
        
        return SUCCESS;
    }

    public String save() {
        if (cancel != null) {
            return "cancel";
        }

        if (delete != null) {
            return delete();
        }
        
        if (project.getUsers() == null) {
        	project.setUsers(new HashSet<User>());
        }
        addElementsByLongId(userManager, project.getUsers(), 
        					getRequest().getParameterValues(REQUEST_PARAMS.USERS_IDS.getParamName()), 
        					true);
        
        boolean isNew = project.getId() == null;
        
        projectManager.save(project);

        String key = isNew ? "project.added" : "project.updated";
        saveMessage(getText(key));

        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}