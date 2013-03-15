package nl.bhit.mtor.server.webapp.action;

import java.util.ArrayList;
import java.util.Collection;
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
    private CompanyManager companyManager;
    private ProjectManager projectManager;
    private List projects;
    private List companies;
    private List users;
    private Project project;
    private Long id;
    private String query;

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public void setCompanyManager(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    public List getProjects() {
        return projects;
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
                Collection projectsNew = new LinkedHashSet(projectManager.search(query, Project.class));
                List<Project> tempProjects = new ArrayList(projectsNew);
                String loggedInUser = UserManagementUtils.getAuthenticatedUser().getFullName();
                projects = new ArrayList();
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

    public List getCompanyList() {
        if (!getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
            List<Project> tempProjects = projectManager.getAllDistinct();
            String loggedInUser = UserManagementUtils.getAuthenticatedUser().getFullName();
            List<Project> projects = new ArrayList();
            for (Project tempProject : tempProjects) {
                Set<User> projectUsers = tempProject.getUsers();
                for (User projectUser : projectUsers) {
                    if (projectUser.getFullName().equalsIgnoreCase(loggedInUser)) {
                        projects.add(tempProject);
                    }
                }
            }
            List<Company> tempCompanies = new ArrayList();
            for (Project project : projects) {
                tempCompanies.add(project.getCompany());
            }
            Collection companiesNew = new LinkedHashSet(tempCompanies);
            companies = new ArrayList(companiesNew);
        } else {
            companies = companyManager.getAllDistinct();
        }
        return companies;
    }

    public List getUserList() {
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
        String[] projectUsers = getRequest().getParameterValues("projectUsers");

        for (int i = 0; projectUsers != null && i < projectUsers.length; i++) {
            String userName = projectUsers[i];
            project.addUser(userManager.getUser(userName));
        }

        return SUCCESS;
    }

    public String save() throws Exception {
        if (cancel != null) {
            return "cancel";
        }

        if (delete != null) {
            return delete();
        }

        if (project.getUsers() != null) {
            project.getUsers().clear();
        }
        String[] projectUsers = getRequest().getParameterValues("projectUsers");

        for (int i = 0; projectUsers != null && i < projectUsers.length; i++) {
            String userName = projectUsers[i];
            project.addUser(userManager.getUser(userName));
        }

        boolean isNew = (project.getId() == null);

        projectManager.save(project);

        String key = (isNew) ? "project.added" : "project.updated";
        saveMessage(getText(key));

        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}