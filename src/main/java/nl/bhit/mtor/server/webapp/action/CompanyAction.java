package nl.bhit.mtor.server.webapp.action;

import java.util.List;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.dao.SearchException;
import nl.bhit.mtor.model.Company;
import nl.bhit.mtor.server.webapp.util.UserManagementUtils;
import nl.bhit.mtor.service.CompanyManager;
import nl.bhit.mtor.service.ProjectManager;

import com.opensymphony.xwork2.Preparable;

public class CompanyAction extends BaseAction implements Preparable {
	private CompanyManager companyManager;
	private ProjectManager projectManager;
	private List companies;
	private Company company;
	private Long id;
	private String query;

	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	public List getCompanies() {
		return companies;
	}

	/**
	 * Grab the entity from the database before populating with request parameters
	 */
	@Override
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
				getCompaniesForUser();
			} else {
				getCompaniesForAdmin();
			}
		} catch (SearchException se) {
			addActionError(se.getMessage());
			getCompaniesForAdmin();
		}
		return SUCCESS;
	}

	protected void getCompaniesForAdmin() {
		companies = companyManager.getAllDistinct();
	}

	protected void getCompaniesForUser() {
		companies = companyManager.getAllByUSer(UserManagementUtils.getAuthenticatedUser());
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