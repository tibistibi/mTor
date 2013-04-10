package nl.bhit.mtor.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectDaoTest extends BaseDaoTestCase {
    @Autowired
    private ProjectDao projectDao;

    @Test
    public void testGet() {
        User user = new User();
        user.setId(-1L);
        List<Project> projects = projectDao.getWithNonResolvedMessages(user);
        assertEquals("found projects", 1, projects.size());
    }
}