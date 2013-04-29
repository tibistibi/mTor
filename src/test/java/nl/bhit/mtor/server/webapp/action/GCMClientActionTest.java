package nl.bhit.mtor.server.webapp.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import nl.bhit.mtor.model.GCMClient;
import nl.bhit.mtor.service.GCMClientManager;

import org.apache.struts2.ServletActionContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.opensymphony.xwork2.ActionSupport;

public class GCMClientActionTest extends BaseActionTestCase {
    private GCMClientAction action;

    @Override
    @Before
    public void onSetUp() {
        super.onSetUp();

        action = new GCMClientAction();
        GCMClientManager gCMClientManager = (GCMClientManager) applicationContext.getBean("gCMClientManager");
        action.setGCMClientManager(gCMClientManager);

        // add a test gCMClient to the database
        GCMClient gCMClient = new GCMClient();

        // enter all required fields

        gCMClientManager.save(gCMClient);
    }

    @Test
    public void testGetAllGCMClients() throws Exception {
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertTrue(action.getGCMClients().size() >= 1);
    }

    @Test
    public void testSearch() throws Exception {
        // regenerate indexes
        GCMClientManager gCMClientManager = (GCMClientManager) applicationContext.getBean("gCMClientManager");
        gCMClientManager.reindex();

        action.setQ("*");
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertEquals(4, action.getGCMClients().size());
    }

    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setId(-1L);
        assertNull(action.getGCMClient());
        assertEquals("success", action.edit());
        assertNotNull(action.getGCMClient());
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setId(-1L);
        assertEquals("success", action.edit());
        assertNotNull(action.getGCMClient());

        GCMClient gCMClient = action.getGCMClient();
        // update required fields

        action.setGCMClient(gCMClient);

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
        GCMClient gCMClient = new GCMClient();
        gCMClient.setId(-2L);
        action.setGCMClient(gCMClient);
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}