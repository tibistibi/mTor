package nl.bhit.mtor.dao.hibernate;

import java.util.List;

import nl.bhit.mtor.dao.CompanyDao;
import nl.bhit.mtor.model.Company;
import nl.bhit.mtor.model.User;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("companyDao")
public class CompanyDaoHibernate extends GenericDaoHibernate<Company, Long> implements CompanyDao {

	public CompanyDaoHibernate() {
		super(Company.class);
	}

	@Override
	public List<Company> getAllByUser(User user) {
		Query query = getSession().createQuery("select c as company from Company as c left join c.projects as p left join p.users as u where u = :user");
		query.setLong("user", user.getId());
		return query.list();
	}
}
