package nl.bhit.webapp.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import nl.bhit.service.GenericManager;
import nl.bhit.model.Company;
import nl.bhit.webapp.action.BaseActionTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompanyActionTest extends BaseActionTestCase {
    private CompanyAction action;

    @Before
    public void onSetUp() {
        super.onSetUp();

        action = new CompanyAction();
        GenericManager companyManager = (GenericManager) applicationContext.getBean("companyManager");
        action.setCompanyManager(companyManager);

        // add a test company to the database
        Company company = new Company();

        // enter all required fields
        company.setName("" + Math.random());

        companyManager.save(company);
    }

    @Test
    public void testGetAllCompanies() throws Exception {
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertTrue(action.getCompanies().size() >= 1);
    }

    @Test
    public void testSearch() throws Exception {
        // regenerate indexes
        GenericManager<Company, Long> companyManager = (GenericManager<Company, Long>) applicationContext.getBean("companyManager");
        companyManager.reindex();

        action.setQ("*");
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertEquals(4, action.getCompanies().size());
    }

    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setId(-1L);
        assertNull(action.getCompany());
        assertEquals("success", action.edit());
        assertNotNull(action.getCompany());
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setId(-1L);
        assertEquals("success", action.edit());
        assertNotNull(action.getCompany());

        Company company = action.getCompany();
        // update required fields
        company.setName("FnJvUxAjMbBtVqGdDvLcGbDnUlUcPpGrYbIdCeFmAeFeOiEoQbJhCdPzFwBnJlMcHzPiMoHfRmVoJiCiJdGrKzPoCgCgVlWaLqXcRcPdSlQjXpMsJbMkLwLdSpMhNmWvRvQkVdLhRsTdPqJaQuJlNuZtJvBmNtJoIkTnTgGmKrXmByRyOiWmVxAtJiDrKxDfUiCnYvXgQlFdLsFePrKvZbUiXfNxImXsTbZtIoSvKwUmOwJfGpBrPoBbMdCdUkD");

        action.setCompany(company);

        assertEquals("input", action.save());
        assertFalse(action.hasActionErrors());
        assertFalse(action.hasFieldErrors());
        assertNotNull(request.getSession().getAttribute("messages"));
    }

    @Test
    public void testRemove() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        Company company = new Company();
        company.setId(-2L);
        action.setCompany(company);
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}