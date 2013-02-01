package nl.bhit.webapp.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import nl.bhit.service.GenericManager;
import nl.bhit.model.Project;
import nl.bhit.webapp.action.BaseActionTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProjectActionTest extends BaseActionTestCase {
    private ProjectAction action;

    @Before
    public void onSetUp() {
        super.onSetUp();

        action = new ProjectAction();
        GenericManager projectManager = (GenericManager) applicationContext.getBean("projectManager");
        action.setProjectManager(projectManager);

        // add a test project to the database
        Project project = new Project();

        // enter all required fields
        project.setName("" + Math.random());

        projectManager.save(project);
    }

    @Test
    public void testGetAllProjects() throws Exception {
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertTrue(action.getProjects().size() >= 1);
    }

    @Test
    public void testSearch() throws Exception {
        // regenerate indexes
        GenericManager<Project, Long> projectManager = (GenericManager<Project, Long>) applicationContext.getBean("projectManager");
        projectManager.reindex();

        action.setQ("*");
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertEquals(4, action.getProjects().size());
    }

    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setId(-1L);
        assertNull(action.getProject());
        assertEquals("success", action.edit());
        assertNotNull(action.getProject());
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setId(-1L);
        assertEquals("success", action.edit());
        assertNotNull(action.getProject());

        Project project = action.getProject();
        // update required fields
        project.setName("VdHuDmErGkEnFdAvMnBoYmXkIbCqNnLoPlItWlCbOlYlUcWrOkAdXnDlQwMeQoIqKkLcSfHkSeMaNoZsNsWxYcXsIkHqRlTnCdRbErZnBiTsEiJnLePvUuIuTwSpYwGuTnOeWuXpEiHrHgBtApGvWoWkBuKxBeWaJmYjAkAsWcEhLvChJlPzXoJeXyPhMyDmVnEbTqTcBzCdYyQyXgPaLuGcUmHjEzKpDmKtWnMeDhSzIcIgAoRtQqBkLkUxMlM");

        action.setProject(project);

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
        Project project = new Project();
        project.setId(-2L);
        action.setProject(project);
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}