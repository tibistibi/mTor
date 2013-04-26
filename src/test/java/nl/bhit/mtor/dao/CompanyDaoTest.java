package nl.bhit.mtor.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nl.bhit.mtor.model.Company;
import nl.bhit.mtor.model.User;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CompanyDaoTest extends BaseDaoTestCase {
    @Autowired
    private CompanyDao companyDao;

    @Test
    public void testGetByUser() {
        LOG.debug("starting testGetByUser...");
        User user = new User();
        user.setId(-1L);
        List<Company> companies = companyDao.getAllByUser(user);
        assertEquals(1, companies.size());
        user.setId(-2L);
        companies = companyDao.getAllByUser(user);
        assertEquals(2, companies.size());
    }

}