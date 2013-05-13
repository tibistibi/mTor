package nl.bhit.mtor.server.webapp.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.model.Company;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.server.webapp.util.UserManagementUtils;
import nl.bhit.mtor.service.CompanyManager;
import nl.bhit.mtor.service.ProjectManager;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.Preparable;

public class ProjectAction extends BaseAction implements Preparable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 7012940279849835576L;
	
	private transient CompanyManager companyManager;
    private transient ProjectManager projectManager;
    
    private Project project;
    private Long id;
    private String query;
    
    private List<Project> projects;
    private List<Company> companies;
    private List<User> users;
    private List<String> assignedUsersIds;
    private List<Map<String, String>> jsonProjectsList = new ArrayList<Map<String,String>>();

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
     * @return List with user's id assigned to the current project.
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
    @Override
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
        if (!getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
            projects = projectManager.getWithNonResolvedMessages(UserManagementUtils.getAuthenticatedUser());
        } else {
            projects = projectManager.getAllDistinct();
        }
        return SUCCESS;
    }

    public List<Company> getCompanyList() {
        if (!getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
            List<Project> tempProjects = projectManager.getAllDistinct();
            String loggedInUser = UserManagementUtils.getAuthenticatedUser().getFullName();
            List<Project> lstProjects = new ArrayList<Project>();
            for (Project tempProject : tempProjects) {
                Set<User> projectUsers = tempProject.getUsers();
                for (User projectUser : projectUsers) {
                    if (projectUser.getFullName().equalsIgnoreCase(loggedInUser)) {
                        lstProjects.add(tempProject);
                    }
                }
            }
            List<Company> tempCompanies = new ArrayList<Company>();
            for (Project p : lstProjects) {
                tempCompanies.add(p.getCompany());
            }
            Collection<Company> companiesNew = new LinkedHashSet<Company>(tempCompanies);
            companies = new ArrayList<Company>(companiesNew);
        } else {
            companies = companyManager.getAllDistinct();
        }
        return companies;
    }

    public List<Map<String, String>> getJsonProjectsList() {
    	jsonProjectsList.clear();
    	if (projects == null || projects.isEmpty()) {
    		return jsonProjectsList;
    	}
    	
    	final String sortBy = (String)getRequest().getParameter("sortBy");
    	final String sortOrder = (String)getRequest().getParameter("sortOrder");
    	if (!StringUtils.isEmpty(sortBy) && !StringUtils.isEmpty(sortOrder)) {
	    	Collections.sort(projects, new Comparator<Project>() {
				@Override
				public int compare(Project p1, Project p2) {
					boolean asc = sortOrder.equalsIgnoreCase("asc");
					if (sortBy.equalsIgnoreCase("id")) {
						return asc ? p2.getId().compareTo(p1.getId()) : p1.getId().compareTo(p2.getId());
					} else if (sortBy.equalsIgnoreCase("name")) {
						return asc ? p1.getName().compareTo(p2.getName()) : p2.getName().compareTo(p1.getName());
					} else if (sortBy.equalsIgnoreCase("status")) {
						return asc ? p1.statusOfProject().compareTo(p2.statusOfProject()) : p2.statusOfProject().compareTo(p1.statusOfProject());
					} else {
						return asc ? Boolean.valueOf(p1.isMonitoring()).compareTo(p2.isMonitoring()) : Boolean.valueOf(p2.isMonitoring()).compareTo(p1.isMonitoring());
					}
				}
			});
    	}
    	
    	String userNames;
    	Map<String, String> mAux;
    	for (final Project p : projects) {
    		mAux = new HashMap<String, String>();
    		mAux.put("id", String.valueOf(p.getId()));
    		mAux.put("name", p.getName());
    		mAux.put("status", p.statusOfProject());
    		userNames = "[";
    		if (p.userNames() != null) {
    			int i = 0;
    			int n = p.userNames().size() - 1;
    			Iterator<String> itName = p.userNames().iterator();
    			while (itName.hasNext()) {
    				userNames += itName.next();
    				if (i++ < n) {
    					userNames += ", ";
    				}
    			}
    		}
    		userNames += "]";
    		mAux.put("usernames", userNames);
    		mAux.put("monitoring", String.valueOf(p.isMonitoring()));
    		jsonProjectsList.add(mAux);
    	}
    	
		return jsonProjectsList;
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
                getRequest().getParameterValues("projectUsers"), false);

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
                getRequest().getParameterValues("projectUsers"), true);

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