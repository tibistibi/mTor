package nl.bhit.mtor.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.List;

import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.Status;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.ExpectedException;

public class MessageDaoTest extends BaseDaoTestCase {
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private ProjectDao projectDao;

	@Test
	@ExpectedException(DataAccessException.class)
	public void testAddAndRemoveMessage() {
		MTorMessage message = new MTorMessage();

		// enter all required fields
		message.setContent("JcVhYnKfBkNeWbBvCjTlXpKsYaHiXpHnOdNzKoFdAjPbVcDmLvPrEuLfCnIeNtNyApAoGgXyNkXfJjJdCdUwSgOiWiRbXdVhQlPnSeLzClLhFbZvIvEkCvJoRdNqHtChPbMfTpXzEfAdDwFsTlSbPlImUfIsYsDvCoRwYlFjKcVzQxRiDfCdQiHwPiGsYzTiIrWyUnSmCpTsPzKtOvGyYfFhGjVaAbMcQpOtDyKfYmErJeRpTmHmFbNsOmYvTpA");
		message.setTimestamp(new java.util.Date());

		log.debug("adding message...");
		message = messageDao.save(message);

		message = messageDao.get(message.getId());

		assertNotNull(message.getId());

		log.debug("removing message...");

		messageDao.remove(message.getId());

		// should throw DataAccessException
		messageDao.get(message.getId());
	}

	@Test
	public void testGetMessagesWithTimestampBase() {
		log.debug("starting testGetMessagesWithTimestamp...");
		MTorMessage message = createMessage(new Date(), projectDao.get(-1L));
		List<MTorMessage> messages = messageDao.getMessagesWithTimestamp(message);
		assertEquals(1, messages.size());
	}

	@Test
	public void testGetMessagesWithTimestampAddedNewerSameProject() {
		MTorMessage message = createMessage(new Date(), projectDao.get(-1L));
		messageDao.save(createMessage(DateUtils.addDays(new Date(), 1), projectDao.get(-1L)));
		List<MTorMessage> messages = messageDao.getMessagesWithTimestamp(message);
		assertEquals("message added which is newer, should have no effect", 1, messages.size());
	}

	@Test
	public void testGetAliveByProject() {
		log.trace("starting testGetAliveByProject");
		long projectId = -1L;
		messageDao.save(createMessage(DateUtils.addDays(new Date(), 1), projectDao.get(projectId)));
		MTorMessage message = messageDao.getAliveByProject(projectId);
		assertNull(message);
		message = createMessage(new Date(), projectDao.get(projectId));
		message.setContent("I am alive!");
		message.setStatus(Status.INFO);
		messageDao.save(message);
		message = messageDao.getAliveByProject(projectId);
		assertNotNull(message);

	}

	@Test
	public void testGetMessagesWithTimestampAddedOlderSameProject() {
		MTorMessage message = createMessage(new Date(), projectDao.get(-1L));
		messageDao.save(createMessage(DateUtils.addDays(new Date(), -1), projectDao.get(-1L)));
		List<MTorMessage> messages = messageDao.getMessagesWithTimestamp(message);
		assertEquals("older message added", 2, messages.size());
	}

	@Test
	public void testGetMessagesWithTimestampAddedOlderDiffProject() {
		MTorMessage message = createMessage(new Date(), projectDao.get(-1L));
		messageDao.save(createMessage(DateUtils.addDays(new Date(), -1), projectDao.get(-2L)));
		List<MTorMessage> messages = messageDao.getMessagesWithTimestamp(message);
		assertEquals("older message added", 1, messages.size());
	}

	protected MTorMessage createMessage(Date date, Project project) {
		MTorMessage message = new MTorMessage();
		message.setTimestamp(date);
		message.setProject(project);
		message.setContent("test message from unit test MessageDaoTest");
		return message;
	}

}