package nl.bhit.service.impl;

import java.util.ArrayList;
import java.util.List;

import nl.bhit.dao.MessageDao;
import nl.bhit.model.MTorMessage;
import nl.bhit.service.impl.BaseManagerMockTestCase;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageManagerImplTest extends BaseManagerMockTestCase {
    private MessageManagerImpl manager = null;
    private MessageDao dao = null;

    @Before
    public void setUp() {
        dao = context.mock(MessageDao.class);
        manager = new MessageManagerImpl(dao);
    }

    @After
    public void tearDown() {
        manager = null;
    }

    @Test
    public void testGetMessage() {
        log.debug("testing get...");

        final Long id = 7L;
        final MTorMessage message = new MTorMessage();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).get(with(equal(id)));
            will(returnValue(message));
        }});

        MTorMessage result = manager.get(id);
        assertSame(message, result);
    }

    @Test
    public void testGetMessages() {
        log.debug("testing getAll...");

        final List messages = new ArrayList();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).getAll();
            will(returnValue(messages));
        }});

        List result = manager.getAll();
        assertSame(messages, result);
    }

    @Test
    public void testSaveMessage() {
        log.debug("testing save...");

        final MTorMessage message = new MTorMessage();
        // enter all required fields
        message.setContent("IzCrGzWyVeZfKcOqPpEhIySiNdPxCzCfAqBgZlNiEsGsVgHlBaGdZqGdUxXtUoHjGdBhRxGnXsGzQmTpBzIrDaCrRmRgLaNnXpKeMqGoDjNnYmGkHaDvMuQaEiQkZeEgCyEzCmXsIxPzBmPxUnBlQjFcFsAwNdVpRvUeQsCaHuLoVuKxHtLbIcZbShKfVfGeBlRuAoGcSjDmVlAfMaRwVoKjDmLtTtYqCwBkOjTbNoBtCkJiUrZgSdWwFqEgYiP");
        message.setTimestamp(new java.util.Date());
        
        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).save(with(same(message)));
        }});

        manager.save(message);
    }

    @Test
    public void testRemoveMessage() {
        log.debug("testing remove...");

        final Long id = -11L;

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).remove(with(equal(id)));
        }});

        manager.remove(id);
    }
}