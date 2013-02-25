package nl.bhit.mtor.server.webapp.action;

import com.opensymphony.xwork2.Preparable;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.dao.SearchException;
import nl.bhit.mtor.model.Company;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.server.webapp.action.BaseAction;
import nl.bhit.mtor.server.webapp.util.UserManagementUtils;
import nl.bhit.mtor.service.GenericManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CompanyAction extends BaseAction implements Preparable {
    private GenericManager<Company, Long> companyManager;
    private GenericManager<Project, Long> projectManager;
    private List companies;
    private Company company;
    private Long id;
    private String query;

    public void setCompanyManager(GenericManager<Company, Long> companyManager) {
        this.companyManager = companyManager;
    }

    public void setProjectManager(GenericManager<Project, Long> projectManager) {
        this.projectManager = projectManager;
    }
    
    public List getCompanies() {
        return companies;
    }

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String companyId = getRequest().getParameter("company.id");
            if (companyId != null && !companyId.equals("")) {
                company = companyManager.get(new Long(companyId));
            }
        }
    }

    public void setQ(String q) {
        this.query = q;
    }

    public String list() {
        try {
        	if (!getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
	            List<Project> tempProjects = projectManager.getAllDistinct();
	            String loggedInUser = UserManagementUtils.getAuthenticatedUser().getFullName();
	            List<Project> projects = new ArrayList();
	            for(Project tempProject : tempProjects){
	            	Set<User> projectUsers = tempProject.getUsers();
	            	for (User projectUser : projectUsers){
	            		if (projectUser.getFullName().equalsIgnoreCase(loggedInUser)){
	            			projects.add(tempProject);
	            		}
	            	}
	            }
	            List<Company> tempCompanies = new ArrayList();
	            for (Project project : projects){
	            	tempCompanies.add(project.getCompany());
	            }
	            Collection companiesNew = new LinkedHashSet(tempCompanies);
	            companies = new ArrayList(companiesNew);
        	} else {
        		companies = companyManager.getAllDistinct();
        	}
        } catch (SearchException se) {
            addActionError(se.getMessage());
            companies = companyManager.getAllDistinct();
        }
        return SUCCESS;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String delete() {
        companyManager.remove(company.getId());
        saveMessage(getText("company.deleted"));

        return SUCCESS;
    }

    public String edit() {
        if (id != null) {
            company = companyManager.get(id);
        } else {
            company = new Company();
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

        boolean isNew = (company.getId() == null);

        companyManager.save(company);

        String key = (isNew) ? "company.added" : "company.updated";
        saveMessage(getText(key));

        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}