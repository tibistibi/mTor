package nl.bhit.mtor.dao;

import java.util.List;

import nl.bhit.mtor.model.Company;
import nl.bhit.mtor.model.User;

/**
 * An interface that provides a data management interface to the Company table.
 */
public interface CompanyDao extends GenericDao<Company, Long> {

    /**
     * will return all companies belonging to this user.
     * 
     * @param user
     *            the user to filter on
     * @return the found companies
     */
    List<Company> getAllByUser(User user);

}