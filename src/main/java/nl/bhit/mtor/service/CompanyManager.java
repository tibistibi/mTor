package nl.bhit.mtor.service;

import java.util.List;

import nl.bhit.mtor.model.Company;
import nl.bhit.mtor.model.User;

public interface CompanyManager extends GenericManager<Company, Long> {

	/**
	 * will return all companies belonging to this user.
	 * 
	 * @param user
	 *            the user to filter on
	 * @return the found companies
	 */
	List<Company> getAllByUSer(User user);

}