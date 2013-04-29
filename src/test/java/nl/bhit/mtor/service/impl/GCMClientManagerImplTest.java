package nl.bhit.mtor.service.impl;

import java.util.ArrayList;
import java.util.List;

import nl.bhit.mtor.dao.GCMClientDao;
import nl.bhit.mtor.model.GCMClient;
import nl.bhit.mtor.service.impl.BaseManagerMockTestCase;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class GCMClientManagerImplTest extends BaseManagerMockTestCase {
    private GCMClientManagerImpl manager = null;
    private GCMClientDao dao = null;

    @Before
    public void setUp() {
        dao = context.mock(GCMClientDao.class);
        manager = new GCMClientManagerImpl(dao);
    }

    @After
    public void tearDown() {
        manager = null;
    }

    @Test
    public void testGetGCMClient() {
        log.debug("testing get...");

        final Long id = 7L;
        final GCMClient gCMClient = new GCMClient();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).get(with(equal(id)));
            will(returnValue(gCMClient));
        }});

        GCMClient result = manager.get(id);
        assertSame(gCMClient, result);
    }

    @Test
    public void testGetGCMClients() {
        log.debug("testing getAll...");

        final List gCMClients = new ArrayList();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).getAll();
            will(returnValue(gCMClients));
        }});

        List result = manager.getAll();
        assertSame(gCMClients, result);
    }

    @Test
    public void testSaveGCMClient() {
        log.debug("testing save...");

        final GCMClient gCMClient = new GCMClient();
        // enter all required fields
        
        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).save(with(same(gCMClient)));
        }});

        manager.save(gCMClient);
    }

    @Test
    public void testRemoveGCMClient() {
        log.debug("testing remove...");

        final Long id = -11L;

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).remove(with(equal(id)));
        }});

        manager.remove(id);
    }
}