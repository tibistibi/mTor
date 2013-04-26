package nl.bhit.mtor.service;

import static org.junit.Assert.assertNotNull;
import nl.bhit.mtor.model.User;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(
        locations = { "/applicationContext-service.xml", "/applicationContext-resources.xml",
                "classpath:/applicationContext-dao.xml" })
public class UserExistsExceptionTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static final transient Logger LOG = Logger.getLogger(UserExistsExceptionTest.class);
	
    @Autowired
    private UserManager manager;

    @Test
    @ExpectedException(UserExistsException.class)
    public void testAddExistingUser() throws Exception {
        LOG.debug("entered 'testAddExistingUser' method");
        assertNotNull(manager);

        User user = manager.getUser("-1");

        // create new object with null id - Hibernate doesn't like setId(null)
        User user2 = new User();
        BeanUtils.copyProperties(user, user2);
        user2.setId(null);
        user2.setVersion(null);
        user2.setRoles(null);

        // try saving as new user, this should fail UserExistsException b/c of unique keys
        manager.saveUser(user2);
    }
}
