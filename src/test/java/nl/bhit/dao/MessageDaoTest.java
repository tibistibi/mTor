package nl.bhit.dao;

import nl.bhit.dao.BaseDaoTestCase;
import nl.bhit.model.MTorMessage;
import org.springframework.dao.DataAccessException;

import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.annotation.ExpectedException;

import java.util.List;

public class MessageDaoTest extends BaseDaoTestCase {
    @Autowired
    private MessageDao messageDao;

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
}