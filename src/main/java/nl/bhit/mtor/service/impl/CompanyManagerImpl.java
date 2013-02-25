package nl.bhit.mtor.service.impl;

import java.util.List;

import nl.bhit.mtor.dao.CompanyDao;
import nl.bhit.mtor.model.Company;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.service.CompanyManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("companyManager")
public class CompanyManagerImpl extends GenericManagerImpl<Company, Long> implements CompanyManager {
	CompanyDao companyDao;

	@Autowired
	public CompanyManagerImpl(CompanyDao companyDao) {
		super(companyDao);
		this.companyDao = companyDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Company> getAllByUSer(User user) {
		return companyDao.getAllByUser(user);
	}

}