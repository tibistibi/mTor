package nl.bhit.mtor.dao;

import static org.junit.Assert.assertNotNull;
import nl.bhit.mtor.model.GCMClient;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.ExpectedException;

public class GCMClientDaoTest extends BaseDaoTestCase {
    @Autowired
    private GCMClientDao gCMClientDao;

    @Test
    @ExpectedException(DataAccessException.class)
    public void testAddAndRemoveGCMClient() {
        GCMClient gCMClient = new GCMClient();

        // enter all required fields

        LOG.debug("adding gCMClient...");
        gCMClient = gCMClientDao.save(gCMClient);

        gCMClient = gCMClientDao.get(gCMClient.getId());

        assertNotNull(gCMClient.getId());

        LOG.debug("removing gCMClient...");

        gCMClientDao.remove(gCMClient.getId());

        // should throw DataAccessException
        gCMClientDao.get(gCMClient.getId());
    }
}