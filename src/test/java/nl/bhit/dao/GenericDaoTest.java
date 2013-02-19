package nl.bhit.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import nl.bhit.mtor.dao.GenericDao;
import nl.bhit.mtor.dao.hibernate.GenericDaoHibernate;
import nl.bhit.mtor.model.User;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class GenericDaoTest extends BaseDaoTestCase {
    Log log = LogFactory.getLog(GenericDaoTest.class);
    GenericDao<User, Long> genericDao;
    @Autowired
    SessionFactory sessionFactory;

    @Before
    public void setUp() {
        genericDao = new GenericDaoHibernate<User, Long>(User.class, sessionFactory);
    }

    @Test
    public void getUser() {
        User user = genericDao.get(-1L);
        assertNotNull(user);
        assertEquals("user", user.getUsername());
    }
}
