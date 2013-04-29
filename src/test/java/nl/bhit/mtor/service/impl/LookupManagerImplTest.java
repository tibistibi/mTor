package nl.bhit.mtor.service.impl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.dao.LookupDao;
import nl.bhit.mtor.model.LabelValue;
import nl.bhit.mtor.model.Role;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

public class LookupManagerImplTest extends BaseManagerMockTestCase {
	
    private LookupManagerImpl mgr = new LookupManagerImpl();
    private LookupDao lookupDao;

    @Before
    public void setUp() throws Exception {
        lookupDao = context.mock(LookupDao.class);
        mgr.dao = lookupDao;
    }

    @Test
    public void testGetAllRoles() {
        LOG.debug("entered 'testGetAllRoles' method");

        // set expected behavior on dao
        Role role = new Role(Constants.ADMIN_ROLE);
        final List<Role> testData = new ArrayList<Role>();
        testData.add(role);
        context.checking(new Expectations() {
            {
                one(lookupDao).getRoles();
                will(returnValue(testData));
            }
        });

        List<LabelValue> roles = mgr.getAllRoles();
        assertTrue(roles.size() > 0);
    }
}
