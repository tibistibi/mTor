package nl.bhit.mtor.dao.hibernate;

import java.util.List;

import nl.bhit.mtor.dao.LookupDao;
import nl.bhit.mtor.model.Role;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of LookupDao.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Modified by jgarcia: updated to hibernate 4
 */
@Repository
public class LookupDaoHibernate implements LookupDao {
	
	private static final transient Logger LOG = Logger.getLogger(LookupDaoHibernate.class);
    
    private final SessionFactory sessionFactory;

    /**
     * Initialize LookupDaoHibernate with Hibernate SessionFactory.
     * 
     * @param sessionFactory
     */
    @Autowired
    public LookupDaoHibernate(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        LOG.debug("Retrieving all role names...");
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Role.class).list();
    }
}
