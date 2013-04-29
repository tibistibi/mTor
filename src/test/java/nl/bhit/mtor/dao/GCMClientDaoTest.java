package nl.bhit.mtor.dao;

import nl.bhit.mtor.dao.BaseDaoTestCase;
import nl.bhit.mtor.model.GCMClient;
import org.springframework.dao.DataAccessException;

import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.annotation.ExpectedException;

import java.util.List;

public class GCMClientDaoTest extends BaseDaoTestCase {
    @Autowired
    private GCMClientDao gCMClientDao;

    @Test
    @ExpectedException(DataAccessException.class)
    public void testAddAndRemoveGCMClient() {
        GCMClient gCMClient = new GCMClient();

        // enter all required fields

        log.debug("adding gCMClient...");
        gCMClient = gCMClientDao.save(gCMClient);

        gCMClient = gCMClientDao.get(gCMClient.getId());

        assertNotNull(gCMClient.getId());

        log.debug("removing gCMClient...");

        gCMClientDao.remove(gCMClient.getId());

        // should throw DataAccessException 
        gCMClientDao.get(gCMClient.getId());
    }
}