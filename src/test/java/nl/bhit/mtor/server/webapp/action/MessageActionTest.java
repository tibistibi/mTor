package nl.bhit.mtor.server.webapp.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.server.webapp.action.MessageAction;
import nl.bhit.mtor.service.MessageManager;

import org.apache.struts2.ServletActionContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.opensymphony.xwork2.ActionSupport;

public class MessageActionTest extends BaseActionTestCase {
    @Autowired
    private MessageAction action;
    @Autowired
    private MessageManager messageManager;

    @Override
    @Before
    public void onSetUp() {
        super.onSetUp();

        // action.setMessageManager(messageManager);
        MTorMessage testMessage = messageManager.get(-1L);
        // add a test message to the database
        MTorMessage message = new MTorMessage();

        // enter all required fields
        message.setContent("NuIiVuNwUzUwGySmEfAyHsZjCyYdFzEdXeChOlMkLrKeYdEqEnAwOyAsBtBeHlWuWvHbCyTeGtFhMkDsNqHuHkLuWcMgUrFpShFtUuSkJtZbZtKmXjQhUqCgEzQmThMyVvQwPmDiJjVhYqTrCiIbVqPgHfAqDhRlLpSoSpDgMlDdSaGsEyGhQdIpWkMuAjKhCuTmLcPvPlJxVsFfVcVkZyRzWmVyJkTjSgOoNwNhAbPzAzFjUoGlMsXtPpAkBdJ");
        message.setTimestamp(new java.util.Date());
        message.setProject(testMessage.getProject());
        messageManager.save(message);

        logUserIntoSession(-1L);
    }

    @Test
    public void testGetAllMessages() throws Exception {
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertTrue(action.getMTorMessages().size() >= 1);
    }

    @Test
    public void testSearch() throws Exception {
        // regenerate indexes
        MessageManager messageManager = (MessageManager) applicationContext.getBean("messageManager");
        messageManager.reindex();

        action.setQ("*");
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertEquals("retrive only messages which belong to the logged in user", 2, action.getMTorMessages().size());
    }

    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setId(-1L);
        assertNull(action.getMessage());
        assertEquals("success", action.edit());
        assertNotNull(action.getMessage());
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setId(-1L);
        assertEquals("success", action.edit());
        assertNotNull(action.getMessage());

        MTorMessage message = action.getMessage();
        // update required fields
        message.setContent("DpHuGrOtKxArDaPiUvDhZfXuOfUwAvFwKeQqAbRaIsDlXxRuPsSyXpWuOcNuVvMsNjXzXnWfWwTfPvLpMlOpEkMoUaZgXfOxUuWgNqOuTwKpXmNvLaFhWdPgOaXuPcJhBrMhGgRlRnLoLiZtMdHlMrGrMaJbQqKxSrTtQgThZkJoExLiPfQkOcRhNgIfBnWsSbFbBkJxAqBcCfOfCsTwJrUmFeUpWpCnCfIwSqOyLxLqHqMrDwMzBkZhKsTqGsN");
        message.setTimestamp(new java.util.Date());

        action.setMessage(message);

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
        MTorMessage message = new MTorMessage();
        message.setId(-2L);
        action.setMessage(message);
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}