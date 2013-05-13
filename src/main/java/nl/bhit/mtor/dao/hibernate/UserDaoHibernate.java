package nl.bhit.mtor.dao.hibernate;

import java.util.List;

import javax.persistence.Table;

import nl.bhit.mtor.dao.UserDao;
import nl.bhit.mtor.model.User;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

/**
 * This class interacts with Hibernate session to save/delete and
 * retrieve User objects.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *         Extended to implement Acegi UserDetailsService interface by David Carter david@carter.net
 *         Modified by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> to work with
 *         the new BaseDaoHibernate implementation that uses generics.
 *         Modified by jgarcia (updated to hibernate 4)
 */
@Repository("userDao")
public class UserDaoHibernate extends GenericDaoHibernate<User, Long> implements UserDao, UserDetailsService {

    /**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoHibernate() {
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        Query qry = getSession().createQuery("from User u order by upper(u.username)");
        return qry.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User saveUser(User user) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("user's id: " + user.getId());
        }
        try {
        	getSession().saveOrUpdate(user);
        } catch (NonUniqueObjectException nuoe) {
        	LOG.debug(nuoe.getMessage(), nuoe);
        	getSession().saveOrUpdate(getSession().merge(user));
        }
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getSession().flush();
        return user;
    }

    /**
     * Overridden simply to call the saveUser method. This is happening
     * because saveUser flushes the session and saveObject of BaseDaoHibernate
     * does not.
     * 
     * @param user
     *            the user to save
     * @return the modified user (with a primary key set if they're new)
     */
    @Override
    public User save(User user) {
        return this.saveUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        @SuppressWarnings("unchecked")
		List<User> users = (List<User>)getSession().createCriteria(User.class).add(Restrictions.eq("username", username)).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserPassword(Long userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
        Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
        return jdbcTemplate
                .queryForObject("select password from " + table.name() + " where id=?", String.class, userId);
    }

	@Override
	public List<User> getUsersWithToken(String token) {
		String hql = "from User u where u.qrToken = :qrToken";
		Query query = getSession().createQuery(hql);
		query.setString("qrToken", token);
		
		@SuppressWarnings("unchecked")
		List<User> users = query.list();
		return users;
	}
	
}
